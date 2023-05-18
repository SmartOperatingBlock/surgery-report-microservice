/*
 * Copyright (c) 2023. Smart Operating Block
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package application.presenter.external.serialization

import application.presenter.external.model.HealthcareUserResultDto
import application.presenter.external.model.TrackingInfoResultDto
import application.presenter.external.model.TrackingTypeResultDto
import entity.healthcareuser.HealthcareUser
import entity.healthcareuser.TaxCode
import entity.healthprofessional.HealthProfessionalID
import entity.room.RoomID
import entity.tracking.TrackType
import entity.tracking.TrackingInfo
import java.time.Instant

/**
 * Serialization for external system.
 */
object ExternalServiceSerialization {
    /**
     * Extension method to obtain the [HealthcareUser] from an [HealthcareUserResultDto].
     * @return the deserialized healthcare user.
     */
    fun HealthcareUserResultDto.toHealthcareUser(): HealthcareUser = HealthcareUser(
        taxCode = TaxCode(this.taxCode),
        name = this.name,
        surname = this.surname,
    )

    /**
     * Extension method to obtain the [TrackingInfo] of an [HealthProfessionalID] from a [TrackingInfoResultDto].
     * @return the deserialized tracking information about a health professional
     */
    fun TrackingInfoResultDto.toHealthProfessionalTrackingInfo(): TrackingInfo<HealthProfessionalID> = TrackingInfo(
        dateTime = Instant.parse(this.dateTime),
        individual = HealthProfessionalID(this.healthProfessionalId),
        roomID = RoomID(this.roomId),
        trackType = this.trackingType.toTrackingType(),
    )

    private fun TrackingTypeResultDto.toTrackingType(): TrackType = when (this) {
        TrackingTypeResultDto.ENTER -> TrackType.ENTER
        TrackingTypeResultDto.EXIT -> TrackType.EXIT
    }
}
