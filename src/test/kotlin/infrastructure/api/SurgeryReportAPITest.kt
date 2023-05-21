/*
 * Copyright (c) 2023. Smart Operating Block
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package infrastructure.api

import application.presenter.api.model.SurgeryReportApiDto
import application.presenter.api.model.SurgeryReportEntry
import application.presenter.api.model.SurgeryReportPatchApiDto
import application.presenter.api.model.apiresponse.ApiResponses
import application.presenter.api.serialization.SurgeryReportSerializer.toApiDto
import application.presenter.api.serialization.SurgeryReportSerializer.toSurgeryReportEntry
import application.service.SurgeryReportService
import data.SurgeryReportData.simpleCompleteSurgeryReport
import data.SurgicalProcessData.listOfTimedPatientVitalSigns
import data.SurgicalProcessData.sampleConsumedImplantableMedicalDevices
import data.SurgicalProcessData.sampleMedicalTechnologyUsage
import data.SurgicalProcessData.simpleSurgicalProcess
import infrastructure.api.KtorTestingUtility.apiTestApplication
import infrastructure.database.SurgeryReportDatabase
import infrastructure.external.testdouble.RepositoryTestDoubles.healthProfessionalRepository
import infrastructure.external.testdouble.RepositoryTestDoubles.healthcareUserRepository
import infrastructure.external.testdouble.RepositoryTestDoubles.roomRepository
import io.kotest.assertions.ktor.client.shouldHaveStatus
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.ktor.client.request.get
import io.ktor.client.request.patch
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class SurgeryReportAPITest : StringSpec({

    fun insertSurgeryReport() = SurgeryReportService.GenerateSurgeryReport(
        simpleSurgicalProcess,
        listOfTimedPatientVitalSigns,
        sampleConsumedImplantableMedicalDevices,
        sampleMedicalTechnologyUsage,
        SurgeryReportDatabase("mongodb://localhost:27017"),
        healthProfessionalRepository,
        roomRepository,
        healthcareUserRepository,
    ).execute()

    "it should be possible to obtain all the surgery report entries" {
        apiTestApplication {
            insertSurgeryReport()
            val response = client.get("/api/v1/reports")
            response shouldHaveStatus HttpStatusCode.OK
            val entries =
                Json.decodeFromString<ApiResponses.ResponseEntryList<ApiResponses.ResponseEntry<SurgeryReportEntry>>>(
                    response.bodyAsText(),
                )
            entries.total shouldBe 1
            entries.entries.find { it.entry == simpleCompleteSurgeryReport.toSurgeryReportEntry() } shouldNotBe null
        }
    }

    "it should be possible to obtain a correct response for the entries even when there aren't anyone" {
        apiTestApplication {
            val response = client.get("/api/v1/reports")
            response shouldHaveStatus HttpStatusCode.NoContent
            val entries =
                Json.decodeFromString<ApiResponses.ResponseEntryList<ApiResponses.ResponseEntry<SurgeryReportEntry>>>(
                    response.bodyAsText(),
                )
            entries.total shouldBe 0
        }
    }

    "it should be possible to obtain an existent surgery report" {
        apiTestApplication {
            insertSurgeryReport()
            val response = client.get("/api/v1/reports/${simpleCompleteSurgeryReport.surgicalProcessID.value}")
            response shouldHaveStatus HttpStatusCode.OK
            Json.decodeFromString<SurgeryReportApiDto>(
                response.bodyAsText(),
            ) shouldBe simpleCompleteSurgeryReport.toApiDto()
        }
    }

    "it should respond properly when requesting a non-existent surgery report" {
        apiTestApplication {
            val response = client.get("/api/v1/reports/${simpleCompleteSurgeryReport.surgicalProcessID.value}")
            response shouldHaveStatus HttpStatusCode.NotFound
        }
    }

    "it should be possible to request the integration of an existent surgery report" {
        val surgeryReportAdditionalData = "Additional information to add"
        apiTestApplication {
            insertSurgeryReport()
            val response = client.patch(
                "/api/v1/reports/${simpleCompleteSurgeryReport.surgicalProcessID.value}",
            ) {
                contentType(ContentType.Application.Json)
                setBody(Json.encodeToString(SurgeryReportPatchApiDto(surgeryReportAdditionalData)))
            }
            response shouldHaveStatus HttpStatusCode.NoContent
            Json.decodeFromString<SurgeryReportApiDto>(
                client.get("/api/v1/reports/${simpleCompleteSurgeryReport.surgicalProcessID.value}")
                    .bodyAsText(),
            ).additionalData shouldBe surgeryReportAdditionalData
        }
    }

    "it should respond properly when requesting the integration of a non-existent surgery report" {
        val surgeryReportAdditionalData = "Additional information to add"
        apiTestApplication {
            val response = client.patch(
                "/api/v1/reports/${simpleCompleteSurgeryReport.surgicalProcessID.value}",
            ) {
                contentType(ContentType.Application.Json)
                setBody(Json.encodeToString(SurgeryReportPatchApiDto(surgeryReportAdditionalData)))
            }
            response shouldHaveStatus HttpStatusCode.NotFound
        }
    }

    "it should respond properly when trying to integrate a surgery report providing data not in the right format" {
        val surgeryReportAdditionalData = """{ "data" : "additional data" }"""
        apiTestApplication {
            val response = client.patch(
                "/api/v1/reports/${simpleCompleteSurgeryReport.surgicalProcessID.value}",
            ) {
                contentType(ContentType.Application.Json)
                setBody(surgeryReportAdditionalData)
            }
            response shouldHaveStatus HttpStatusCode.BadRequest
        }
    }
})
