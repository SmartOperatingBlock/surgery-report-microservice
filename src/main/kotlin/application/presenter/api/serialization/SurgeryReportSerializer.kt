/*
 * Copyright (c) 2023. Smart Operating Block
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package application.presenter.api.serialization

import application.presenter.api.model.SurgeryReportApiDto
import application.presenter.api.model.SurgeryReportEntry
import application.presenter.api.model.SurgicalProcessStepAggregateDataApiDto
import application.presenter.api.model.measurements.AggregateDataApiDto
import application.presenter.api.serialization.HealthcareUserSerializer.toApiDto
import application.presenter.api.serialization.MedicalDeviceSerializer.toApiDto
import application.presenter.api.serialization.RoomSerializer.toApiDto
import application.presenter.api.serialization.RoomSerializer.toApiDtoType
import application.presenter.api.serialization.SurgicalProcessSerializer.toApiDto
import application.presenter.api.serialization.TrackingInformationSerializer.toApiDto
import entity.report.SurgeryProcessStepAggregateData
import entity.report.SurgeryReport

/**
 * Serializers for data to return to API.
 */
object SurgeryReportSerializer {
    /**
     * Extension method to obtain the surgery report entry information.
     * @return the surgery report entry.
     */
    fun SurgeryReport.toSurgeryReportEntry(): SurgeryReportEntry = SurgeryReportEntry(
        surgicalProcessID = this.surgicalProcessID.value,
        patientId = this.patientID.value,
        patientName = this.healthcareUser?.name,
        patientSurname = this.healthcareUser?.surname,
        surgicalProcessDescription = this.surgicalProcessDescription,
        surgeryDate = this.surgeryDate.toString(),
    )

    /**
     * Extension method to obtain the surgery report api dto.
     * @return the surgery report dto for api.
     */
    fun SurgeryReport.toApiDto(): SurgeryReportApiDto = SurgeryReportApiDto(
        surgicalProcessID = this.surgicalProcessID.value,
        surgeryDate = this.surgeryDate.toString(),
        surgicalProcessDescription = this.surgicalProcessDescription,
        inChargeHealthProfessionalID = this.inChargeHealthProfessional.value,
        patientID = this.patientID.value,
        roomsInvolved = this.roomsInvolved.map { it.toApiDto() },
        healthcareUser = this.healthcareUser?.toApiDto(),
        stepData = this.stepData
            .mapKeys { (step, _) -> step.toApiDto() }
            .mapValues { (_, data) -> data.toApiDto() },
        consumedImplantableMedicalDevice = this.consumedImplantableMedicalDevices.map { it.toApiDto() }.toSet(),
        medicalTechnologyUsageData = this.medicalTechnologyUsageData
            .map { it.first.toString() to it.second.toApiDto() }
            .toSet(),
        healthProfessionalTrackingInformation = this.healthProfessionalTrackingInformation.map { it.toApiDto() },
        additionalData = this.additionalData,
    )

    private fun SurgeryProcessStepAggregateData.toApiDto(): SurgicalProcessStepAggregateDataApiDto =
        SurgicalProcessStepAggregateDataApiDto(
            startDateTime = this.startDateTime.toString(),
            stopDateTime = this.stopDateTime?.toString(),
            patientVitalSignAggregateData = AggregateDataApiDto(
                average = this.patientVitalSignsAggregateData.average.toApiDto(),
                std = this.patientVitalSignsAggregateData.std.toApiDto(),
                maximum = this.patientVitalSignsAggregateData.maximum.toApiDto(),
                minimum = this.patientVitalSignsAggregateData.minimum.toApiDto(),
            ),
            environmentalAggregateData = this.environmentalAggregateData
                .mapKeys { (type, _) -> type.toApiDtoType() }
                .mapValues { (_, data) ->
                    AggregateDataApiDto(
                        average = data.average.toApiDto(),
                        std = data.std.toApiDto(),
                        maximum = data.maximum.toApiDto(),
                        minimum = data.minimum.toApiDto(),
                    )
                },
        )
}
