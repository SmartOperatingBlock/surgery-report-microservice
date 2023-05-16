/*
 * Copyright (c) 2023. Smart Operating Block
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package application.presenter.api.model.room

import application.presenter.api.model.measurements.ValueWithUnit
import kotlinx.serialization.Serializable

/**
 * Presenter class to serialize [entity.room.Room] information.
 * The necessary information are [id] and [type].
 */
@Serializable
data class RoomApiDto(
    val id: String,
    val type: RoomApiDtoType,
)

/**
 * Presenter enum class to represent the room type.
 */
@Serializable
enum class RoomApiDtoType {
    /** Operating room type. */
    OPERATING_ROOM,

    /** Pre-operating room type. */
    PRE_OPERATING_ROOM,
}

/**
 * Wraps all the environmental data associated to a Room.
 * So it describes:
 * - the [temperature] inside the room
 * - the [humidity] inside the room
 * - the [luminosity] inside the room
 * - the [presence] of someone in the room
 * All the data may be not present.
 */
@Serializable
data class RoomEnvironmentalDataApiDto(
    val temperature: ValueWithUnit<Double>? = null,
    val humidity: Double? = null,
    val luminosity: ValueWithUnit<Double>? = null,
    val presence: Boolean? = null,
)
