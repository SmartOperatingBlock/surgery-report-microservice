/*
 * Copyright (c) 2023. Smart Operating Block
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package infrastructure.database

import data.SurgicalProcessData.listOfTimedPatientVitalSigns
import data.SurgicalProcessData.listOfTimedRoomEnvironmentalData
import data.SurgicalProcessData.listOfhealthProfessionalTrackingData
import data.SurgicalProcessData.sampleConsumedImplantableMedicalDevices
import data.SurgicalProcessData.sampleMedicalTechnologyUsage
import data.SurgicalProcessData.simpleSurgicalProcess
import entity.healthcareuser.HealthcareUser
import entity.healthcareuser.TaxCode
import entity.room.RoomType
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.equality.shouldBeEqualToComparingFields
import io.kotest.matchers.shouldBe
import usecase.ReportGenerationUseCase

class SurgeryReportDatabaseTest : StringSpec({
    val surgeryReport = ReportGenerationUseCase(
        simpleSurgicalProcess,
        listOfTimedPatientVitalSigns,
        listOfhealthProfessionalTrackingData,
        mapOf(
            RoomType.PRE_OPERATING_ROOM to listOfTimedRoomEnvironmentalData,
            RoomType.OPERATING_ROOM to listOfTimedRoomEnvironmentalData,
        ),
        HealthcareUser(TaxCode("taxcode"), "Mario", "Rossi"),
        sampleConsumedImplantableMedicalDevices,
        sampleMedicalTechnologyUsage,
    ).execute()

    fun getDatabase() = SurgeryReportDatabase("mongodb://localhost:27017")

    "it should be able to store a surgery report" {
        withMongo {
            val database = getDatabase()
            database.createSurgeryReport(surgeryReport) shouldBe true
            database.findBy(surgeryReport.surgicalProcessID)?.shouldBeEqualToComparingFields(surgeryReport)
        }
    }

    "it should be able to integrate a surgery report" {
        val newAdditionalData = "new additional data"
        withMongo {
            val database = getDatabase()
            database.createSurgeryReport(surgeryReport)
            database.integrateSurgeryReport(
                surgeryReport.surgicalProcessID,
                newAdditionalData,
            ) shouldBe true
            database.findBy(surgeryReport.surgicalProcessID)?.additionalData shouldBe newAdditionalData
        }
    }

    "it should handle the integration of a non-existing surgery report" {
        val newAdditionalData = "new additional data"
        withMongo {
            val database = getDatabase()
            database.integrateSurgeryReport(
                surgeryReport.surgicalProcessID,
                newAdditionalData,
            ) shouldBe false
        }
    }

    "it should be possible to retrieve an existing surgery report" {
        withMongo {
            val database = getDatabase()
            database.createSurgeryReport(surgeryReport)
            database.findBy(surgeryReport.surgicalProcessID)?.shouldBeEqualToComparingFields(surgeryReport)
        }
    }

    "it should handle the request of retrieving a non-existent surgery report" {
        withMongo {
            val database = getDatabase()
            database.findBy(surgeryReport.surgicalProcessID) shouldBe null
        }
    }

    "it should be possible to retrieve the list of the surgery reports available" {
        withMongo {
            val database = getDatabase()
            database.createSurgeryReport(surgeryReport)
            database.getSurgeryReports().size shouldBe 1
            database.getSurgeryReports().contains(surgeryReport) shouldBe true
        }
    }

    "it should be possible to retrieve the list of the surgery reports available even if empty" {
        withMongo {
            val database = getDatabase()
            database.getSurgeryReports().size shouldBe 0
        }
    }
})
