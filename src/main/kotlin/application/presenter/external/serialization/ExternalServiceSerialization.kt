/*
 * Copyright (c) 2023. Smart Operating Block
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package application.presenter.external.serialization

import application.presenter.external.model.HealthcareUserResultDto
import entity.healthcareuser.HealthcareUser
import entity.healthcareuser.TaxCode

/**
 * Serialization for external system.
 */
object ExternalServiceSerialization {
    /**
     * Extension method to obtain the [HealthcareUser] from an [HealthcareUserResultDto].
     * @return the deserialized healthcare user.
     */
    fun HealthcareUserResultDto.toHealthcareUser(): HealthcareUser = HealthcareUser(
        taxCode = TaxCode(this.taxCode),
        name = this.name,
        surname = this.surname,
    )
}
