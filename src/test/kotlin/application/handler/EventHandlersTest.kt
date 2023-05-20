/*
 * Copyright (c) 2023. Smart Operating Block
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package application.handler

import application.handler.SerializationUtils.toEventDto
import application.presenter.event.model.Event
import application.presenter.event.model.ProcessStateEventDto
import application.presenter.event.model.ProcessStepEventDto
import application.presenter.event.model.SurgicalProcessSummaryEvent
import application.presenter.event.model.SurgicalProcessSummaryPayload
import application.service.SurgeryReportService
import data.SurgicalProcessData.listOfTimedPatientVitalSigns
import data.SurgicalProcessData.sampleConsumedImplantableMedicalDevices
import data.SurgicalProcessData.sampleMedicalTechnologyUsage
import data.SurgicalProcessData.simpleSurgicalProcess
import infrastructure.database.SurgeryReportDatabase
import infrastructure.database.withMongo
import infrastructure.external.testdouble.RepositoryTestDoubles.healthProfessionalRepository
import infrastructure.external.testdouble.RepositoryTestDoubles.healthcareUserRepository
import infrastructure.external.testdouble.RepositoryTestDoubles.roomRepository
import io.kotest.assertions.throwables.shouldNotThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import java.time.Instant

class EventHandlersTest : StringSpec({
    fun getDatabase() = SurgeryReportDatabase("mongodb://localhost:27017")

    listOf(
        EventHandlers.SurgicalProcessSummaryEventHandler(
            getDatabase(),
            healthProfessionalRepository,
            roomRepository,
            healthcareUserRepository,
        ),
    ).forEach {
        "event handlers should safely handle events that can't be processed" {
            val nonExistentEvent = object : Event<Int> {
                override val key: String = "NON_EXISTENT_KEY"
                override val data: Int = 1
                override val dateTime: String = Instant.now().toString()
            }
            it.canHandle(nonExistentEvent) shouldBe false
            shouldNotThrow<Exception> { it.consume(nonExistentEvent) }
        }
    }

    "surgery process summary event must be handled correctly" {
        withMongo {
            val database = getDatabase()
            SurgeryReportService.GetSurgeryReport(
                simpleSurgicalProcess.id,
                database,
            ).execute() shouldBe null
            val event = SurgicalProcessSummaryEvent(
                key = SurgicalProcessSummaryEvent.SURGICAL_PROCESS_SUMMARY_EVENT_KEY,
                data = SurgicalProcessSummaryPayload(
                    simpleSurgicalProcess.id.value,
                    simpleSurgicalProcess.description,
                    simpleSurgicalProcess.patientID.value,
                    simpleSurgicalProcess.taxCode?.value,
                    simpleSurgicalProcess.inChargeHealthProfessional?.value,
                    simpleSurgicalProcess.preOperatingRoom.value,
                    simpleSurgicalProcess.operatingRoom.value,
                    listOf(
                        "2020-10-03T08:10:00Z" to ProcessStateEventDto.PRE_SURGERY,
                        "2020-10-03T08:12:00Z" to ProcessStateEventDto.SURGERY,
                        "2020-10-03T08:20:00Z" to ProcessStateEventDto.POST_SURGERY,
                        "2020-10-03T09:00:00Z" to ProcessStateEventDto.TERMINATED,
                    ),
                    listOf(
                        "2020-10-03T08:10:00Z" to ProcessStepEventDto.PATIENT_IN_PREPARATION,
                        "2020-10-03T08:12:00Z" to ProcessStepEventDto.PATIENT_ON_OPERATING_TABLE,
                        "2020-10-03T08:15:00Z" to ProcessStepEventDto.SURGERY_IN_PROGRESS,
                        "2020-10-03T08:18:00Z" to ProcessStepEventDto.END_OF_SURGERY,
                        "2020-10-03T08:20:00Z" to ProcessStepEventDto.PATIENT_UNDER_OBSERVATION,
                    ),
                    listOfTimedPatientVitalSigns.map { it.first.toString() to it.second.toEventDto() },
                    sampleConsumedImplantableMedicalDevices.map { it.toEventDto() },
                    sampleMedicalTechnologyUsage.map { it.toEventDto() },
                ),
                dateTime = Instant.now().toString(),
            )
            val eventHandler = EventHandlers.SurgicalProcessSummaryEventHandler(
                database,
                healthProfessionalRepository,
                roomRepository,
                healthcareUserRepository,
            )
            eventHandler.canHandle(event) shouldBe true
            shouldNotThrow<Exception> { eventHandler.consume(event) }
            SurgeryReportService.GetSurgeryReport(
                simpleSurgicalProcess.id,
                database,
            ).execute()?.surgicalProcessID shouldBe simpleSurgicalProcess.id
        }
    }
})
