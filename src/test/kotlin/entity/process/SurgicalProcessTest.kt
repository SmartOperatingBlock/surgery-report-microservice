/*
 * Copyright (c) 2023. Smart Operating Block
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package entity.process

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe

class SurgicalProcessTest : StringSpec({
    val surgicalProcess = SurgicalProcess(SurgicalProcessID("sp1"), "type1")
    val surgicalProcessUpdated = SurgicalProcess(SurgicalProcessID("sp1"), "type2")
    val differentSurgicalProcess = SurgicalProcess(SurgicalProcessID("sp2"), "type1")

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
})
