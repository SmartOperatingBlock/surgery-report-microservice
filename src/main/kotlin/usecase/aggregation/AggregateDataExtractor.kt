/*
 * Copyright (c) 2023. Smart Operating Block
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package usecase.aggregation

import entity.measurements.AggregateData

/**
 * Interface that models an [AggregateData] extractor of type [T].
 */
fun interface AggregateDataExtractor<out T> {
    /**
     * Extract [AggregateData].
     */
    fun aggregate(): AggregateData<T>
}
