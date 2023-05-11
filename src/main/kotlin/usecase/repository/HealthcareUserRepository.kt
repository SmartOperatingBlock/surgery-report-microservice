/*
 * Copyright (c) 2023. Smart Operating Block
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package usecase.repository

import entity.healthcareuser.HealthcareUser
import entity.healthcareuser.TaxCode

/**
 * Interface that models the repository to handle data about healthcare users.
 */
fun interface HealthcareUserRepository {
    /**
     * Get a healthcare user data by its [taxCode].
     * The associated [HealthcareUser] if available, null otherwise.
     */
    fun getHealthcareUser(taxCode: TaxCode): HealthcareUser?
}
