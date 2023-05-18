/*
 * Copyright (c) 2023. Smart Operating Block
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package application.presenter.external.model

import kotlinx.serialization.Serializable

/**
 * Module that wraps all the api model of building management service.
 */
object BuildingManagementDtoModel {
    /**
     * It represents the result returned by external building management system, and it models building information.
     * The information returned are:
     * - the [temperature] inside the room
     * - the [humidity] inside the room
     * - the [luminosity] inside the room
     * - the [presence] of someone in the room
     * All the data may be not present.
     */
    @Serializable
    data class EnvironmentalDataApiDto(
        val temperature: ValueWithExplicitUnit<Double, TemperatureUnitResultDto>? = null,
        val humidity: Double? = null,
        val luminosity: ValueWithExplicitUnit<Double, LuminosityUnitResultDto>? = null,
        val presence: Boolean? = null,
    )

    /**
     * It represents a [value] of type [V], with a [unit] of measurement [T].
     */
    @Serializable
    data class ValueWithExplicitUnit<out V : Number, out T>(val value: V, val unit: T)

    /**
     * It represents the unit of measurement returned by the building management system for the temperature.
     */
    @Serializable
    enum class TemperatureUnitResultDto {
        /** Celsius unit of measurement. */
        CELSIUS,
    }

    /**
     * It represents the unit of measurement returned by the building management system for the luminosity.
     */
    @Serializable
    enum class LuminosityUnitResultDto {
        /** Lux unit of measurement. */
        LUX,
    }
}
