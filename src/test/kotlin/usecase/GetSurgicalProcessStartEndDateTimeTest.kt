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
import usecase.SurgicalProcessData.simpleSurgicalProcessInterrupted
import java.time.Instant

class GetSurgicalProcessStartEndDateTimeTest : StringSpec({
    val startDateTime = Instant.parse("2020-10-03T08:10:00Z")
    val stopDateTimeTerminated = Instant.parse("2020-10-03T09:00:00Z")
    val stopDateTimeInterrupted = Instant.parse("2020-10-03T08:25:00Z")

    "It should be able to extract the date time from a surgical process that is terminated correctly" {
        val dateTime = GetSurgicalProcessStartEndUseCase(simpleSurgicalProcess).execute()
        dateTime shouldBe (startDateTime to stopDateTimeTerminated)
    }

    "It should be able to extract the date time from a surgical process that has been interrupted" {
        val dateTime = GetSurgicalProcessStartEndUseCase(simpleSurgicalProcessInterrupted).execute()
        dateTime shouldBe (startDateTime to stopDateTimeInterrupted)
    }
})
