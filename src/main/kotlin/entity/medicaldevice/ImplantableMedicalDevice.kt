/*
 * Copyright (c) 2023. Smart Operating Block
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package entity.medicaldevice

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.time.Instant

/**
 * It describes an implantable medical device used during a surgery inside an Operating Room.
 * It is identified by its [id] and it is of a particular [type] and it is used in a specific [usageDateTime].
 */
@Serializable
data class ImplantableMedicalDevice(
    val id: ImplantableMedicalDeviceID,
    val type: ImplantableMedicalDeviceType,
    @Contextual val usageDateTime: Instant? = null,
) {
    override fun equals(other: Any?): Boolean = when {
        other === this -> true
        other is ImplantableMedicalDevice -> this.id == other.id
        else -> false
    }

    override fun hashCode(): Int = this.id.hashCode()
}

/**
 * Identification for [ImplantableMedicalDevice].
 * @param[value] the id.
 */
@Serializable
data class ImplantableMedicalDeviceID(val value: String) {
    init {
        // Constructor validation: the id must not be empty
        require(this.value.isNotEmpty())
    }
}

/**
 * The types of [ImplantableMedicalDevice].
 */
@Serializable
enum class ImplantableMedicalDeviceType {
    /** The Catheter is an implantable medical device type. */
    CATHETER,

    /** The pacemaker is an implantable medical device type. */
    PACEMAKER,
}
