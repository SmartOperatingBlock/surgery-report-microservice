/*
 * Copyright (c) 2023. Smart Operating Block
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package application.presenter.event.serialization

import application.presenter.event.model.Event
import application.presenter.event.model.ImplantableMedicalDeviceTypeEventDto
import application.presenter.event.model.MedicalTechnologyTypeEventDto
import application.presenter.event.model.PatientVitalSignsEventDto
import application.presenter.event.model.ProcessStateEventDto
import application.presenter.event.model.ProcessStepEventDto
import application.presenter.event.model.SurgicalProcessSummaryEvent
import application.presenter.event.model.SurgicalProcessSummaryPayload
import application.presenter.event.model.TemperatureUnitEventDto
import entity.healthcareuser.PatientID
import entity.healthcareuser.PatientVitalSigns
import entity.healthcareuser.TaxCode
import entity.healthcareuser.VitalSign
import entity.healthprofessional.HealthProfessionalID
import entity.measurements.Percentage
import entity.measurements.Temperature
import entity.measurements.TemperatureUnit
import entity.medicaldevice.ImplantableMedicalDevice
import entity.medicaldevice.ImplantableMedicalDeviceID
import entity.medicaldevice.ImplantableMedicalDeviceType
import entity.medicaldevice.MedicalTechnology
import entity.medicaldevice.MedicalTechnologyID
import entity.medicaldevice.MedicalTechnologyType
import entity.medicaldevice.MedicalTechnologyUsage
import entity.process.SurgicalProcess
import entity.process.SurgicalProcessID
import entity.process.SurgicalProcessState
import entity.process.SurgicalProcessStep
import entity.room.RoomID
import kotlinx.serialization.json.Json
import java.time.Instant

/** Module that wraps the event serialization. */
object EventSerialization {
    /**
     * Convert an event body to an [Event] object giving its [eventKey].
     * @throws IllegalArgumentException if the event cannot be deserialized.
     */
    fun String.toEvent(eventKey: String): Event<*> = when (eventKey) {
        SurgicalProcessSummaryEvent.SURGICAL_PROCESS_SUMMARY_EVENT_KEY ->
            Json.decodeFromString<SurgicalProcessSummaryEvent>(this)
        else -> throw IllegalArgumentException("Event not supported")
    }

    /**
     * Extension method to extract the information about the [SurgicalProcess] from a [SurgicalProcessSummaryPayload].
     */
    fun SurgicalProcessSummaryPayload.extractSurgicalProcess() = SurgicalProcess(
        id = SurgicalProcessID(this.processId),
        description = this.processType,
        patientID = PatientID(this.patientId),
        preOperatingRoom = RoomID(this.preOperatingRoomId),
        operatingRoom = RoomID(this.operatingRoomId),
        inChargeHealthProfessional = this.healthProfessionalId?.let { HealthProfessionalID(it) },
        taxCode = this.patientTaxCode?.let { TaxCode(it) },
        processStates = obtainStateEvolution(this.processStates, this.processSteps),
    )

    private fun obtainStateEvolution(
        states: List<Pair<String, ProcessStateEventDto>>,
        steps: List<Pair<String, ProcessStepEventDto>>,
    ): List<Pair<Instant, SurgicalProcessState>> =
        steps.map { Instant.parse(it.first) to it.second.toState() } + states.filter {
            it.second == ProcessStateEventDto.INTERRUPTED || it.second == ProcessStateEventDto.TERMINATED
        }.map { Instant.parse(it.first) to it.second.toState() }

    private fun ProcessStateEventDto.toState(): SurgicalProcessState = when (this) {
        ProcessStateEventDto.PRE_SURGERY -> SurgicalProcessState.PreSurgery(SurgicalProcessStep.PATIENT_IN_PREPARATION)
        ProcessStateEventDto.SURGERY -> SurgicalProcessState.Surgery(SurgicalProcessStep.PATIENT_ON_OPERATING_TABLE)
        ProcessStateEventDto.POST_SURGERY -> SurgicalProcessState.Surgery(SurgicalProcessStep.PATIENT_UNDER_OBSERVATION)
        ProcessStateEventDto.INTERRUPTED -> SurgicalProcessState.Interrupted()
        ProcessStateEventDto.TERMINATED -> SurgicalProcessState.Terminated()
    }

