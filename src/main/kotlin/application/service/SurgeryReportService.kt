/*
 * Copyright (c) 2023. Smart Operating Block
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package application.service

import application.presenter.api.model.SurgeryReportEntry
import application.presenter.api.serialization.SurgeryReportSerializer.toSurgeryReportEntry
import entity.healthcareuser.PatientVitalSigns
import entity.medicaldevice.ImplantableMedicalDevice
import entity.medicaldevice.MedicalTechnologyUsage
import entity.process.SurgicalProcess
import entity.process.SurgicalProcessID
import entity.report.SurgeryReport
import entity.room.RoomType
import usecase.GetSurgicalProcessStartEndUseCase
import usecase.ReportGenerationUseCase
import usecase.repository.HealthProfessionalRepository
import usecase.repository.HealthcareUserRepository
import usecase.repository.RoomRepository
import usecase.repository.SurgeryReportRepository
import java.time.Instant

/**
 * Module that wraps all the services that orchestrate the application logic (not domain one) about
 * the [SurgeryReport].
 */
object SurgeryReportService {
    /**
     * Application Service that has the objective of generating and storing the [SurgeryReport] of a [surgicalProcess].
     * It needs the [patientVitalSigns], the [consumedImplantableMedicalDevices], the [medicalTechnologyUsage].
     * It reaches the goal by orchestrating the [surgeryReportRepository], the [healthProfessionalRepository],
     * the [healthcareUserRepository] and the [roomRepository].
     */
    class GenerateSurgeryReport(
        private val surgicalProcess: SurgicalProcess,
        private val patientVitalSigns: List<Pair<Instant, PatientVitalSigns>>,
        private val consumedImplantableMedicalDevices: Set<ImplantableMedicalDevice>,
        private val medicalTechnologyUsage: Set<MedicalTechnologyUsage>,
        private val surgeryReportRepository: SurgeryReportRepository,
        private val healthProfessionalRepository: HealthProfessionalRepository,
        private val roomRepository: RoomRepository,
        private val healthcareUserRepository: HealthcareUserRepository,
    ) : ApplicationService<SurgeryReport?> {
        override fun execute(): SurgeryReport? =
            with(GetSurgicalProcessStartEndUseCase(this.surgicalProcess).execute()) {
                val surgeryReport = ReportGenerationUseCase(
                    surgicalProcess,
                    patientVitalSigns,
                    listOf(surgicalProcess.preOperatingRoom, surgicalProcess.operatingRoom).flatMap {
                        healthProfessionalRepository.getHealthProfessionalTrackingInfo(it, this.first, this.second)
                    },
                    listOf(
                        surgicalProcess.preOperatingRoom to RoomType.PRE_OPERATING_ROOM,
                        surgicalProcess.operatingRoom to RoomType.OPERATING_ROOM,
                    ).associate {
                        Pair(it.second, roomRepository.getRoomEnvironmentalData(it.first, this.first, this.second))
                    },
                    surgicalProcess.taxCode?.let { healthcareUserRepository.getHealthcareUser(it) },
                    consumedImplantableMedicalDevices,
                    medicalTechnologyUsage,
                ).execute()
                if (surgeryReportRepository.createSurgeryReport(surgeryReport)) surgeryReport else null
            }
    }

    /**
     * Application Service that has the objective of getting the [SurgeryReport] associated to a
     * [surgicalProcessID] using the provided [surgeryReportRepository].
     */
    class GetSurgeryReport(
        private val surgicalProcessID: SurgicalProcessID,
        private val surgeryReportRepository: SurgeryReportRepository,
    ) : ApplicationService<SurgeryReport?> {
        override fun execute(): SurgeryReport? = this.surgeryReportRepository.findBy(this.surgicalProcessID)
    }

    /**
     * Application Service that has the objective of integrate a [SurgeryReport] identified by its [surgicalProcessID]
     * with [additionalInformation] using the provided [surgeryReportRepository].
     * It returns true if the integration is successful, false otherwise.
     */
    class IntegrateSurgeryReport(
        private val surgicalProcessID: SurgicalProcessID,
        private val additionalInformation: String,
        private val surgeryReportRepository: SurgeryReportRepository,
    ) : ApplicationService<Boolean> {
        override fun execute(): Boolean =
            this.surgeryReportRepository.integrateSurgeryReport(this.surgicalProcessID, this.additionalInformation)
    }

    /**
     * Application Service that has the objective to obtain all the surgery report that have been generated in the form
     * of [SurgeryReportEntry]. It will be performed using the provided [surgeryReportRepository].
     */
    class GetAllSurgeryReportEntry(
        private val surgeryReportRepository: SurgeryReportRepository,
    ) : ApplicationService<List<SurgeryReportEntry>> {
        override fun execute(): List<SurgeryReportEntry> =
            this.surgeryReportRepository.getSurgeryReports().map { it.toSurgeryReportEntry() }
    }
}
