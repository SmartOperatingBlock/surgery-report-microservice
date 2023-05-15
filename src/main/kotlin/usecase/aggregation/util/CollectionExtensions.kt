/*
 * Copyright (c) 2023. Smart Operating Block
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package usecase.aggregation.util

import java.time.Instant
import kotlin.math.pow
import kotlin.math.sqrt

/**
 * Module that wraps the extension for math aggregate data extraction.
 */
object CollectionExtensions {
    /**
     * Compute the standard deviation in of collection of numbers.
     */
    fun Collection<Double>.std(): Double = with(this.average()) {
        sqrt(this@std.sumOf { (it - this).pow(2) } / this@std.size)
    }

    /**
     * Return a sub-collection that is within the period indicated, [from] a specific [Instant] [to] another.
     */
    fun <X> Collection<Pair<Instant, X>>.takePeriod(from: Instant, to: Instant? = null) =
        this.filter {
            (to != null && it.first >= from && it.first <= to) || (to == null && it.first >= from)
        }
}
