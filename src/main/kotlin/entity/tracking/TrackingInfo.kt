/*
 * Copyright (c) 2023. Smart Operating Block
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package entity.tracking

import entity.room.RoomID
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.time.Instant

/**
 * It describes the tracking information of an [individual] of type [I].
 * It is interested to know the [dateTime] of the tracking information and the
 * [roomID] where it performs the action described in the [trackType].
 */
@Serializable
data class TrackingInfo<out I>(
    @Contextual val dateTime: Instant,
    val individual: I,
    val roomID: RoomID,
    val trackType: TrackType,
)

/** It describes the possible track information. */
@Serializable
enum class TrackType {
    /** Enter a room. */
    ENTER,

    /** Exit from a room. */
    EXIT,
}