    private fun ProcessStepEventDto.toState(): SurgicalProcessState = when (this) {
        ProcessStepEventDto.PATIENT_IN_PREPARATION ->
            SurgicalProcessState.PreSurgery(SurgicalProcessStep.PATIENT_IN_PREPARATION)
        ProcessStepEventDto.PATIENT_ON_OPERATING_TABLE ->
            SurgicalProcessState.Surgery(SurgicalProcessStep.PATIENT_ON_OPERATING_TABLE)
        ProcessStepEventDto.ANESTHESIA -> SurgicalProcessState.Surgery(SurgicalProcessStep.ANESTHESIA)
        ProcessStepEventDto.SURGERY_IN_PROGRESS -> SurgicalProcessState.Surgery(SurgicalProcessStep.SURGERY_IN_PROGRESS)
        ProcessStepEventDto.END_OF_SURGERY -> SurgicalProcessState.Surgery(SurgicalProcessStep.END_OF_SURGERY)
        ProcessStepEventDto.PATIENT_UNDER_OBSERVATION ->
            SurgicalProcessState.PostSurgery(SurgicalProcessStep.PATIENT_UNDER_OBSERVATION)
    }

    /**
     * Extension method to extract the information about the [PatientVitalSigns] from a [SurgicalProcessSummaryPayload].
     */
    fun SurgicalProcessSummaryPayload.extractPatientVitalSigns(): List<Pair<Instant, PatientVitalSigns>> =
        this.patientMedicalData.map { Instant.parse(it.first) to it.second.toPatientVitalSigns() }

    private fun PatientVitalSignsEventDto.toPatientVitalSigns(): PatientVitalSigns = PatientVitalSigns(
        heartBeat = this.heartBeat?.let { VitalSign.HeartBeat(it.bpm) },
        diastolicBloodPressure = this.diastolicBloodPressure?.let { VitalSign.DiastolicBloodPressure(it.pressure) },
        systolicBloodPressure = this.systolicBloodPressure?.let { VitalSign.SystolicBloodPressure(it.pressure) },
        respiratoryRate = this.respiratoryRate?.let { VitalSign.RespiratoryRate(it.rate) },
        saturationPercentage = this.saturationPercentage?.let {
            VitalSign.SaturationPercentage(Percentage(it.percentage.toDouble()))
        },
        bodyTemperature = this.bodyTemperature?.let {
            VitalSign.BodyTemperature(Temperature(it.degree, it.unit.toTemperatureUnit()))
        },
    )

    private fun TemperatureUnitEventDto.toTemperatureUnit(): TemperatureUnit = when (this) {
        TemperatureUnitEventDto.CELSIUS -> TemperatureUnit.CELSIUS
    }

    /**
     * Extension method to extract the information about the consumed [ImplantableMedicalDevice]
     * from a [SurgicalProcessSummaryPayload].
     */
    fun SurgicalProcessSummaryPayload.extractImplantableMedicalDeviceUsage(): Set<ImplantableMedicalDevice> =
        this.medicalDeviceUsage.map {
            ImplantableMedicalDevice(ImplantableMedicalDeviceID(it.id.id), it.type.toImplantableMedicalDeviceType())
        }.toSet()

    private fun ImplantableMedicalDeviceTypeEventDto.toImplantableMedicalDeviceType(): ImplantableMedicalDeviceType =
        when (this) {
            ImplantableMedicalDeviceTypeEventDto.PACE_MAKER -> ImplantableMedicalDeviceType.PACEMAKER
            ImplantableMedicalDeviceTypeEventDto.CATHETER -> ImplantableMedicalDeviceType.CATHETER
        }

    /**
     * Extension method to extract the information about the usage of [MedicalTechnology]
     * from a [SurgicalProcessSummaryPayload].
     */
    fun SurgicalProcessSummaryPayload.extractMedicalTechnologyUsage(): Set<MedicalTechnologyUsage> =
        this.medicalTechnologyUsage.map {
            MedicalTechnologyUsage(
                Instant.parse(it.first),
                MedicalTechnology(
                    MedicalTechnologyID(it.second.id.id),
                    it.second.name,
                    it.second.description,
                    it.second.type.toMedicalTechnologyType(),
                    it.second.inUse,
                ),
            )
        }.toSet()

    private fun MedicalTechnologyTypeEventDto.toMedicalTechnologyType(): MedicalTechnologyType = when (this) {
        MedicalTechnologyTypeEventDto.ENDOSCOPE -> MedicalTechnologyType.ENDOSCOPE
        MedicalTechnologyTypeEventDto.X_RAY -> MedicalTechnologyType.XRAY
    }
}
