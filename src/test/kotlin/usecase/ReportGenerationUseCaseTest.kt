/*
 * Copyright (c) 2023. Smart Operating Block
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package usecase

import data.SurgeryReportData.simpleCompleteSurgeryReport
import data.SurgicalProcessData.listOfTimedPatientVitalSigns
import data.SurgicalProcessData.listOfTimedRoomEnvironmentalData
import data.SurgicalProcessData.listOfhealthProfessionalTrackingData
import data.SurgicalProcessData.sampleConsumedImplantableMedicalDevices
import data.SurgicalProcessData.sampleMedicalTechnologyUsage
import data.SurgicalProcessData.simpleSurgicalProcess
import entity.healthcareuser.HealthcareUser
import entity.room.RoomType
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import java.time.Instant

/*
    Here will be tested only the internal logic of the use case.
    Other aspects, that are external dependency have already been tested.
 */
class ReportGenerationUseCaseTest : StringSpec({
    val surgeryReport = ReportGenerationUseCase(
        simpleSurgicalProcess,
        listOf(),
        listOf(),
        mapOf(),
    ).execute()

    val patientInPreparationInstant = Instant.parse("2020-10-03T08:10:00Z")

    "The surgery date should correspond to the start instant in which the patient goes in preparation" {
        surgeryReport.surgeryDate shouldBe patientInPreparationInstant
    }

    "Date of start and end of each surgical process step must be respected in the aggregate data" {
        val stateToConsider = simpleSurgicalProcess.processStates.filter { it.second.currentStep != null }
        val processStep = stateToConsider.mapNotNull { it.second.currentStep }.iterator()
        val startDates = stateToConsider.map { it.first }.iterator()
        val stopDates = simpleSurgicalProcess
            .processStates
            .map { it.first }
            .subList(1, simpleSurgicalProcess.processStates.size)
            .iterator()
        surgeryReport.stepData.forEach { (state, data) ->
            state shouldBe processStep.next()
            data.startDateTime shouldBe startDates.next()
            data.stopDateTime?.run { this shouldBe stopDates.next() }
        }
    }

    "It should be able to generate a complete surgery report from the data related to a surgical process" {
        ReportGenerationUseCase(
            simpleSurgicalProcess,
            listOfTimedPatientVitalSigns,
            listOfhealthProfessionalTrackingData,
            mapOf(
                RoomType.PRE_OPERATING_ROOM to listOfTimedRoomEnvironmentalData,
                RoomType.OPERATING_ROOM to listOfTimedRoomEnvironmentalData,
            ),
            simpleSurgicalProcess.taxCode?.let { HealthcareUser(it, "Mario", "Rossi") },
            sampleConsumedImplantableMedicalDevices,
            sampleMedicalTechnologyUsage,
        ).execute() shouldBe simpleCompleteSurgeryReport
    }
})
