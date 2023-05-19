/*
 * Copyright (c) 2023. Smart Operating Block
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package application.presenter.event.model

import kotlinx.serialization.Serializable

/**
 * Presenter class that represents data about an Implantable medical device.
 * It has data about the [id] and the [type] of it.
 */
@Serializable
data class ImplantableMedicalDeviceEventDto(
    val id: ImplantableMedicalDeviceIDEventDto,
    val type: ImplantableMedicalDeviceTypeEventDto,
)

/**
 * Presenter class for the [id] of [ImplantableMedicalDeviceEventDto].
 */
@Serializable
data class ImplantableMedicalDeviceIDEventDto(val id: String)

/** The type of [ImplantableMedicalDeviceEventDto]. */
@Serializable
enum class ImplantableMedicalDeviceTypeEventDto {
    PACE_MAKER,
    CATHETER,
}

/**
 * Presenter class that represents data about a Medical technology.
 * It has data about the [id], the [name], the [description], its [type] and if it is [inUse] or not.
 */
@Serializable
data class MedicalTechnologyEventDto(
    val id: MedicalTechnologyIDEventDto,
    val name: String,
    val description: String? = null,
    val type: MedicalTechnologyTypeEventDto,
    val inUse: Boolean,
)

/**
 * Presenter class for the [id] of [MedicalTechnologyEventDto].
 */
@Serializable
data class MedicalTechnologyIDEventDto(val id: String)

/** The type of [MedicalTechnologyEventDto]. */
@Serializable
enum class MedicalTechnologyTypeEventDto {
    ENDOSCOPE,
    X_RAY,
}
