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
import entity.measurements.LightUnit
import entity.measurements.Luminosity
import entity.measurements.Percentage
import entity.measurements.Temperature
import entity.measurements.TemperatureUnit
import entity.room.RoomEnvironmentalData
import usecase.aggregation.util.CollectionExtensions.std

/**
 * Extract [AggregateData] from a list of [roomEnvironmentalData].
 * Aggregation is performed only on possible data, so presence is not considered.
 */
class AggregateRoomEnvironmentalDataExtractor(
    private val roomEnvironmentalData: Collection<RoomEnvironmentalData>,
) : AggregateDataExtractor<RoomEnvironmentalData> {
    override fun aggregate(): AggregateData<RoomEnvironmentalData> = AggregateData(
        average = this.roomEnvironmentalData.applyAggregationToRoomEnvironmentalData { this.average() },
        std = this.roomEnvironmentalData.applyAggregationToRoomEnvironmentalData { this.std() },
        maximum = this.roomEnvironmentalData.applyAggregationToRoomEnvironmentalData { this.max() },
        minimum = this.roomEnvironmentalData.applyAggregationToRoomEnvironmentalData { this.min() },
    )

    private fun Collection<RoomEnvironmentalData>.applyAggregationToRoomEnvironmentalData(
        operation: Collection<Double>.() -> Double,
    ): RoomEnvironmentalData = RoomEnvironmentalData(
        temperature = Temperature(
            this.mapNotNull { it.temperature?.value }.operation(),
            TemperatureUnit.CELSIUS,
        ),
        humidity = Humidity(Percentage(this.mapNotNull { it.humidity?.percentage?.value }.operation())),
        luminosity = Luminosity(
            this.mapNotNull { it.luminosity?.value }.operation(),
            LightUnit.LUX,
        ),
    )
}
