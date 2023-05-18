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
 * It represents the result returned by external staff tracking system, and it models tracking information of a
 * health professional. The information returned are: [dateTime], [roomId], [healthProfessionalId] and [trackingType].
 */
@Serializable
data class TrackingInfoResultDto(
    val dateTime: String,
    val roomId: String,
    val healthProfessionalId: String,
    val trackingType: TrackingTypeResultDto,
)

/**
 * It represents the [TrackingInfoResultDto] types.
 */
@Serializable
enum class TrackingTypeResultDto {
    /** Enter a room. */
    ENTER,

    /** Exit from a room. */
    EXIT,
}
