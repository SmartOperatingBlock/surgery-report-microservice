/*
 * Copyright (c) 2023. Smart Operating Block
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package application.service

import application.presenter.api.serialization.SurgeryReportSerializer.toSurgeryReportEntry
import data.SurgeryReportData.simpleCompleteSurgeryReport
import data.SurgicalProcessData.listOfTimedPatientVitalSigns
import data.SurgicalProcessData.listOfTimedRoomEnvironmentalData
import data.SurgicalProcessData.listOfhealthProfessionalTrackingData
import data.SurgicalProcessData.sampleConsumedImplantableMedicalDevices
import data.SurgicalProcessData.sampleMedicalTechnologyUsage
import data.SurgicalProcessData.simpleSurgicalProcess
import entity.healthcareuser.HealthcareUser
import infrastructure.database.SurgeryReportDatabase
import infrastructure.database.withMongo
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import usecase.repository.HealthProfessionalRepository
import usecase.repository.HealthcareUserRepository
import usecase.repository.RoomRepository

class SurgeryReportServiceTest : StringSpec({
    val healthProfessionalRepository = HealthProfessionalRepository { _, _, _ -> listOfhealthProfessionalTrackingData }
    val roomRepository = RoomRepository { _, _, _ -> listOfTimedRoomEnvironmentalData }
    val healthcareUserRepository = HealthcareUserRepository { _ ->
        simpleSurgicalProcess.taxCode?.let { HealthcareUser(it, "Mario", "Rossi") }
    }

    fun getDatabase() = SurgeryReportDatabase("mongodb://localhost:27017")

    fun generateSurgeryReport() = SurgeryReportService.GenerateSurgeryReport(
        simpleSurgicalProcess,
        listOfTimedPatientVitalSigns,
        sampleConsumedImplantableMedicalDevices,
        sampleMedicalTechnologyUsage,
        getDatabase(),
        healthProfessionalRepository,
        roomRepository,
        healthcareUserRepository,
    ).execute()

    "it should be possible to generate a surgery report using the repositories to obtain the remaining data" {
        withMongo {
            generateSurgeryReport() shouldBe simpleCompleteSurgeryReport
        }
    }

    "it should not be possible to generate two surgery report with the same surgical process id" {
        withMongo {
            generateSurgeryReport()
            generateSurgeryReport() shouldBe null
        }
    }

    "it should be possible to obtain an existing surgery report" {
        withMongo {
            generateSurgeryReport()
            SurgeryReportService.GetSurgeryReport(
                simpleSurgicalProcess.id,
                getDatabase(),
            ).execute() shouldBe simpleCompleteSurgeryReport
        }
    }

    "it should handle the request of a non-existent surgery report" {
        withMongo {
            SurgeryReportService.GetSurgeryReport(simpleSurgicalProcess.id, getDatabase()).execute() shouldBe null
        }
    }

    "it should be possible to integrate an existent surgery report" {
        val additionalData = "additional data"
        withMongo {
            generateSurgeryReport()
            SurgeryReportService.IntegrateSurgeryReport(
                simpleSurgicalProcess.id,
                additionalData,
                getDatabase(),
            ).execute() shouldBe true
            SurgeryReportService.GetSurgeryReport(simpleSurgicalProcess.id, getDatabase())
                .execute()?.additionalData shouldBe additionalData
        }
    }

    "it should not be possible to integrate a non-existent surgery report" {
        val additionalData = "additional data"
        withMongo {
            SurgeryReportService.IntegrateSurgeryReport(
                simpleSurgicalProcess.id,
                additionalData,
                getDatabase(),
            ).execute() shouldBe false
        }
    }

    "it should be possible to obtain all the surgery report entries" {
        withMongo {
            generateSurgeryReport()
            val entries = SurgeryReportService.GetAllSurgeryReportEntry(getDatabase()).execute()
            entries.size shouldBe 1
            entries.contains(simpleCompleteSurgeryReport.toSurgeryReportEntry()) shouldBe true
        }
    }

    "it should be possible to obtain all the surgery report entries even when there aren't" {
        withMongo {
            SurgeryReportService.GetAllSurgeryReportEntry(getDatabase()).execute().size shouldBe 0
        }
    }
})
