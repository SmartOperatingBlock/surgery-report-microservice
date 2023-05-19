/*
 * Copyright (c) 2023. Smart Operating Block
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package infrastructure.events

import org.apache.kafka.clients.consumer.KafkaConsumer
import java.time.Duration

/**
 * This class manage the Kafka client needed to consume events.
 */
class KafkaClient {
    init {
        checkNotNull(System.getenv(BOOTSTRAP_SERVER_URL_VARIABLE)) { "kafka bootstrap server url required" }
        checkNotNull(System.getenv(SCHEMA_REGISTRY_URL_VARIABLE)) { "kafka schema registry url required" }
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
                    println("${event.key()} - ${event.value()}")
                }
            }
        }
    }

    companion object {
        private const val BOOTSTRAP_SERVER_URL_VARIABLE = "BOOTSTRAP_SERVER_URL"
        private const val SCHEMA_REGISTRY_URL_VARIABLE = "SCHEMA_REGISTRY_URL"
        private const val PROCESS_SUMMARY_EVENTS_TOPIC = "process-summary-events"
        private const val POLLING_TIME = 100L
    }
}
