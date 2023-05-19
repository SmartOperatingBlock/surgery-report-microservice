/*
 * Copyright (c) 2023. Smart Operating Block
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package entity.healthcareuser

import kotlinx.serialization.Serializable

/**
 * Id of [HealthcareUser] under a surgery.
 * @param[value] the id.
 */
@Serializable
data class PatientID(val value: String) {
    init {
        // Constructor validation: The id must not be empty
        require(this.value.isNotEmpty())
    }
}

/**
 * [VitalSign]s of a patient.
 * - [heartBeat]
 * - [diastolicBloodPressure]
 * - [systolicBloodPressure]
 * - [respiratoryRate]
 * - [saturationPercentage]
 * - [bodyTemperature]
 */
@Serializable
data class PatientVitalSigns(
    val heartBeat: VitalSign.HeartBeat? = null,
    val diastolicBloodPressure: VitalSign.DiastolicBloodPressure? = null,
    val systolicBloodPressure: VitalSign.SystolicBloodPressure? = null,
    val respiratoryRate: VitalSign.RespiratoryRate? = null,
    val saturationPercentage: VitalSign.SaturationPercentage? = null,
    val bodyTemperature: VitalSign.BodyTemperature? = null,
)
