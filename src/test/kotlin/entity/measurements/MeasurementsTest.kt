/*
 * Copyright (c) 2023. Smart Operating Block
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package entity.measurements

import io.kotest.assertions.throwables.shouldNotThrow
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec

class MeasurementsTest : StringSpec({
    val negativeLuminosityValue = -10.0
    val positiveLuminosityValue = 10.0
    val outsidePercentageValue = 110.0
    val correctPercentageValue = 100.0

    "it should be impossible to create a luminosity object with negative value" {
        shouldThrow<IllegalArgumentException> { Luminosity(negativeLuminosityValue) }
    }

    "it should be possible to create a correct luminosity with positive values" {
        shouldNotThrow<IllegalArgumentException> { Luminosity(positiveLuminosityValue) }
    }

    "it should not be possible to create a percentage with a value outside its range" {
        shouldThrow<IllegalArgumentException> { Percentage(outsidePercentageValue) }
    }

    "it should be possible to create a percentage with a value within its range" {
        shouldNotThrow<IllegalArgumentException> { Percentage(correctPercentageValue) }
    }
})
