/*
 * Copyright (c) 2023. Smart Operating Block
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package application.presenter.api.model.measurements

import kotlinx.serialization.Serializable

/**
 * Presenter class for the [entity.measurements.AggregateData] class.
 * It returns the following data: [average], [std], [maximum] and [minimum].
 */
@Serializable
data class AggregateDataApiDto<out E>(
    val average: E,
    val std: E,
    val maximum: E,
    val minimum: E,
)
