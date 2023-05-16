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
import entity.healthprofessional.HealthProfessionalID
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
import usecase.aggregation.AggregateRoomEnvironmentalDataExtractor
import usecase.aggregation.AggregateVitalSignsExtractor
import usecase.aggregation.util.CollectionExtensions.takePeriod
import java.time.Instant
import java.util.Date

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
        this.surgicalProcess.id,
        Date.from(
            this.surgicalProcess.processStates.first {
                it.second == SurgicalProcessState.PreSurgery(SurgicalProcessStep.PATIENT_IN_PREPARATION)
            }.first,
        ),
        this.surgicalProcess.description,
        this.surgicalProcess.inChargeHealthProfessional,
        this.surgicalProcess.patientID,
        this.healthcareUser,
        computeAggregateData(),
        this.consumedImplantableMedicalDevices,
        this.medicalTechnologyUsage,
        this.healthProfessionalTrackingInformation,
    )

    private fun computeAggregateData(): Map<SurgicalProcessState, SurgeryProcessStepAggregateData> {
        val stateSorted = this.surgicalProcess.processStates.sortedBy { it.first }
        val finalDateIterator = stateSorted.map { it.first }.iterator()
        // the start date of the next step correspond to the end date of the previous one
        if (finalDateIterator.hasNext()) finalDateIterator.next()

        return stateSorted.associate { stateEntry ->
            val dateTimeFrom = stateEntry.first
            val dateTimeTo = if (finalDateIterator.hasNext()) finalDateIterator.next() else null
            val vitalSignsInPeriod = this.patientVitalSigns
                .takePeriod(dateTimeFrom, dateTimeTo)
                .map { pair -> pair.second }
            val environmentalDataInPeriod = this.environmentalData.mapValues { roomDataEntry ->
                roomDataEntry
                    .value
                    .takePeriod(dateTimeFrom, dateTimeTo)
                    .map { it.second }
            }
            Pair(
                stateEntry.second,
                SurgeryProcessStepAggregateData(
                    dateTimeFrom,
                    dateTimeTo,
                    AggregateVitalSignsExtractor(vitalSignsInPeriod).aggregate(),
                    environmentalDataInPeriod.mapValues { roomDataEntry ->
                        AggregateRoomEnvironmentalDataExtractor(roomDataEntry.value).aggregate()
                    },
                ),
            )
        }
    }
}
