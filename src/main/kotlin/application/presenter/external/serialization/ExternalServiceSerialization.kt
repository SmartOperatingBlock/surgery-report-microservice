/*
 * Copyright (c) 2023. Smart Operating Block
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package application.presenter.external.serialization

import application.presenter.external.model.BuildingManagementDtoModel
import application.presenter.external.model.PatientManagementIntegrationDtoModel
import application.presenter.external.model.StaffTrackingDtoModel
import entity.healthcareuser.HealthcareUser
import entity.healthcareuser.TaxCode
import entity.healthprofessional.HealthProfessionalID
import entity.measurements.Humidity
import entity.measurements.LightUnit
import entity.measurements.Luminosity
import entity.measurements.Percentage
import entity.measurements.Presence
import entity.measurements.Temperature
import entity.measurements.TemperatureUnit
import entity.room.RoomEnvironmentalData
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

    /**
     * Extension method to obtain the [RoomEnvironmentalData] from a
     * [BuildingManagementDtoModel.EnvironmentalDataApiDto].
     * @return the deserialized room environmental data
     */
    fun BuildingManagementDtoModel.EnvironmentalDataApiDto.toRoomEnvironmentalData(): RoomEnvironmentalData =
        RoomEnvironmentalData(
            temperature = this.temperature?.let { Temperature(it.value, it.unit.toTemperatureUnit()) },
            humidity = this.humidity?.let { Humidity(Percentage(it)) },
            luminosity = this.luminosity?.let { Luminosity(it.value, it.unit.toLuminosityUnit()) },
            presence = this.presence?.let { Presence(it) },
        )

    private fun BuildingManagementDtoModel.TemperatureUnitResultDto.toTemperatureUnit(): TemperatureUnit = when (this) {
        BuildingManagementDtoModel.TemperatureUnitResultDto.CELSIUS -> TemperatureUnit.CELSIUS
    }

    private fun BuildingManagementDtoModel.LuminosityUnitResultDto.toLuminosityUnit(): LightUnit = when (this) {
        BuildingManagementDtoModel.LuminosityUnitResultDto.LUX -> LightUnit.LUX
    }
}
