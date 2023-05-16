/*
 * Copyright (c) 2023. Smart Operating Block
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package application.presenter.api.model.tracking

import kotlinx.serialization.Serializable

/**
 * Presenter class for [entity.tracking.TrackingInfo] class.
 * It returns: the [dateTime] of the track event, the [healthProfessionalId] involved, the [roomID] associated to
 * the [trackType] that describe the action performed in the tracking event.
 */
@Serializable
data class TrackingInformationApiDto(
    val dateTime: String,
    val healthProfessionalId: String,
    val roomID: String,
    val trackType: TrackApiDtoType,
)

/**
 * Presenter enum class for [entity.tracking.TrackType].
 */
@Serializable
enum class TrackApiDtoType {
    /** Enter a room. */
    ENTER,

    /** Exit from a room. */
    EXIT,
}
