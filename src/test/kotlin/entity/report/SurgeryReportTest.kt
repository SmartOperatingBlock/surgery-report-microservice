/*
 * Copyright (c) 2023. Smart Operating Block
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package entity.report

import entity.healthcareuser.PatientID
import entity.healthprofessional.HealthProfessionalID
import entity.process.SurgicalProcessID
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import java.time.Instant

class SurgeryReportTest : StringSpec({
    val surgeryReport = SurgeryReport(
        SurgicalProcessID("p1"),
        Instant.now(),
        "description",
        PatientID("patient1"),
        setOf(),
        HealthProfessionalID("h1"),
        null,
        mapOf(),
        setOf(),
        setOf(),
        listOf(),
        "additional data",
    )
    val surgeryReportUpdated = surgeryReport.copy(additionalData = "modified additional data")
    val differentSurgeryReport = surgeryReport.copy(surgicalProcessID = SurgicalProcessID("p2"))

    listOf(
        differentSurgeryReport,
        null,
        4,
    ).forEach {
        "a surgery report should not be equal to other reports with different id, other classes or null" {
            surgeryReport shouldNotBe it
        }
    }

    "two surgery reports are equal only based on their id" {
        surgeryReport shouldBe surgeryReportUpdated
    }

    "two equal surgery reports should have the same hashcode" {
        surgeryReport.hashCode() shouldBe surgeryReportUpdated.hashCode()
    }

    "two different surgery reports should not have the same hashcode" {
        surgeryReport.hashCode() shouldNotBe differentSurgeryReport.hashCode()
    }
})
