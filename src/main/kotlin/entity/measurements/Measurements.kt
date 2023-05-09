/*
 * Copyright (c) 2023. Smart Operating Block
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package entity.measurements

/**
 * Temperature concept.
 * It is described by the current temperature [value] expressed in a [unit].
 */
data class Temperature(val value: Double, val unit: TemperatureUnit = TemperatureUnit.CELSIUS)

/**
 * This enum describe the possible [Temperature] unit of measurement.
 */
enum class TemperatureUnit {
    /**
     * Celsius unit.
     */
    CELSIUS,
}

/**
 * Percentage concept.
 * It represents a generic percentage.
 * Therefore, its [value] must be within 0 and 100.
 */
data class Percentage(val value: Double) {
    init {
        // Constructor validation
        require(value in 0.0..100.0) { "Value of the percentage must be between 0 and 100" }
    }
}

/**
 * Humidity concept.
 * It is described by the current [percentage] of humidity. So it describes the Relative Humidity.
 */
data class Humidity(val percentage: Percentage)

/**
 * Luminosity concept.
 * It is described by the current luminosity [value] expressed in a [unit].
 */
data class Luminosity(val value: Double, val unit: LightUnit = LightUnit.LUX) {
    init {
        // Constructor validation
        require(this.value >= 0)
    }
}

/**
 * This enum describe the possible [Luminosity] unit of measurement.
 */
enum class LightUnit {
    /**
     * Lux unit.
     */
    LUX,
}

/**
 * Describe the presence inside a room.
 * @param[presenceDetected] true if someone is in the room, false otherwise.
 */
data class Presence(val presenceDetected: Boolean)
