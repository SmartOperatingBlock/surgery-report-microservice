/*
 * Copyright (c) 2023. Smart Operating Block
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package usecase

import entity.healthcareuser.HealthcareUser
import entity.healthcareuser.PatientVitalSigns
import entity.healthcareuser.VitalSign
import entity.healthprofessional.HealthProfessionalID
import entity.measurements.AggregateData
import entity.measurements.Humidity
import entity.measurements.LightUnit
import entity.measurements.Luminosity
import entity.measurements.Percentage
import entity.measurements.Presence
import entity.measurements.Temperature
import entity.measurements.TemperatureUnit
import entity.medicaldevice.ImplantableMedicalDevice
import entity.medicaldevice.MedicalTechnologyUsage
import entity.process.SurgicalProcess
import entity.process.SurgicalProcessState
import entity.process.SurgicalProcessStep
import entity.report.SurgeryProcessStepAggregateData
import entity.report.SurgeryReport
import entity.room.RoomEnvironmentalData
import entity.room.RoomType
import entity.tracking.TrackingInfo
import java.time.Instant
import java.util.Date
import kotlin.math.pow

/**
 * This use case has the objective of generate the [SurgeryReport] based on the information of the
 * surgical process and the related data. The data needed is:
 * - the description of the [surgicalProcess]
 * - all the [patientVitalSigns]
 * - the [healthProfessionalTrackingInformation]
 * - the [environmentalData]
 * - the [healthcareUser] if information are available
 * - the [consumedImplantableMedicalDevices]
 * - the [medicalTechnologyUsage]
 */
class ReportGenerationUseCase(
    private val surgicalProcess: SurgicalProcess,
    private val patientVitalSigns: List<Pair<Instant, PatientVitalSigns>>,
    private val healthProfessionalTrackingInformation: List<TrackingInfo<HealthProfessionalID>>,
    private val environmentalData: Map<RoomType, List<Pair<Instant, RoomEnvironmentalData>>>,
    private val healthcareUser: HealthcareUser? = null,
    private val consumedImplantableMedicalDevices: Set<ImplantableMedicalDevice> = setOf(),
    private val medicalTechnologyUsage: Set<MedicalTechnologyUsage> = setOf(),
) : UseCase<SurgeryReport> {
    override fun execute(): SurgeryReport = SurgeryReport(
        surgicalProcess.id,
        Date.from(
            surgicalProcess.processStates.first {
                it.second == SurgicalProcessState.PreSurgery(SurgicalProcessStep.PATIENT_IN_PREPARATION)
            }.first,
        ),
        surgicalProcess.description,
        surgicalProcess.inChargeHealthProfessional,
        surgicalProcess.patientID,
        healthcareUser,
        computeAggregateData(),
        consumedImplantableMedicalDevices,
        medicalTechnologyUsage,
        healthProfessionalTrackingInformation,
    )

    private fun computeAggregateData(): Map<SurgicalProcessState, SurgeryProcessStepAggregateData> {
        val stateSorted = this.surgicalProcess.processStates.sortedBy { it.first }
        val finalDateIterator = stateSorted.map { it.first }.sorted().iterator()

        return stateSorted.associate {
            val dateTimeFrom = it.first
            val dateTimeTo = if (finalDateIterator.hasNext()) finalDateIterator.next() else null
            val vitalSignsInPeriod = this.patientVitalSigns
                .takePeriod(dateTimeFrom, dateTimeTo)
                .map { pair -> pair.second }
            val environmentalDataInPeriod =
                this.environmentalData.mapValues { entry ->
                    entry
                        .value
                        .takePeriod(dateTimeFrom, dateTimeTo)
                        .map { it.second }
                }
            Pair(
                it.second,
                SurgeryProcessStepAggregateData(
                    it.first,
                    AggregateData(
                        average = vitalSignsInPeriod.applyAggregationToVitalSigns { this.average() },
                        variance = vitalSignsInPeriod.applyAggregationToVitalSigns { this.variance() },
                        maximum = vitalSignsInPeriod.applyAggregationToVitalSigns { this.max() },
                        minimum = vitalSignsInPeriod.applyAggregationToVitalSigns { this.min() },
                    ),
                    environmentalDataInPeriod.mapValues { roomDataEntry ->
                        with(roomDataEntry.value) {
                            AggregateData(
                                average = this.applyAggregationToRoomEnvironmentalData { this.average() },
                                variance = this.applyAggregationToRoomEnvironmentalData { this.variance() },
                                maximum = this.applyAggregationToRoomEnvironmentalData { this.max() },
                                minimum = this.applyAggregationToRoomEnvironmentalData { this.min() },
                            )
                        }
                    },
                ),
            )
        }
    }

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
        presence = Presence(
            this.mapNotNull { it.presence?.presenceDetected }.groupBy { it }.maxBy { it.value.size }.key,
        ),
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
            Temperature(this.mapNotNull { it.bodyTemperature?.temperature?.value }.average(), TemperatureUnit.CELSIUS),
        ),
    )
    private fun <X> Collection<Pair<Instant, X>>.takePeriod(from: Instant, to: Instant? = null) =
        this.filter {
            (to != null && it.first >= from && it.first <= to) || (to == null && it.first >= from)
        }
    private fun Collection<Double>.variance(): Double = with(this.average()) {
        this@variance.sumOf { (it - this).pow(2) } / this@variance.size
    }
}
