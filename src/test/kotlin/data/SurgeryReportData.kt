/*
 * Copyright (c) 2023. Smart Operating Block
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package data

import data.SurgicalProcessData.listOfTimedPatientVitalSigns
import data.SurgicalProcessData.listOfTimedRoomEnvironmentalData
import data.SurgicalProcessData.listOfhealthProfessionalTrackingData
import data.SurgicalProcessData.sampleConsumedImplantableMedicalDevices
import data.SurgicalProcessData.sampleMedicalTechnologyUsage
import data.SurgicalProcessData.simpleSurgicalProcess
import entity.healthcareuser.HealthcareUser
import entity.process.SurgicalProcessStep
import entity.report.SurgeryProcessStepAggregateData
import entity.report.SurgeryReport
import entity.room.Room
import entity.room.RoomType
import usecase.aggregation.AggregateRoomEnvironmentalDataExtractor
import usecase.aggregation.AggregateVitalSignsExtractor
import usecase.aggregation.util.CollectionExtensions.takePeriod
import java.time.Instant

object SurgeryReportData {
    /**
     * Use:
     * - [simpleSurgicalProcess]
     * - [sampleConsumedImplantableMedicalDevices]
     * - [sampleMedicalTechnologyUsage]
     * - [listOfhealthProfessionalTrackingData]
     * - [listOfTimedPatientVitalSigns]
     * - [listOfTimedRoomEnvironmentalData].
     */
    val simpleCompleteSurgeryReport = SurgeryReport(
        simpleSurgicalProcess.id,
        Instant.parse("2020-10-03T08:10:00Z"),
        simpleSurgicalProcess.description,
        simpleSurgicalProcess.patientID,
        setOf(
            Room(simpleSurgicalProcess.preOperatingRoom, RoomType.PRE_OPERATING_ROOM),
            Room(simpleSurgicalProcess.operatingRoom, RoomType.OPERATING_ROOM),
        ),
        simpleSurgicalProcess.inChargeHealthProfessional,
        simpleSurgicalProcess.taxCode?.let { HealthcareUser(it, "Mario", "Rossi") },
        mapOf(
            SurgicalProcessStep.PATIENT_IN_PREPARATION to getAggregateData(
                Instant.parse("2020-10-03T08:10:00Z"),
                Instant.parse("2020-10-03T08:12:00Z"),
            ),
            SurgicalProcessStep.PATIENT_ON_OPERATING_TABLE to getAggregateData(
                Instant.parse("2020-10-03T08:12:00Z"),
                Instant.parse("2020-10-03T08:15:00Z"),
            ),
            SurgicalProcessStep.SURGERY_IN_PROGRESS to getAggregateData(
                Instant.parse("2020-10-03T08:15:00Z"),
                Instant.parse("2020-10-03T08:18:00Z"),
            ),
            SurgicalProcessStep.END_OF_SURGERY to getAggregateData(
                Instant.parse("2020-10-03T08:18:00Z"),
                Instant.parse("2020-10-03T08:20:00Z"),
            ),
            SurgicalProcessStep.PATIENT_UNDER_OBSERVATION to getAggregateData(
                Instant.parse("2020-10-03T08:20:00Z"),
                Instant.parse("2020-10-03T09:00:00Z"),
            ),
        ),
        sampleConsumedImplantableMedicalDevices,
        sampleMedicalTechnologyUsage,
        listOfhealthProfessionalTrackingData,
    )

    private fun getAggregateData(from: Instant, to: Instant) = SurgeryProcessStepAggregateData(
        from,
        to,
        patientVitalSignsAggregateData = AggregateVitalSignsExtractor(
            listOfTimedPatientVitalSigns.takePeriod(from, to).map { it.second },
        ).aggregate(),
        environmentalAggregateData = listOf(RoomType.PRE_OPERATING_ROOM, RoomType.OPERATING_ROOM)
            .associateWith { _ ->
                AggregateRoomEnvironmentalDataExtractor(
                    listOfTimedRoomEnvironmentalData.takePeriod(from, to).map { it.second },
                ).aggregate()
            },
    )
}
