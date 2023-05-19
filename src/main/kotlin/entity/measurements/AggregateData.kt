/*
 * Copyright (c) 2023. Smart Operating Block
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package entity.measurements

import kotlinx.serialization.Serializable

/**
 * It describes aggregate data of type [E].
 * It stores the [average], the [std], the [maximum] and the [minimum].
 */
@Serializable
data class AggregateData<out E>(
    val average: E,
    val std: E,
    val maximum: E,
    val minimum: E,
)
