/*
 * Copyright (c) 2023. Smart Operating Block
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package infrastructure.api

import application.presenter.api.model.apiresponse.ApiResponses
import application.service.SurgeryReportService
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
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
