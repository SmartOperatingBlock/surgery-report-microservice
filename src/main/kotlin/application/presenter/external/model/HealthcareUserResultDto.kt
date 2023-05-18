/*
 * Copyright (c) 2023. Smart Operating Block
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package application.presenter.external.model

import kotlinx.serialization.Serializable

/**
 * It represents the result returned by external healthcare user system, and it models healthcare user's information.
 * The information returned are: [taxCode], [name], [surname], [height], [weight], [birthdate] and [bloodGroup].
 */
@Serializable
data class HealthcareUserResultDto(
    val taxCode: String,
    val name: String,
    val surname: String,
    val height: Double,
    val weight: Double,
    val birthdate: String,
    val bloodGroup: String,
)
