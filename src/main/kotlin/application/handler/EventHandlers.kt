/*
 * Copyright (c) 2023. Smart Operating Block
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package application.handler

import application.presenter.event.model.Event
import application.presenter.event.model.SurgicalProcessSummaryEvent
import application.presenter.event.serialization.EventSerialization.extractImplantableMedicalDeviceUsage
import application.presenter.event.serialization.EventSerialization.extractMedicalTechnologyUsage
import application.presenter.event.serialization.EventSerialization.extractPatientVitalSigns
import application.presenter.event.serialization.EventSerialization.extractSurgicalProcess
import application.service.SurgeryReportService
import usecase.repository.HealthProfessionalRepository
import usecase.repository.HealthcareUserRepository
import usecase.repository.RoomRepository
import usecase.repository.SurgeryReportRepository

/**
 * Module that wraps the event handlers.
 */
object EventHandlers {
    /**
     * Event handler for the [SurgicalProcessSummaryEvent].
     */
    class SurgicalProcessSummaryEventHandler(
        private val surgeryReportRepository: SurgeryReportRepository,
        private val healthProfessionalRepository: HealthProfessionalRepository,
        private val roomRepository: RoomRepository,
        private val healthcareUserRepository: HealthcareUserRepository,
    ) : EventHandler {
        override fun canHandle(event: Event<*>): Boolean = event is SurgicalProcessSummaryEvent

        override fun consume(event: Event<*>) {
            event.cast<SurgicalProcessSummaryEvent> {
                SurgeryReportService.GenerateSurgeryReport(
                    this.data.extractSurgicalProcess(),
                    this.data.extractPatientVitalSigns(),
                    this.data.extractImplantableMedicalDeviceUsage(),
                    this.data.extractMedicalTechnologyUsage(),
                    surgeryReportRepository,
                    healthProfessionalRepository,
                    roomRepository,
                    healthcareUserRepository,
                ).execute() != null
            }
        }
    }

    private inline fun <reified T> Any?.cast(operation: T.() -> Boolean = { true }): Boolean = if (this is T) {
        operation()
    } else {
        false
    }
}
