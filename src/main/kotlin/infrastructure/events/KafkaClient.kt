/*
 * Copyright (c) 2023. Smart Operating Block
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package infrastructure.events

import application.handler.EventHandler
import application.handler.EventHandlers
import application.presenter.event.serialization.EventSerialization.toEvent
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.clients.consumer.KafkaConsumer
import usecase.repository.HealthProfessionalRepository
import usecase.repository.HealthcareUserRepository
import usecase.repository.RoomRepository
import usecase.repository.SurgeryReportRepository
import java.time.Duration
import java.time.format.DateTimeParseException

/**
 * This class manage the Kafka client needed to consume events.
 * It takes the [surgeryReportRepository], the [healthProfessionalRepository], the [roomRepository]
 * and the [healthcareUserRepository].
 */
class KafkaClient(
    private val surgeryReportRepository: SurgeryReportRepository,
    private val healthProfessionalRepository: HealthProfessionalRepository,
    private val roomRepository: RoomRepository,
    private val healthcareUserRepository: HealthcareUserRepository,
) {
    private val eventHandlers: List<EventHandler>

    init {
        checkNotNull(System.getenv(BOOTSTRAP_SERVER_URL_VARIABLE)) { "kafka bootstrap server url required" }
        checkNotNull(System.getenv(SCHEMA_REGISTRY_URL_VARIABLE)) { "kafka schema registry url required" }
        this.eventHandlers = listOf(
            EventHandlers.SurgicalProcessSummaryEventHandler(
                this.surgeryReportRepository,
                this.healthProfessionalRepository,
                this.roomRepository,
                this.healthcareUserRepository,
            ),
        )
    }

    private val kafkaConsumer = KafkaConsumer<String, String>(
        loadConsumerProperties(
            System.getenv(BOOTSTRAP_SERVER_URL_VARIABLE),
            System.getenv(SCHEMA_REGISTRY_URL_VARIABLE),
        ),
    )

    /**
     * Start consuming event on Kafka.
     */
    fun start() {
        this.kafkaConsumer.subscribe(listOf(PROCESS_SUMMARY_EVENTS_TOPIC)).run {
            while (true) {
                kafkaConsumer.poll(Duration.ofMillis(POLLING_TIME)).forEach { event ->
                    try {
                        consumeEvent(event)
                    } catch (e: IllegalArgumentException) {
                        println("INFO: Event discarded! - $e")
                    } catch (e: DateTimeParseException) {
                        println("ERROR: Invalid Date in event. Event discarded! - $e")
                    }
                }
            }
        }
    }

    private fun consumeEvent(event: ConsumerRecord<String, String>) {
        val deserializedEvent = event.value().toEvent(event.key())
        this.eventHandlers
            .filter { it.canHandle(deserializedEvent) }
            .forEach { it.consume(deserializedEvent) }
    }

    companion object {
        private const val BOOTSTRAP_SERVER_URL_VARIABLE = "BOOTSTRAP_SERVER_URL"
        private const val SCHEMA_REGISTRY_URL_VARIABLE = "SCHEMA_REGISTRY_URL"
        private const val PROCESS_SUMMARY_EVENTS_TOPIC = "process-summary-events"
        private const val POLLING_TIME = 100L
    }
}
