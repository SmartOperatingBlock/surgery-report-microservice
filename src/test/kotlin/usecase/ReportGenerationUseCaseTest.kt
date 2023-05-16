/*
 * Copyright (c) 2023. Smart Operating Block
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package usecase

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import usecase.SurgicalProcessData.simpleSurgicalProcess
import java.time.Instant
import java.util.Date

/*
    Here will be tested only the internal logic of the use case.
    Other aspects, that are external dependency have already been tested.
 */
class ReportGenerationUseCaseTest : StringSpec({
    val surgeryReport = ReportGenerationUseCase(
        simpleSurgicalProcess,
        listOf(),
        listOf(),
        mapOf(),
    ).execute()

    val patientInPreparationInstant = Instant.parse("2020-10-03T08:10:00Z")

    "The surgery date should correspond to the start instant in which the patient goes in preparation" {
        surgeryReport.surgeryDate shouldBe Date.from(patientInPreparationInstant)
    }

    "Date of start and end of each surgical process step must be respected in the aggregate data" {
        val processState = simpleSurgicalProcess.processStates.map { it.second }.iterator()
        val startDates = simpleSurgicalProcess.processStates.map { it.first }.iterator()
        val stopDates = simpleSurgicalProcess
            .processStates
            .map { it.first }
            .subList(1, simpleSurgicalProcess.processStates.size)
            .iterator()
        surgeryReport.statesData.forEach { (state, data) ->
            state shouldBe processState.next()
            data.startDateTime shouldBe startDates.next()
            data.stopDateTime?.run { this shouldBe stopDates.next() }
        }
    }
})
