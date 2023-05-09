/*
 * Copyright (c) 2023. Smart Operating Block
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package entity.healthcareuser

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe

class HealthcareUserTest : StringSpec({
    val healthcareUser = HealthcareUser(TaxCode("p1"), "Andrea", "Verdi")
    val healthcareUserUpdated = HealthcareUser(TaxCode("p1"), "Max", "Rossi")
    val differentHealthcareUser = HealthcareUser(TaxCode("p2"), "Andrea", "Verdi")

    "tax code should not be empty" {
        shouldThrow<IllegalArgumentException> { TaxCode("") }
    }

    listOf(
        differentHealthcareUser,
        null,
        4,
    ).forEach {
        "a patient should not be equal to other patients with different tax code, other classes or null" {
            healthcareUser shouldNotBe it
        }
    }

    "two patients are equal only based on their tax code" {
        healthcareUser shouldBe healthcareUserUpdated
    }

    "two equal patient should have the same hashcode" {
        healthcareUser.hashCode() shouldBe healthcareUser.hashCode()
    }

    "two different patients should not have the same hashcode" {
        healthcareUser.hashCode() shouldNotBe differentHealthcareUser.hashCode()
    }
})
