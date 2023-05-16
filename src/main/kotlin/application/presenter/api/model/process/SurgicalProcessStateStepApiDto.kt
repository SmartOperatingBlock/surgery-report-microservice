/*
 * Copyright (c) 2023. Smart Operating Block
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package application.presenter.api.model.process

import kotlinx.serialization.Serializable

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
