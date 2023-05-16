/*
 * Copyright (c) 2023. Smart Operating Block
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package usecase.aggregation.util

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.doubles.plusOrMinus
import io.kotest.matchers.shouldBe
import usecase.aggregation.util.CollectionExtensions.std
import usecase.aggregation.util.CollectionExtensions.takePeriod
import java.time.Instant

class CollectionExtensionsTest : StringSpec({
    val collection = listOf(
        Pair(Instant.parse("2020-10-03T08:10:00Z"), 7.0),
        Pair(Instant.parse("2020-10-04T08:10:00Z"), 5.0),
        Pair(Instant.parse("2020-10-05T08:10:00Z"), 8.0),
        Pair(Instant.parse("2020-10-06T08:10:00Z"), 4.0),
        Pair(Instant.parse("2020-10-07T08:10:00Z"), 6.0),
    )
    val std = 1.41

    "std extension method should be able to compute standard deviation in a collection of double" {
        collection.map { it.second }.std() shouldBe (std plusOrMinus 0.01)
    }

    "it should be possible to extract data that is within a period" {
        collection.takePeriod(
            Instant.parse("2020-10-04T00:00:00Z"),
            Instant.parse("2020-10-07T00:00:00Z"),
        ) shouldBe listOf(
            Pair(Instant.parse("2020-10-04T08:10:00Z"), 5.0),
            Pair(Instant.parse("2020-10-05T08:10:00Z"), 8.0),
            Pair(Instant.parse("2020-10-06T08:10:00Z"), 4.0),
        )
    }

    "it should be possible to extract data from an instant" {
        collection.takePeriod(Instant.parse("2020-10-04T00:00:00Z")) shouldBe listOf(
            Pair(Instant.parse("2020-10-04T08:10:00Z"), 5.0),
            Pair(Instant.parse("2020-10-05T08:10:00Z"), 8.0),
            Pair(Instant.parse("2020-10-06T08:10:00Z"), 4.0),
            Pair(Instant.parse("2020-10-07T08:10:00Z"), 6.0),
        )
    }

    "if there isn't data in that period the takePeriod should work properly" {
        val emptyPeriod = collection.takePeriod(
            Instant.parse("2020-12-04T00:00:00Z"),
            Instant.parse("2020-12-05T00:00:00Z"),
        )
        emptyPeriod.size shouldBe 0
    }
})
