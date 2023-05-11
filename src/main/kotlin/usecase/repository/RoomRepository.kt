/*
 * Copyright (c) 2023. Smart Operating Block
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package usecase.repository

import entity.room.RoomEnvironmentalData
import entity.room.RoomID
import java.time.Instant

/**
 * Interface that models the repository to manage rooms' data.
 */
fun interface RoomRepository {
    /**
     * Get a room environmental data [from] a specific date time [to] a specific date time.
     * The room is identified by its [roomID].
     */
    fun getRoomEnvironmentalData(roomID: RoomID, from: Instant, to: Instant): List<Pair<Instant, RoomEnvironmentalData>>
}
