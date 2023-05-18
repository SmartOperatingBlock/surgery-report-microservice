/*
 * Copyright (c) 2023. Smart Operating Block
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package application.presenter.external.serialization

import application.presenter.external.model.PatientManagementIntegrationDtoModel
import application.presenter.external.model.StaffTrackingDtoModel
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
     * Extension method to obtain the [HealthcareUser] from an
     * [PatientManagementIntegrationDtoModel.HealthcareUserResultDto].
     * @return the deserialized healthcare user.
     */
    fun PatientManagementIntegrationDtoModel.HealthcareUserResultDto.toHealthcareUser(): HealthcareUser =
        HealthcareUser(
            taxCode = TaxCode(this.taxCode),
            name = this.name,
            surname = this.surname,
        )

    /**
     * Extension method to obtain the [TrackingInfo] of an [HealthProfessionalID] from a
     * [StaffTrackingDtoModel.TrackingInfoResultDto].
     * @return the deserialized tracking information about a health professional
     */
    fun StaffTrackingDtoModel.TrackingInfoResultDto.toHealthProfessionalTrackingInfo():
        TrackingInfo<HealthProfessionalID> = TrackingInfo(
        dateTime = Instant.parse(this.dateTime),
        individual = HealthProfessionalID(this.healthProfessionalId),
        roomID = RoomID(this.roomId),
        trackType = this.trackingType.toTrackingType(),
    )

    private fun StaffTrackingDtoModel.TrackingTypeResultDto.toTrackingType(): TrackType = when (this) {
        StaffTrackingDtoModel.TrackingTypeResultDto.ENTER -> TrackType.ENTER
        StaffTrackingDtoModel.TrackingTypeResultDto.EXIT -> TrackType.EXIT
    }
}
