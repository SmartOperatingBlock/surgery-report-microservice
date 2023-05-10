/*
 * Copyright (c) 2023. Smart Operating Block
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package entity.healthcareuser

import entity.measurements.Temperature
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec

class VitalSignTest : StringSpec({
    val negativeValue = -1

    "heart beat cannot be negative" {
        shouldThrow<IllegalArgumentException> { VitalSign.HeartBeat(negativeValue) }
    }

    "diastolic blood pressure cannot be negative" {
        shouldThrow<IllegalArgumentException> { VitalSign.DiastolicBloodPressure(negativeValue) }
    }

    "systolic blood pressure cannot be negative" {
        shouldThrow<IllegalArgumentException> { VitalSign.SystolicBloodPressure(negativeValue) }
    }

    "respiratory rate cannot be negative" {
        shouldThrow<IllegalArgumentException> { VitalSign.RespiratoryRate(negativeValue) }
    }

    "Body temperature cannot be negative" {
        shouldThrow<IllegalArgumentException> { VitalSign.BodyTemperature(Temperature(negativeValue.toDouble())) }
    }
})
