/*
 * Copyright (c) 2023. Smart Operating Block
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package infrastructure.api

import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import usecase.repository.SurgeryReportRepository

/**
 * It manages the REST-API of the microservice.
 * @param[surgeryReportRepository] the repository of surgery reports.
 */
class APIController(private val surgeryReportRepository: SurgeryReportRepository) {
    /** Starts the http server to serve the client requests. */
    fun start() {
        embeddedServer(Netty, port = PORT) {
            dispatcher(this)
            install(ContentNegotiation) {
                json()
            }
        }.start(wait = false)
    }

    private fun dispatcher(app: Application) {
        with(app) {
            surgeryReportAPI(API_PATH, PORT, this@APIController.surgeryReportRepository)
        }
    }

    companion object {
        private const val PORT = 3000
        private const val API_VERSION = "v1"
        private const val API_PATH = "/api/$API_VERSION"
    }
}
