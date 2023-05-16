/*
 * Copyright (c) 2023. Smart Operating Block
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package usecase

import entity.process.SurgicalProcess
import entity.process.SurgicalProcessState
import entity.process.SurgicalProcessStep
import java.time.Instant

/**
 * This use case has the objective of getting the start and end date time from a [surgicalProcess].
 */
class GetSurgicalProcessStartEndUseCase(
    private val surgicalProcess: SurgicalProcess,
) : UseCase<Pair<Instant, Instant>> {
    /**
     * The first element contains the start time, and the second the end time.
     */
    override fun execute(): Pair<Instant, Instant> =
        Pair(
            this.surgicalProcess.processStates.first {
                it.second == SurgicalProcessState.PreSurgery(SurgicalProcessStep.PATIENT_IN_PREPARATION)
            }.first,
            this.surgicalProcess.processStates.first {
                it.second is SurgicalProcessState.Interrupted || it.second is SurgicalProcessState.Terminated
            }.first,
        )
}
