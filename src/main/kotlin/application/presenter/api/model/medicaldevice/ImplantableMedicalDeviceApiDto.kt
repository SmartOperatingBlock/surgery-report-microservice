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
 * Presenter class for the [entity.medicaldevice.ImplantableMedicalDevice] class.
 * It returns: the [id] of the implantable medical device, the [type] of it and its [usageDateTime] if present.
 */
@Serializable
data class ImplantableMedicalDeviceApiDto(
    val id: String,
    val type: ImplantableMedicalDeviceApiDtoType,
    val usageDateTime: String? = null,
)

/**
 * Presenter enum class to represent [entity.medicaldevice.ImplantableMedicalDeviceType].
 */
enum class ImplantableMedicalDeviceApiDtoType {
    /** The Catheter is an implantable medical device type. */
    CATHETER,

    /** The pacemaker is an implantable medical device type. */
    PACEMAKER,
}
