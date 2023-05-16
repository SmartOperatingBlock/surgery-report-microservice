/*
 * Copyright (c) 2023. Smart Operating Block
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package application.presenter.api.model.healthcareuser

import application.presenter.api.model.measurements.ValueWithUnit
import kotlinx.serialization.Serializable

/**
 * Presenter class to serialize [entity.healthcareuser.PatientVitalSigns].
 * It describes the [heartBeat], the [diastolicBloodPressure], the [systolicBloodPressure], the [respiratoryRate],
 * the [saturationPercentage] and the [bodyTemperature].
 */
@Serializable
data class PatientVitalSignsApiDto(
    val heartBeat: Int? = null,
    val diastolicBloodPressure: Int? = null,
    val systolicBloodPressure: Int? = null,
    val respiratoryRate: Int? = null,
    val saturationPercentage: Double? = null,
    val bodyTemperature: ValueWithUnit<Double>? = null,
)
