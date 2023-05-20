/*
 * Copyright (c) 2023. Smart Operating Block
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package application.handler

import application.presenter.event.model.BodyTemperatureEventDto
import application.presenter.event.model.DiastolicBloodPressureEventDto
import application.presenter.event.model.HeartBeatEventDto
import application.presenter.event.model.ImplantableMedicalDeviceEventDto
import application.presenter.event.model.ImplantableMedicalDeviceIDEventDto
import application.presenter.event.model.ImplantableMedicalDeviceTypeEventDto
import application.presenter.event.model.MedicalTechnologyEventDto
import application.presenter.event.model.MedicalTechnologyIDEventDto
import application.presenter.event.model.MedicalTechnologyTypeEventDto
import application.presenter.event.model.PatientVitalSignsEventDto
import application.presenter.event.model.RespiratoryRateEventDto
import application.presenter.event.model.SaturationPercentageEventDto
import application.presenter.event.model.SystolicBloodPressureEventDto
import application.presenter.event.model.TemperatureUnitEventDto
import entity.healthcareuser.PatientVitalSigns
import entity.measurements.TemperatureUnit
import entity.medicaldevice.ImplantableMedicalDevice
import entity.medicaldevice.ImplantableMedicalDeviceType
import entity.medicaldevice.MedicalTechnologyType
import entity.medicaldevice.MedicalTechnologyUsage

/**
 * Serialization util useful in tests for surgical summary event payload.
 */
object SerializationUtils {
    fun PatientVitalSigns.toEventDto(): PatientVitalSignsEventDto = PatientVitalSignsEventDto(
        heartBeat = this.heartBeat?.let { HeartBeatEventDto(it.bpm) },
        diastolicBloodPressure = this.diastolicBloodPressure?.let { DiastolicBloodPressureEventDto(it.pressure) },
        systolicBloodPressure = this.systolicBloodPressure?.let { SystolicBloodPressureEventDto(it.pressure) },
        respiratoryRate = this.respiratoryRate?.let { RespiratoryRateEventDto(it.rate) },
        saturationPercentage = this.saturationPercentage?.let {
            SaturationPercentageEventDto(it.percentage.value.toInt())
        },
        bodyTemperature = this.bodyTemperature?.let {
            BodyTemperatureEventDto(it.temperature.value, it.temperature.unit.toEventDto())
        },
    )

    private fun TemperatureUnit.toEventDto(): TemperatureUnitEventDto = when (this) {
        TemperatureUnit.CELSIUS -> TemperatureUnitEventDto.CELSIUS
    }

    fun ImplantableMedicalDevice.toEventDto(): ImplantableMedicalDeviceEventDto = ImplantableMedicalDeviceEventDto(
        id = ImplantableMedicalDeviceIDEventDto(this.id.value),
        type = this.type.toEventDto(),
    )

    private fun ImplantableMedicalDeviceType.toEventDto(): ImplantableMedicalDeviceTypeEventDto =
        when (this) {
            ImplantableMedicalDeviceType.PACEMAKER -> ImplantableMedicalDeviceTypeEventDto.PACE_MAKER
            ImplantableMedicalDeviceType.CATHETER -> ImplantableMedicalDeviceTypeEventDto.CATHETER
        }

    fun MedicalTechnologyUsage.toEventDto(): Pair<String, MedicalTechnologyEventDto> =
        this.first.toString() to MedicalTechnologyEventDto(
            id = MedicalTechnologyIDEventDto(this.second.id.value),
            name = this.second.name,
            description = this.second.description,
            type = this.second.type.toEventDto(),
            inUse = this.second.inUse,
        )

    private fun MedicalTechnologyType.toEventDto(): MedicalTechnologyTypeEventDto = when (this) {
        MedicalTechnologyType.ENDOSCOPE -> MedicalTechnologyTypeEventDto.ENDOSCOPE
        MedicalTechnologyType.XRAY -> MedicalTechnologyTypeEventDto.X_RAY
    }
}
