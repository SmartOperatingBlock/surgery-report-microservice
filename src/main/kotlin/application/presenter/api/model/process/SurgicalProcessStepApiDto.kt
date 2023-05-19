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
enum class SurgicalProcessStepApiDto {
    /** Pre-surgery state - patient in preparation step. */
    PATIENT_IN_PREPARATION,

    /** Surgery state - patient on operating table step. */
    PATIENT_ON_OPERATING_TABLE,

    /** Surgery state - anesthesia step. */
    ANESTHESIA,

    /** Surgery state - surgery in progress step. */
    SURGERY_IN_PROGRESS,

    /** Surgery state - end of surgery step. */
    END_OF_SURGERY,

    /** Post-surgery state - patient under observation step. */
    PATIENT_UNDER_OBSERVATION,
}
