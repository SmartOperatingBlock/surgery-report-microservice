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
 * Describes a [value] with a [unit] of measurement.
 */
@Serializable
data class ValueWithUnit<T>(val value: T, val unit: String)
