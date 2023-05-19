/*
 * Copyright (c) 2023. Smart Operating Block
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package entity.healthcareuser

import kotlinx.serialization.Serializable

/**
 * It represents a Healthcare user of the Healthcare national system.
 * Each user is identified by their [taxCode] and has a [name] and a [surname].
 */
@Serializable
data class HealthcareUser(
    val taxCode: TaxCode,
    val name: String,
    val surname: String,
) {
    override fun equals(other: Any?): Boolean = when {
        other === this -> true
        other is HealthcareUser -> this.taxCode == other.taxCode
        else -> false
    }

    override fun hashCode(): Int = this.taxCode.hashCode()
}

/**
 * Tax Code of a [HealthcareUser].
 * @param[value] the tax code.
 */
@Serializable
data class TaxCode(val value: String) {
    init {
        // Constructor validation: the code must not be empty
        require(this.value.isNotEmpty())
    }
}
