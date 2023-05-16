/*
 * Copyright (c) 2023. Smart Operating Block
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package application.presenter.api.model

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

// @Serializable
// data class SurgeryReportApiDto(
//    val surgicalProcessID: String,
//    val surgeryDate: String,
//    val surgicalProcessDescription: String,
//    val inChargeHealthProfessionalID: String,
//    val patientID: String,
//    val roomsInvolved: List<RoomApiDto>,
//    val healthcareUser: HealthcareUserApiDto?,
//    val statesData: Map<SurgicalProcessStateStepApiDto, >
// )

/**
 * Presenter class to serialize [entity.room.Room] information.
 * The necessary information are [id] and [type].
 */
@Serializable
data class RoomApiDto(
    val id: String,
    val type: String,
)

/**
 * Presenter class to serialize [entity.healthcareuser.HealthcareUser] information.
 * The necessary information are the [taxCode], the [name] and the [surname].
 */
@Serializable
data class HealthcareUserApiDto(
    val taxCode: String,
    val name: String,
    val surname: String,
)

/**
* Presenter enum class to represent the surgical process states and steps.
*/
@Serializable
enum class SurgicalProcessStateStepApiDto {
    /** Pre-surgery state - patient in preparation step. */
    PRE_SURGERY_PATIENT_IN_PREPARATION,

    /** Surgery state - patient on operating table step. */
    SURGERY_PATIENT_ON_OPERATING_TABLE,

    /** Surgery state - anesthesia step. */
    SURGERY_ANESTHESIA,

    /** Surgery state - surgery in progress step. */
    SURGERY_SURGERY_IN_PROGRESS,

    /** Surgery state - end of surgery step. */
    SURGERY_END_OF_SURGERY,

    /** Post-surgery state - patient under observation step. */
    POST_SURGERY_PATIENT_UNDER_OBSERVATION,

    /** Interrupted state. */
    INTERRUPTED,

    /** Terminated state. */
    TERMINATED,
}
