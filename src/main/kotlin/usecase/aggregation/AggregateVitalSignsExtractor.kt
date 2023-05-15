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
        heartBeat = VitalSign.HeartBeat(this.mapNotNull { it.heartBeat?.bpm?.toDouble() }.operation().toInt()),
        diastolicBloodPressure = VitalSign.DiastolicBloodPressure(
            this.mapNotNull { it.diastolicBloodPressure?.pressure?.toDouble() }.operation().toInt(),
        ),
        systolicBloodPressure = VitalSign.SystolicBloodPressure(
            this.mapNotNull { it.systolicBloodPressure?.pressure?.toDouble() }.operation().toInt(),
        ),
        respiratoryRate = VitalSign.RespiratoryRate(
            this.mapNotNull { it.respiratoryRate?.rate?.toDouble() }.operation().toInt(),
        ),
        saturationPercentage = VitalSign.SaturationPercentage(
            Percentage(this.mapNotNull { it.saturationPercentage?.percentage?.value }.operation()),
        ),
        bodyTemperature = VitalSign.BodyTemperature(
            Temperature(
                this.mapNotNull { it.bodyTemperature?.temperature?.value }.operation(),
                TemperatureUnit.CELSIUS,
            ),
        ),
    )
}
