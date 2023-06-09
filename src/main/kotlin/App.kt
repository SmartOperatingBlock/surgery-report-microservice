import infrastructure.api.APIController
import infrastructure.database.SurgeryReportDatabase
import infrastructure.events.KafkaClient
import infrastructure.external.ExternalServiceCaller

/*
 * Copyright (c) 2023. Smart Operating Block
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

/**
 * Template for kotlin projects.
 */
fun main() {
    val surgeryReportRepository = SurgeryReportDatabase()
    val externalServiceCaller = ExternalServiceCaller()
    APIController(surgeryReportRepository).start()
    KafkaClient(
        surgeryReportRepository,
        externalServiceCaller,
        externalServiceCaller,
        externalServiceCaller,
    ).start()
}
