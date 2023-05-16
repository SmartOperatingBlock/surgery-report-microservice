/*
 * Copyright (c) 2023. Smart Operating Block
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package application.presenter.api.model.healthcareuser

import kotlinx.serialization.Serializable

/**
 * Presenter class to serialize [entity.healthcareuser.HealthcareUser] information.
 * The necessary information are the [taxCode], the [name] and the [surname].
 */
@Serializable
data class HealthcareUserApiDto(
    val taxCode: String,
    val name: String,
    val surname: String,
)
