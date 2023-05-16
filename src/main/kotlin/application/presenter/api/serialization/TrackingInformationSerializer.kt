/*
 * Copyright (c) 2023. Smart Operating Block
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package application.presenter.api.serialization

import application.presenter.api.model.tracking.TrackApiDtoType
import application.presenter.api.model.tracking.TrackingInformationApiDto
import entity.healthprofessional.HealthProfessionalID
import entity.tracking.TrackType
import entity.tracking.TrackingInfo

/**
 * Serializers for data to return to API.
 */
object TrackingInformationSerializer {
    /**
     * Extension method to obtain the api dto of tracking information about an health professional.
     */
    fun TrackingInfo<HealthProfessionalID>.toApiDto(): TrackingInformationApiDto = TrackingInformationApiDto(
        dateTime = this.dateTime.toString(),
        healthProfessionalId = this.individual.value,
        roomID = this.roomID.value,
        trackType = this.trackType.toApiDtoType(),
    )

    private fun TrackType.toApiDtoType(): TrackApiDtoType = when (this) {
        TrackType.ENTER -> TrackApiDtoType.ENTER
        TrackType.EXIT -> TrackApiDtoType.EXIT
    }
}
