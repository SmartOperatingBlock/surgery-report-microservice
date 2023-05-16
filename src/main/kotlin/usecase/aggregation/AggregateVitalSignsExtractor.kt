/*
 * Copyright (c) 2023. Smart Operating Block
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package usecase.aggregation

import entity.healthcareuser.PatientVitalSigns
import entity.healthcareuser.VitalSign
import entity.measurements.AggregateData
import entity.measurements.Percentage
import entity.measurements.Temperature
import entity.measurements.TemperatureUnit
import usecase.aggregation.util.CollectionExtensions.std

/**
 * Extract [AggregateData] from a list of patient [vitalSigns].
 */
class AggregateVitalSignsExtractor(
    private val vitalSigns: Collection<PatientVitalSigns>,
) : AggregateDataExtractor<PatientVitalSigns> {
    override fun aggregate(): AggregateData<PatientVitalSigns> = AggregateData(
        average = this.vitalSigns.applyAggregationToVitalSigns { this.average() },
        std = this.vitalSigns.applyAggregationToVitalSigns { this.std() },
        maximum = this.vitalSigns.applyAggregationToVitalSigns { this.max() },
        minimum = this.vitalSigns.applyAggregationToVitalSigns { this.min() },
    )

    private fun Collection<PatientVitalSigns>.applyAggregationToVitalSigns(
        operation: Collection<Double>.() -> Double,
    ): PatientVitalSigns = PatientVitalSigns(
        heartBeat = this.mapNotNull { it.heartBeat?.bpm?.toDouble() }.let {
            if (it.isNotEmpty()) VitalSign.HeartBeat(it.operation().toInt()) else null
        },
        diastolicBloodPressure = this.mapNotNull { it.diastolicBloodPressure?.pressure?.toDouble() }.let {
            if (it.isNotEmpty()) VitalSign.DiastolicBloodPressure(it.operation().toInt()) else null
        },
        systolicBloodPressure = this.mapNotNull { it.systolicBloodPressure?.pressure?.toDouble() }.let {
            if (it.isNotEmpty()) VitalSign.SystolicBloodPressure(it.operation().toInt()) else null
        },
        respiratoryRate = this.mapNotNull { it.respiratoryRate?.rate?.toDouble() }.let {
            if (it.isNotEmpty()) VitalSign.RespiratoryRate(it.operation().toInt()) else null
        },
        saturationPercentage = this.mapNotNull { it.saturationPercentage?.percentage?.value }.let {
            if (it.isNotEmpty()) VitalSign.SaturationPercentage(Percentage(it.operation())) else null
        },
        bodyTemperature = this.mapNotNull { it.bodyTemperature?.temperature?.value }.let {
            if (it.isNotEmpty()) {
                VitalSign.BodyTemperature(Temperature(it.operation(), TemperatureUnit.CELSIUS))
            } else { null }
        },
    )
}
