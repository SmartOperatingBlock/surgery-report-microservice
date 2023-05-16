/*
 * Copyright (c) 2023. Smart Operating Block
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package usecase.repository

import entity.healthprofessional.HealthProfessionalID
import entity.room.RoomID
import entity.tracking.TrackingInfo
import java.time.Instant

/**
 * Interface that models the repository to manage health professionals data.
 */
fun interface HealthProfessionalRepository {
    /**
     * Get the tracking information within a room, identified by its [roomID], inside the Operating Block
     * of health professionals, [from] a specific date time [to] a specific date time.
     */
    fun getHealthProfessionalTrackingInfo(
        roomID: RoomID,
        from: Instant,
        to: Instant,
    ): List<TrackingInfo<HealthProfessionalID>>
}
