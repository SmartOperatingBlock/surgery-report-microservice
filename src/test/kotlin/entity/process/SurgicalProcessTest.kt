/*
 * Copyright (c) 2023. Smart Operating Block
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package entity.process

import entity.healthcareuser.PatientID
import entity.healthprofessional.HealthProfessionalID
import entity.room.RoomID
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe

class SurgicalProcessTest : StringSpec({
    val surgicalProcess = SurgicalProcess(
        SurgicalProcessID("sp1"),
        "type1",
        PatientID("p1"),
        HealthProfessionalID("h1"),
        RoomID("preOp1"),
        RoomID("op1"),
    )
    val surgicalProcessUpdated = surgicalProcess.copy(description = "description updated")
    val differentSurgicalProcess = surgicalProcess.copy(id = SurgicalProcessID("sp2"))

    "surgical process id should not be empty" {
        shouldThrow<IllegalArgumentException> { SurgicalProcessID("") }
    }

    listOf(
        differentSurgicalProcess,
        null,
        4,
    ).forEach {
        "a surgical process should not be equal to other surgical processes with different id, other classes or null" {
            surgicalProcess shouldNotBe it
        }
    }

    "two surgical processes are equal only based on their id" {
        surgicalProcess shouldBe surgicalProcessUpdated
    }

    "two equal surgical processes should have the same hashcode" {
        surgicalProcess.hashCode() shouldBe surgicalProcessUpdated.hashCode()
    }

    "two different surgical processes should not have the same hashcode" {
        surgicalProcess.hashCode() shouldNotBe differentSurgicalProcess.hashCode()
    }

    SurgicalProcessStep.values().filter { it != SurgicalProcessStep.PATIENT_IN_PREPARATION }.forEach {
        "Pre-surgery state must be only in the supported steps" {
            shouldThrow<IllegalArgumentException> { SurgicalProcessState.PreSurgery(it) }
        }
    }

    SurgicalProcessStep.values().filter {
        it !in setOf(
            SurgicalProcessStep.PATIENT_ON_OPERATING_TABLE,
            SurgicalProcessStep.ANESTHESIA,
            SurgicalProcessStep.SURGERY_IN_PROGRESS,
            SurgicalProcessStep.END_OF_SURGERY,
        )
    }.forEach {
        "Surgery state must be only in the supported steps" {
            shouldThrow<IllegalArgumentException> { SurgicalProcessState.Surgery(it) }
        }
    }

    SurgicalProcessStep.values().filter { it != SurgicalProcessStep.PATIENT_UNDER_OBSERVATION }.forEach {
        "Post-surgery state must be only in the supported steps" {
            shouldThrow<IllegalArgumentException> { SurgicalProcessState.PostSurgery(it) }
        }
    }
})
