/*
 * Copyright (c) 2023. Smart Operating Block
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package entity.process

import entity.healthcareuser.PatientID
import entity.healthcareuser.TaxCode
import entity.healthprofessional.HealthProfessionalID
import entity.room.RoomID

/**
 * It models the data associated to a surgical process that is happened inside the Operating Block.
 * Each process is identified by its [id] and it has a [type] that describes it.
 * Moreover, a surgical process during its progress pass between several [processStates].
 * The surgical process, obviously is a surgery that involve a [patientID], that when possible has a [taxCode]
 * associated, under the responsibility of a [inChargeHealthProfessional].
 * The process happens in two rooms of the Operating Block: the [preOperatingRoom] and the [operatingRoom].
 */
data class SurgicalProcess(
    val id: SurgicalProcessID,
    val type: String,
    val patientID: PatientID,
    val inChargeHealthProfessional: HealthProfessionalID,
    val preOperatingRoom: RoomID,
    val operatingRoom: RoomID,
    val taxCode: TaxCode? = null,
    val processStates: List<SurgicalProcessState> = listOf(),
) {
    override fun equals(other: Any?): Boolean = when {
        other === this -> true
        other is SurgicalProcess -> this.id == other.id
        else -> false
    }

    override fun hashCode(): Int = this.id.hashCode()
}

/**
 * Identification of a [SurgicalProcess].
 * @param[value] the id.
 */
data class SurgicalProcessID(val value: String) {
    init {
        // Constructor validation: The id must not be empty
        require(this.value.isNotEmpty())
    }
}

/**
 * The state of the [SurgicalProcess].
 * Each state is in a [currentStep] that must be supported.
 */
sealed class SurgicalProcessState(
    val currentStep: SurgicalProcessStep? = null,
    supportedSteps: Set<SurgicalProcessStep> = setOf(),
) {

    init {
        require(currentStep == null || currentStep in supportedSteps)
    }

    /** Pre-surgery state. */
    class PreSurgery(currentStep: SurgicalProcessStep) : SurgicalProcessState(
        currentStep,
        setOf(SurgicalProcessStep.PATIENT_IN_PREPARATION),
    )

    /** Surgery state. */
    class Surgery(currentStep: SurgicalProcessStep) : SurgicalProcessState(
        currentStep,
        setOf(
            SurgicalProcessStep.PATIENT_ON_OPERATING_TABLE,
            SurgicalProcessStep.ANESTHESIA,
            SurgicalProcessStep.SURGERY_IN_PROGRESS,
            SurgicalProcessStep.END_OF_SURGERY,
        ),
    )

    /** Post-surgery state. */
    class PostSurgery(currentStep: SurgicalProcessStep) : SurgicalProcessState(
        currentStep,
        setOf(SurgicalProcessStep.PATIENT_UNDER_OBSERVATION),
    )

    /** Interrupted state. The surgery is interrupted */
    class Interrupted : SurgicalProcessState()

    /** Terminated state. The surgery is terminated. */
    class Terminated : SurgicalProcessState()
}

/**
 * The steps involved in a [SurgicalProcessState] during a [SurgicalProcess].
 */
enum class SurgicalProcessStep {
    /** The patient is in preparation. */
    PATIENT_IN_PREPARATION,

    /** The patient is moved on the Operating Table. */
    PATIENT_ON_OPERATING_TABLE,

    /** The anesthesia is given to the patient. */
    ANESTHESIA,

    /** The surgery is in progress. */
    SURGERY_IN_PROGRESS,

    /** The surgery ends. */
    END_OF_SURGERY,

    /** The patient is under observation inside the post-operating room. */
    PATIENT_UNDER_OBSERVATION,
}
