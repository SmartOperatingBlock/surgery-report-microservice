/*
 * Copyright (c) 2023. Smart Operating Block
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package application.presenter.event.model

import kotlinx.serialization.Serializable

/**
 * Presenter class of the summary of the surgical Process that comes as an event.
 * It includes:
 * - the [processId]
 * - the [processType]
 * - the [patientId]
 * - the [patientTaxCode]
 * - the [healthProfessionalId]
 * - the [preOperatingRoomId]
 * - the [operatingRoomId]
 * - the [processStates]
 * - the [processSteps]
 * - the [patientMedicalData]
 * - the [medicalDeviceUsage]
 * - the [medicalTechnologyUsage]
 */
@Serializable
data class SurgicalProcessSummaryPayload(
    val processId: String,
    val processType: String,
    val patientId: String,
    val patientTaxCode: String?,
    val healthProfessionalId: String?,
    val preOperatingRoomId: String,
    val operatingRoomId: String,
    val processStates: List<Pair<String, ProcessStateEventDto>>,
    val processSteps: List<Pair<String, ProcessStepEventDto>>,
    val patientMedicalData: List<Pair<String, PatientVitalSignsEventDto>>,
    val medicalDeviceUsage: List<ImplantableMedicalDeviceEventDto>,
    val medicalTechnologyUsage: List<Pair<String, MedicalTechnologyEventDto>>,
)

/** The different states of a [SurgicalProcessSummaryPayload]. */
@Serializable
enum class ProcessStateEventDto {
    PRE_SURGERY, SURGERY, POST_SURGERY, INTERRUPTED, TERMINATED
}

/** The different steps of a [SurgicalProcessSummaryPayload]. */
enum class ProcessStepEventDto {
    PATIENT_IN_PREPARATION,
    PATIENT_ON_OPERATING_TABLE,
    ANESTHESIA,
    SURGERY_IN_PROGRESS,
    END_OF_SURGERY,
    PATIENT_UNDER_OBSERVATION,
}
