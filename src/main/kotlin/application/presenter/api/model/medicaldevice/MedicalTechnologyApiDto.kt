/*
 * Copyright (c) 2023. Smart Operating Block
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package application.presenter.api.model.medicaldevice

import kotlinx.serialization.Serializable

/**
 * Presenter class for the [entity.medicaldevice.MedicalTechnology] class.
 * It returns: the [id], the [name], the [description], the [type] of the medical technology and moreover
 * if it is [inUse].
 */
@Serializable
data class MedicalTechnologyApiDto(
    val id: String,
    val name: String,
    val description: String? = null,
    val type: MedicalTechnologyApiDtoType,
    val inUse: Boolean = false,
)

/**
 * Presenter enum to handle medical technology type.
 */
@Serializable
enum class MedicalTechnologyApiDtoType {
    ENDOSCOPE,
    XRAY,
}

/**
 * Presenter class that represent the usage of a [medicalTechnology] in a specific [dateTime].
 */
@Serializable
data class MedicalTechnologyUsageApiDto(
    val dateTime: String,
    val medicalTechnology: MedicalTechnologyApiDto,
)
