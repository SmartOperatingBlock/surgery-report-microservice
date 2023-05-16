/*
 * Copyright (c) 2023. Smart Operating Block
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package usecase.aggregation

import entity.measurements.AggregateData
import entity.measurements.Humidity
import entity.measurements.Luminosity
import entity.measurements.Percentage
import entity.measurements.Temperature
import entity.room.RoomEnvironmentalData
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.doubles.plusOrMinus
import io.kotest.matchers.shouldBe
import usecase.SurgicalProcessData.listOfRoomEnvironmentalData

class AggregateRoomEnvironmentalDataExtractorTest : StringSpec({
    val average = RoomEnvironmentalData(
        Temperature(32.3),
        Humidity(Percentage(61.6)),
        Luminosity(183.3),
    )

    val maximum = RoomEnvironmentalData(
        Temperature(35.0),
        Humidity(Percentage(80.0)),
        Luminosity(300.0),
    )

    val minimum = RoomEnvironmentalData(
        Temperature(30.0),
        Humidity(Percentage(50.0)),
        Luminosity(100.0),
    )

    val standardDeviation = RoomEnvironmentalData(
        Temperature(2.05),
        Humidity(Percentage(13.12)),
        Luminosity(84.98),
    )

    val aggregateData = AggregateRoomEnvironmentalDataExtractor(listOfRoomEnvironmentalData).aggregate()
    val tolerance = 0.1

    "It should be able to extract average data of a collection of room environmental data" {
        aggregateData.average.temperature?.value shouldBe (average.temperature?.value?.plusOrMinus(tolerance))
        aggregateData.average.humidity?.percentage?.value shouldBe
            (average.humidity?.percentage?.value?.plusOrMinus(tolerance))
        aggregateData.average.luminosity?.value shouldBe (average.luminosity?.value?.plusOrMinus(tolerance))
    }

    "It should be able to extract standard deviation from a collection of room environmental data" {
        aggregateData.std.temperature?.value shouldBe (standardDeviation.temperature?.value?.plusOrMinus(tolerance))
        aggregateData.std.humidity?.percentage?.value shouldBe
            (standardDeviation.humidity?.percentage?.value?.plusOrMinus(tolerance))
        aggregateData.std.luminosity?.value shouldBe (standardDeviation.luminosity?.value?.plusOrMinus(tolerance))
    }

    "It should be able to extract maximum values from a collection of room environmental data" {
        aggregateData.maximum shouldBe maximum
    }

    "It should be able to extract minimum values from a collection of room environmental data" {
        aggregateData.minimum shouldBe minimum
    }

    "If the list is empty the extractor should work and return an empty instance of data" {
        AggregateRoomEnvironmentalDataExtractor(listOf()).aggregate() shouldBe AggregateData(
            RoomEnvironmentalData(),
            RoomEnvironmentalData(),
            RoomEnvironmentalData(),
            RoomEnvironmentalData(),
        )
    }
})
