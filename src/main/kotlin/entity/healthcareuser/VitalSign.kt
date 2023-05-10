/*
 * Copyright (c) 2023. Smart Operating Block
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package entity.healthcareuser

import entity.measurements.Percentage
import entity.measurements.Temperature

/**
 * Module that wraps all the vital signs for a patient under surgery.
 */
object VitalSign {
    /** The [bpm] of the patient. */
    data class HeartBeat(val bpm: Int) {
        init {
            require(this.bpm >= 0) {
                "Beat per minute cannot be negative"
            }
        }
    }

    /** The Diastolic blood [pressure] of the patient. */
    data class DiastolicBloodPressure(val pressure: Int) {
        init {
            require(this.pressure >= 0) {
                "Diastolic blood pressure cannot be negative"
            }
        }
    }

    /** The Systolic blood [pressure] of the patient. */
    data class SystolicBloodPressure(val pressure: Int) {
        init {
            require(this.pressure >= 0) {
                "Systolic blood pressure cannot be negative"
            }
        }
    }

    /** The Respiratory [rate] of the patient. */
    data class RespiratoryRate(val rate: Int) {
        init {
            require(this.rate >= 0) {
                "Respiratory rate cannot be negative"
            }
        }
    }

    /** The Saturation [percentage] of the patient. */
    data class SaturationPercentage(val percentage: Percentage)

    /** The body [temperature] of the patient. */
    data class BodyTemperature(val temperature: Temperature) {
        init {
            require(this.temperature.value >= 0) {
                "Body temperature cannot be negative"
            }
        }
    }
}
