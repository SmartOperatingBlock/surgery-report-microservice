/*
 * Copyright (c) 2023. Smart Operating Block
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package entity.room

import entity.measurements.Humidity
import entity.measurements.Luminosity
import entity.measurements.Presence
import entity.measurements.Temperature

/**
 * It describes a room inside the Operating Block.
 * Each room has a [type] and is identified by an [id].
 */
data class Room(
    val id: RoomID,
    val type: RoomType,
) {
    override fun equals(other: Any?): Boolean = when {
        other === this -> true
        other is Room -> this.id == other.id
        else -> false
    }

    override fun hashCode(): Int = this.id.hashCode()
}

/**
 * Identification of a [Room].
 * @param[value] the id.
 */
data class RoomID(val value: String) {
    init {
        // Constructor validation: The id must not be empty
        require(this.value.isNotEmpty())
    }
}

/** Describes the type of [Room]. */
enum class RoomType {
    /** It is the Pre/Post Operating Room. */
    PRE_OPERATING_ROOM,

    /** It is the Operating Room. */
    OPERATING_ROOM,
}

/**
 * Wraps all the environmental data associated to a [Room].
 * So it describe:
 * - the [temperature] inside the room
 * - the [humidity] inside the room
 * - the [luminosity] inside the room
 * - the [presence] of someone in the room
 * All the data may be not present.
 */
data class RoomEnvironmentalData(
    val temperature: Temperature? = null,
    val humidity: Humidity? = null,
    val luminosity: Luminosity? = null,
    val presence: Presence? = null,
)
