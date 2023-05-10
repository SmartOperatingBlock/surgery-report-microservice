/*
 * Copyright (c) 2023. Smart Operating Block
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package entity.medicaldevice

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe

class MedicalTechnologyTest : StringSpec({
    val medicalTechnology = MedicalTechnology(
        MedicalTechnologyID("m1"),
        "endoscope",
        type = MedicalTechnologyType.ENDOSCOPE,
    )
    val medicalTechnologyUpdated = MedicalTechnology(
        MedicalTechnologyID("m1"),
        "endoscope updated",
        type = MedicalTechnologyType.ENDOSCOPE,
    )
    val differentMedicalTechnology = MedicalTechnology(
        MedicalTechnologyID("m2"),
        "endoscope",
        type = MedicalTechnologyType.ENDOSCOPE,
    )

    "medical technology id should not be empty" {
        shouldThrow<IllegalArgumentException> { MedicalTechnologyID("") }
    }

    listOf(
        differentMedicalTechnology,
        null,
        4,
    ).forEach {
        "a medical technology should not be equal to other technologies with different id, other classes or null" {
            medicalTechnology shouldNotBe it
        }
    }

    "two medical technologies are equal only based on their id" {
        medicalTechnology shouldBe medicalTechnologyUpdated
    }

    "two equal medical technologies should have the same hashcode" {
        medicalTechnology.hashCode() shouldBe medicalTechnologyUpdated.hashCode()
    }

    "two different medical technologies should not have the same hashcode" {
        medicalTechnology.hashCode() shouldNotBe differentMedicalTechnology.hashCode()
    }
})
