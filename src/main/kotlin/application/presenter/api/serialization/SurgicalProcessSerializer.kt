/*
 * Copyright (c) 2023. Smart Operating Block
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package application.presenter.api.serialization

import application.presenter.api.model.process.SurgicalProcessStateStepApiDto
import entity.process.SurgicalProcessState
import entity.process.SurgicalProcessStep

/**
 * Serializers for data to return to API.
 */
object SurgicalProcessSerializer {
    /**
     * Extension method to obtain the api dto of the surgical process state.
     */
    fun SurgicalProcessState.toApiDto(): SurgicalProcessStateStepApiDto = when (this) {
        SurgicalProcessState.PreSurgery(SurgicalProcessStep.PATIENT_IN_PREPARATION) ->
            SurgicalProcessStateStepApiDto.PRE_SURGERY_PATIENT_IN_PREPARATION
        SurgicalProcessState.Surgery(SurgicalProcessStep.PATIENT_ON_OPERATING_TABLE) ->
            SurgicalProcessStateStepApiDto.SURGERY_PATIENT_ON_OPERATING_TABLE
        SurgicalProcessState.Surgery(SurgicalProcessStep.ANESTHESIA) ->
            SurgicalProcessStateStepApiDto.SURGERY_ANESTHESIA
        SurgicalProcessState.Surgery(SurgicalProcessStep.SURGERY_IN_PROGRESS) ->
            SurgicalProcessStateStepApiDto.SURGERY_SURGERY_IN_PROGRESS
        SurgicalProcessState.Surgery(SurgicalProcessStep.END_OF_SURGERY) ->
            SurgicalProcessStateStepApiDto.SURGERY_END_OF_SURGERY
        SurgicalProcessState.PostSurgery(SurgicalProcessStep.PATIENT_UNDER_OBSERVATION) ->
            SurgicalProcessStateStepApiDto.POST_SURGERY_PATIENT_UNDER_OBSERVATION
        is SurgicalProcessState.Interrupted -> SurgicalProcessStateStepApiDto.INTERRUPTED
        is SurgicalProcessState.Terminated -> SurgicalProcessStateStepApiDto.TERMINATED
        else -> throw IllegalArgumentException("State not supported")
    }
}
