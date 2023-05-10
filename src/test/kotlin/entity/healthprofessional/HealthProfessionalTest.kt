/*
 * Copyright (c) 2023. Smart Operating Block
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package entity.healthprofessional

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec

class HealthProfessionalTest : StringSpec({
    "health professional id should not be empty" {
        shouldThrow<IllegalArgumentException> { HealthProfessionalID("") }
    }
})
