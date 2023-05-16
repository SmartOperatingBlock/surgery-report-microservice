/*
 * Copyright (c) 2023. Smart Operating Block
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package application.presenter.api.model

import application.presenter.api.model.healthcareuser.HealthcareUserApiDto
import application.presenter.api.model.healthcareuser.PatientVitalSignsApiDto
import application.presenter.api.model.measurements.AggregateDataApiDto
import application.presenter.api.model.medicaldevice.ImplantableMedicalDeviceApiDto
import application.presenter.api.model.medicaldevice.MedicalTechnologyApiDto
import application.presenter.api.model.process.SurgicalProcessStateStepApiDto
import application.presenter.api.model.room.RoomApiDto
import application.presenter.api.model.room.RoomApiDtoType
import application.presenter.api.model.room.RoomEnvironmentalDataApiDto
import application.presenter.api.model.tracking.TrackingInformationApiDto
import kotlinx.serialization.Serializable

/**
 * It represents the presentation of a single [entity.report.SurgeryReport] entry.
 * The necessary information are the [surgicalProcessID], the [patientId], the [patientName], the [patientSurname],
 * the [surgicalProcessDescription] and the [surgeryDate].
 */
@Serializable
data class SurgeryReportEntry(
    val surgicalProcessID: String,
    val patientId: String,
    val patientName: String?,
    val patientSurname: String?,
    val surgicalProcessDescription: String,
    val surgeryDate: String,
)

/**
 * It represents the presentation of a [entity.report.SurgeryReport].
 * The necessary information are: the [surgicalProcessID], the [surgeryDate], the [surgicalProcessDescription],
 * the [inChargeHealthProfessionalID], the [patientID], the [roomsInvolved], the [healthcareUser], the [statesData],
 * the [consumedImplantableMedicalDevice], the [medicalTechnologyUsageData], the [healthProfessionalTrackingInformation]
 * and the [additionalData].
 */
@Serializable
data class SurgeryReportApiDto(
    val surgicalProcessID: String,
    val surgeryDate: String,
    val surgicalProcessDescription: String,
    val inChargeHealthProfessionalID: String,
    val patientID: String,
    val roomsInvolved: List<RoomApiDto>,
    val healthcareUser: HealthcareUserApiDto?,
    val statesData: Map<SurgicalProcessStateStepApiDto, SurgicalProcessStepAggregateDataApiDto>,
    val consumedImplantableMedicalDevice: Set<ImplantableMedicalDeviceApiDto>,
    val medicalTechnologyUsageData: Set<Pair<String, MedicalTechnologyApiDto>>,
    val healthProfessionalTrackingInformation: List<TrackingInformationApiDto>,
    val additionalData: String,
)

/**
 * Presenter class for the [entity.report.SurgeryProcessStepAggregateData].
 * It represents its [startDateTime], [stopDateTime], [patientVitalSignAggregateData] and [environmentalAggregateData].
 */
@Serializable
data class SurgicalProcessStepAggregateDataApiDto(
    val startDateTime: String,
    val stopDateTime: String?,
    val patientVitalSignAggregateData: AggregateDataApiDto<PatientVitalSignsApiDto>,
    val environmentalAggregateData: Map<RoomApiDtoType, AggregateDataApiDto<RoomEnvironmentalDataApiDto>>,
)
