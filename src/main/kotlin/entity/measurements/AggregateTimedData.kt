/*
 * Copyright (c) 2023. Smart Operating Block
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package entity.measurements

import java.time.Instant

/**
 * It describes aggregate data of type [E].
 * It is called [AggregateTimedData] because the [maximum] and the [minimum] value are associated to the
 * instant of measurement. Moreover, it stores the [average] and the [variance].
 */
data class AggregateTimedData<out E>(
    val average: E,
    val variance: E,
    val maximum: Pair<Instant, E>,
    val minimum: Pair<Instant, E>,
)
