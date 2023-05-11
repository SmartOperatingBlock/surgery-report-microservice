/*
 * Copyright (c) 2023. Smart Operating Block
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package usecase.repository

import entity.process.SurgicalProcessID
import entity.report.SurgeryReport

/**
 * Interface that models the repository to manage surgery reports.
 */
interface SurgeryReportRepository {
    /**
     * Creates a [SurgeryReport].
     * Return false if the surgery report already exists, true otherwise.
     */
    fun createSurgeryReport(surgeryReport: SurgeryReport): Boolean

    /**
     * Integrate a Surgery report with additional information.
     * The surgery report to integrate with the [informationToAdd] is identified by the described [surgeryProcessID].
     */
    fun integrateSurgeryReport(surgeryProcessID: SurgicalProcessID, informationToAdd: String): Boolean

    /**
     * Get a surgery report that describe a surgical process identified by its [surgeryProcessID].
     * The method return the surgery report if available, otherwise null.
     */
    fun getSurgeryReport(surgeryProcessID: SurgicalProcessID): SurgeryReport?
}
