/*
 * Copyright (c) 2023. Smart Operating Block
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package application.presenter.event.model

import kotlinx.serialization.Serializable

/**
 * Presenter class to represent patient vitals signs that is contained in events.
 * The vital signs received are: the [heartBeat], the [diastolicBloodPressure], the [systolicBloodPressure],
 * the [respiratoryRate], the [saturationPercentage] and the [bodyTemperature].
 */
@Serializable
data class PatientVitalSignsEventDto(
    val heartBeat: HeartBeatEventDto? = null,
    val diastolicBloodPressure: DiastolicBloodPressureEventDto? = null,
    val systolicBloodPressure: SystolicBloodPressureEventDto? = null,
    val respiratoryRate: RespiratoryRateEventDto? = null,
    val saturationPercentage: SaturationPercentageEventDto? = null,
    val bodyTemperature: BodyTemperatureEventDto? = null,
)

/** Presenter class to represent heartbeat vital sign, through [bpm], that is contained in events. */
@Serializable
data class HeartBeatEventDto(val bpm: Int)

/** Presenter class to represent diastolic blood [pressure] vital sign that is contained in events. */
@Serializable
data class DiastolicBloodPressureEventDto(val pressure: Int)

/** Presenter class to represent systolic blood [pressure] vital sign that is contained in events. */
@Serializable
data class SystolicBloodPressureEventDto(val pressure: Int)

/** Presenter class to represent respiratory [rate] vital sign that is contained in events. */
@Serializable
data class RespiratoryRateEventDto(val rate: Int)

/** Presenter class to represent saturation [percentage] vital sign that is contained in events. */
@Serializable
data class SaturationPercentageEventDto(val percentage: Int)

/**
 * Presenter class to represent body temperature vital sign that is contained in events.
 * It is expressed in [degree] with a [unit] of measurement.
 */
@Serializable
data class BodyTemperatureEventDto(val degree: Double, val unit: TemperatureUnitEventDto)

/** Presenter enum that represent the temperature unit of [BodyTemperatureEventDto]. */
@Serializable
enum class TemperatureUnitEventDto {
    CELSIUS,
}
