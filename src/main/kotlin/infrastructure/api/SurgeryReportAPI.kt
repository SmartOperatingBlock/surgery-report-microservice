/*
 * Copyright (c) 2023. Smart Operating Block
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package infrastructure.api

import application.presenter.api.model.SurgeryReportPatchApiDto
import application.presenter.api.model.apiresponse.ApiResponses
import application.presenter.api.serialization.SurgeryReportSerializer.toApiDto
import application.service.SurgeryReportService
import entity.process.SurgicalProcessID
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.patch
import io.ktor.server.routing.routing
import usecase.repository.SurgeryReportRepository

/**
 * The definition of the Surgery Report API.
 * @param[apiPath] it represents the path to reach the api.
 * @param[port] the port where the api are exposed.
 * @param[surgeryReportRepository] the surgery report repository.
 */
fun Application.surgeryReportAPI(apiPath: String, port: Int, surgeryReportRepository: SurgeryReportRepository) {
    routing {
        getAllSurgeryReportEntriesRoute(apiPath, port, surgeryReportRepository)
        getSurgeryReportRoute(apiPath, surgeryReportRepository)
        integrateSurgeryReportRoute(apiPath, surgeryReportRepository)
    }
}

private fun Route.getAllSurgeryReportEntriesRoute(
    apiPath: String,
    port: Int,
    surgeryReportRepository: SurgeryReportRepository,
) = get("$apiPath/reports") {
    val entries = SurgeryReportService.GetAllSurgeryReportEntry(surgeryReportRepository)
        .execute()
        .map { ApiResponses.ResponseEntry(it, "http://localhost:$port$apiPath/reports/${it.surgicalProcessID}") }
    call.response.status(if (entries.isNotEmpty()) HttpStatusCode.OK else HttpStatusCode.NoContent)
    call.respond(ApiResponses.ResponseEntryList(entries, entries.size))
}

private fun Route.getSurgeryReportRoute(apiPath: String, surgeryReportRepository: SurgeryReportRepository) =
    get("$apiPath/reports/{surgicalProcessId}") {
        SurgeryReportService.GetSurgeryReport(
            SurgicalProcessID(call.parameters["surgicalProcessId"].orEmpty()),
            surgeryReportRepository,
        ).execute().apply {
            when (this) {
                null -> call.respond(HttpStatusCode.NotFound)
                else -> call.respond(this.toApiDto())
            }
        }
    }

private fun Route.integrateSurgeryReportRoute(apiPath: String, surgeryReportRepository: SurgeryReportRepository) =
    patch("$apiPath/reports/{surgicalProcessId}") {
        call.respond(
            SurgeryReportService.IntegrateSurgeryReport(
                SurgicalProcessID(call.parameters["surgicalProcessId"].orEmpty()),
                call.receive<SurgeryReportPatchApiDto>().additionalData,
                surgeryReportRepository,
            ).execute().let { result ->
                if (result) HttpStatusCode.NoContent else HttpStatusCode.NotFound
            },
        )
    }
