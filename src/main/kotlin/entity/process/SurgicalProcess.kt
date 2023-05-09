/*
 * Copyright (c) 2023. Smart Operating Block
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package entity.process

/**
 * It describes a surgical process that is happened inside the Operating Block.
 * Each process is identified by its [id] and it has a [type] that describes it.
 */
data class SurgicalProcess(
    val id: SurgicalProcessID,
    val type: String,
) {
    override fun equals(other: Any?): Boolean = when {
        other === this -> true
        other is SurgicalProcess -> this.id == other.id
        else -> false
    }

    override fun hashCode(): Int = this.id.hashCode()
}

/**
 * Identification of a [SurgicalProcess].
 * @param[value] the id.
 */
data class SurgicalProcessID(val value: String) {
    init {
        // Constructor validation: The id must not be empty
        require(this.value.isNotEmpty())
    }
}
