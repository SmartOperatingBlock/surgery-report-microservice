/*
 * Copyright (c) 2023. Smart Operating Block
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package entity.healthprofessional

/**
 * Identification of a health professional working withing the Operating Block.
 * @param[value] the id.
 */
data class HealthProfessionalID(val value: String) {
    init {
        // Constructor validation: the id must not be empty
        require(this.value.isNotEmpty())
    }
}
