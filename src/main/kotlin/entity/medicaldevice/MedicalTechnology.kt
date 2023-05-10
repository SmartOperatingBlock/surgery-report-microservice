/*
 * Copyright (c) 2023. Smart Operating Block
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package entity.medicaldevice

/**
 * It describes a Medical Technology used inside an Operating Room.
 * A medical technology is of a particular [type] and is identified by its [id] and described by a [name] and
 * a [description].
 * The technology can be [inUse] inside the Operating Room.
 */
data class MedicalTechnology(
    val id: MedicalTechnologyID,
    val name: String,
    val description: String? = null,
    val type: MedicalTechnologyType,
    val inUse: Boolean = false,
) {
    override fun equals(other: Any?): Boolean = when {
        other === this -> true
        other is MedicalTechnology -> this.id == other.id
        else -> false
    }

    override fun hashCode(): Int = this.id.hashCode()
}

/**
 * Identification of [MedicalTechnology].
 * @param[value] the id.
 */
data class MedicalTechnologyID(val value: String) {
    init {
        // Constructor validation: the id must not be empty
        require(this.value.isNotEmpty())
    }
}

/** The types of [MedicalTechnology]. */
enum class MedicalTechnologyType {
    /** Endoscope technology. */
    ENDOSCOPE,

    /** X-ray technology. */
    XRAY,
}
