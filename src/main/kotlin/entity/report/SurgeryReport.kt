/*
 * Copyright (c) 2023. Smart Operating Block
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package entity.report

import entity.healthcareuser.HealthcareUser
import entity.healthcareuser.PatientID
import entity.healthcareuser.PatientVitalSigns
import entity.healthprofessional.HealthProfessionalID
import entity.measurements.AggregateTimedData
import entity.medicaldevice.ImplantableMedicalDevice
import entity.medicaldevice.MedicalTechnologyUsage
import entity.process.SurgicalProcessID
import entity.process.SurgicalProcessState
import entity.room.RoomEnvironmentalData
import entity.room.RoomType
import entity.tracking.TrackingInfo
import java.time.Instant

/**
 * It describes the surgery report, so the final product to be delivered to the user.
 * The information that are reported in the [SurgeryReport] are:
 * - [surgicalProcessID] the id of the process described in the report. It identifies the report.
 * - [surgicalProcessDescription] the description of the surgery.
 * - [inChargeHealthProfessional] the health professional that is in charge of the surgery.
 * - [patientID] the identification of the patient.
 * - [healthcareUser] if available, the information about the associated healthcare user.
 * - [statesData] aggregate data about the various steps in the surgery.
 * - [consumedImplantableMedicalDevices] the set of implantable medical device consumed during the surgery process.
 * - [medicalTechnologyUsageData] the usage of medical technology during the surgical process.
 * - [healthProfessionalTrackingInformation] the tracking information about the health professionals involved.
 * - [additionalData] additional data provided manually by the health professionals.
 */
data class SurgeryReport(
    val surgicalProcessID: SurgicalProcessID,
    val surgicalProcessDescription: String,
    val inChargeHealthProfessional: HealthProfessionalID,
    val patientID: PatientID,
    val healthcareUser: HealthcareUser?,
    val statesData: Map<SurgicalProcessState, SurgeryProcessStepAggregateData>,
    val consumedImplantableMedicalDevices: Set<ImplantableMedicalDevice>,
    val medicalTechnologyUsageData: Set<MedicalTechnologyUsage>,
    val healthProfessionalTrackingInformation: List<TrackingInfo<HealthProfessionalID>>,
    val additionalData: String,
) {
    override fun equals(other: Any?): Boolean = when {
        other === this -> true
        other is SurgeryReport -> this.surgicalProcessID == other.surgicalProcessID
        else -> false
    }

    override fun hashCode(): Int = this.surgicalProcessID.hashCode()
}

/**
 * Aggregate data about each step of the Surgical process described in [SurgeryReport].
 * It contains the [startDateTime] of the step, and the [patientVitalSignsAggregateData]
 * and the [environmentalAggregateData].
 */
data class SurgeryProcessStepAggregateData(
    val startDateTime: Instant,
    val patientVitalSignsAggregateData: AggregateTimedData<PatientVitalSigns>,
    val environmentalAggregateData: Map<RoomType, AggregateTimedData<RoomEnvironmentalData>>,
)
