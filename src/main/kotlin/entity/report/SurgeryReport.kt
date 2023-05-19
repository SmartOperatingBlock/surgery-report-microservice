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
import entity.measurements.AggregateData
import entity.medicaldevice.ImplantableMedicalDevice
import entity.medicaldevice.MedicalTechnologyUsage
import entity.process.SurgicalProcessID
import entity.process.SurgicalProcessStep
import entity.room.Room
import entity.room.RoomEnvironmentalData
import entity.room.RoomType
import entity.tracking.TrackingInfo
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.time.Instant

/**
 * It describes the surgery report, so the final product to be delivered to the user.
 * The information that are reported in the [SurgeryReport] are:
 * - [surgicalProcessID] the id of the process described in the report. It identifies the report.
 * - [surgeryDate] the date of the surgery.
 * - [surgicalProcessDescription] the description of the surgery.
 * - [inChargeHealthProfessional] the health professional that is in charge of the surgery.
 * - [patientID] the identification of the patient.
 * - [roomsInvolved] the rooms involved in the surgical process.
 * - [healthcareUser] if available, the information about the associated healthcare user.
 * - [stepData] aggregate data about the various steps in the surgery.
 * - [consumedImplantableMedicalDevices] the set of implantable medical device consumed during the surgery process.
 * - [medicalTechnologyUsageData] the usage of medical technology during the surgical process.
 * - [healthProfessionalTrackingInformation] the tracking information about the health professionals involved.
 * - [additionalData] additional data provided manually by the health professionals.
 */
@Serializable
data class SurgeryReport(
    val surgicalProcessID: SurgicalProcessID,
    @Contextual val surgeryDate: Instant,
    val surgicalProcessDescription: String,
    val inChargeHealthProfessional: HealthProfessionalID,
    val patientID: PatientID,
    val roomsInvolved: Set<Room>,
    val healthcareUser: HealthcareUser?,
    val stepData: Map<SurgicalProcessStep, SurgeryProcessStepAggregateData>,
    val consumedImplantableMedicalDevices: Set<ImplantableMedicalDevice>,
    val medicalTechnologyUsageData: Set<MedicalTechnologyUsage>,
    val healthProfessionalTrackingInformation: List<TrackingInfo<HealthProfessionalID>>,
    val additionalData: String = "",
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
 * It contains the [startDateTime] and the [stopDateTime] of the step, the [patientVitalSignsAggregateData]
 * and the [environmentalAggregateData].
 */
@Serializable
data class SurgeryProcessStepAggregateData(
    @Contextual val startDateTime: Instant,
    @Contextual val stopDateTime: Instant?,
    val patientVitalSignsAggregateData: AggregateData<PatientVitalSigns>,
    val environmentalAggregateData: Map<RoomType, AggregateData<RoomEnvironmentalData>>,
)
