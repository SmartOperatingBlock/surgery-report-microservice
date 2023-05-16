/*
 * Copyright (c) 2023. Smart Operating Block
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package application.presenter.api.serialization

import application.presenter.api.model.SurgeryReportEntry
import entity.report.SurgeryReport

/**
 * Serializers for data to return to API.
 */
object ApiSerializer {
    /**
     * Extension method to obtain the surgery report entry information.
     * @return the surgery report entry.
     */
    fun SurgeryReport.toSurgeryReportEntry(): SurgeryReportEntry = SurgeryReportEntry(
        surgicalProcessID = this.surgicalProcessID.value,
        patientId = this.patientID.value,
        patientName = this.healthcareUser?.name,
        patientSurname = this.healthcareUser?.surname,
        surgicalProcessDescription = this.surgicalProcessDescription,
        surgeryDate = this.surgeryDate.toInstant().toString(),
    )
}
