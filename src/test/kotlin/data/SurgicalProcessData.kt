/*
 * Copyright (c) 2023. Smart Operating Block
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package data

import entity.healthcareuser.PatientID
import entity.healthcareuser.PatientVitalSigns
import entity.healthcareuser.VitalSign
import entity.healthprofessional.HealthProfessionalID
import entity.measurements.Humidity
import entity.measurements.Luminosity
import entity.measurements.Percentage
import entity.measurements.Presence
import entity.measurements.Temperature
import entity.medicaldevice.ImplantableMedicalDevice
import entity.medicaldevice.ImplantableMedicalDeviceID
import entity.medicaldevice.ImplantableMedicalDeviceType
import entity.medicaldevice.MedicalTechnology
import entity.medicaldevice.MedicalTechnologyID
import entity.medicaldevice.MedicalTechnologyType
import entity.medicaldevice.MedicalTechnologyUsage
import entity.process.SurgicalProcess
import entity.process.SurgicalProcessID
import entity.process.SurgicalProcessState
import entity.process.SurgicalProcessStep
import entity.room.RoomEnvironmentalData
import entity.room.RoomID
import entity.tracking.TrackType
import entity.tracking.TrackingInfo
import java.time.Instant

/**
 * Module that wraps some data about simple surgical processes.
 */
object SurgicalProcessData {
    val listOfTimedPatientVitalSigns = listOf(
        Instant.parse("2020-10-03T08:10:50Z") to PatientVitalSigns(
            VitalSign.HeartBeat(60),
            VitalSign.DiastolicBloodPressure(100),
            VitalSign.SystolicBloodPressure(80),
            VitalSign.RespiratoryRate(30),
            VitalSign.SaturationPercentage(Percentage(85.0)),
            VitalSign.BodyTemperature(Temperature(36.0)),
        ),
        Instant.parse("2020-10-03T08:17:55Z") to PatientVitalSigns(
            VitalSign.HeartBeat(50),
            VitalSign.DiastolicBloodPressure(150),
            VitalSign.SystolicBloodPressure(100),
            VitalSign.RespiratoryRate(40),
            VitalSign.SaturationPercentage(Percentage(40.0)),
            VitalSign.BodyTemperature(Temperature(37.0)),
        ),
        Instant.parse("2020-10-03T08:19:00Z") to PatientVitalSigns(
            VitalSign.HeartBeat(100),
            VitalSign.DiastolicBloodPressure(115),
            VitalSign.SystolicBloodPressure(87),
            VitalSign.RespiratoryRate(22),
            VitalSign.SaturationPercentage(Percentage(60.0)),
            VitalSign.BodyTemperature(Temperature(35.0)),
        ),
    )

    val listOfPatientVitalSigns = listOfTimedPatientVitalSigns.map { it.second }

    val listOfTimedRoomEnvironmentalData = listOf(
        Instant.parse("2020-10-03T08:11:50Z") to RoomEnvironmentalData(
            Temperature(30.0),
            Humidity(Percentage(50.0)),
            Luminosity(100.0),
            Presence(false),
        ),
        Instant.parse("2020-10-03T08:16:55Z") to RoomEnvironmentalData(
            Temperature(35.0),
            Humidity(Percentage(55.0)),
            Luminosity(150.0),
            Presence(true),
        ),
        Instant.parse("2020-10-03T08:25:00Z") to RoomEnvironmentalData(
            Temperature(32.0),
            Humidity(Percentage(80.0)),
            Luminosity(300.0),
            Presence(true),
        ),
    )

    val listOfRoomEnvironmentalData = listOfTimedRoomEnvironmentalData.map { it.second }

    val simpleSurgicalProcess = SurgicalProcess(
        SurgicalProcessID("process"),
        "description",
        PatientID("patient"),
        RoomID("preOperatingRoom"),
        RoomID("operatingRoom"),
        HealthProfessionalID("health professional"),
        taxCode = null,
        listOf(
            Pair(
                Instant.parse("2020-10-03T08:10:00Z"),
                SurgicalProcessState.PreSurgery(SurgicalProcessStep.PATIENT_IN_PREPARATION),
            ),
            Pair(
                Instant.parse("2020-10-03T08:12:00Z"),
                SurgicalProcessState.Surgery(SurgicalProcessStep.PATIENT_ON_OPERATING_TABLE),
            ),
            Pair(
                Instant.parse("2020-10-03T08:15:00Z"),
                SurgicalProcessState.Surgery(SurgicalProcessStep.SURGERY_IN_PROGRESS),
            ),
            Pair(
                Instant.parse("2020-10-03T08:18:00Z"),
                SurgicalProcessState.Surgery(SurgicalProcessStep.END_OF_SURGERY),
            ),
            Pair(
                Instant.parse("2020-10-03T08:20:00Z"),
                SurgicalProcessState.PostSurgery(SurgicalProcessStep.PATIENT_UNDER_OBSERVATION),
            ),
            Pair(
                Instant.parse("2020-10-03T09:00:00Z"),
                SurgicalProcessState.Terminated(),
            ),
        ),
    )

    val simpleSurgicalProcessInterrupted = simpleSurgicalProcess.copy(
        processStates = listOf(
            Pair(
                Instant.parse("2020-10-03T08:10:00Z"),
                SurgicalProcessState.PreSurgery(SurgicalProcessStep.PATIENT_IN_PREPARATION),
            ),
            Pair(
                Instant.parse("2020-10-03T08:25:00Z"),
                SurgicalProcessState.Interrupted(),
            ),
        ),
    )

    val sampleConsumedImplantableMedicalDevices = setOf(
        ImplantableMedicalDevice(
            ImplantableMedicalDeviceID("implantable1"),
            ImplantableMedicalDeviceType.PACEMAKER,
        ),
        ImplantableMedicalDevice(
            ImplantableMedicalDeviceID("implantable2"),
            ImplantableMedicalDeviceType.CATHETER,
        ),
    )

    val sampleMedicalTechnologyUsage = setOf(
        MedicalTechnologyUsage(
            Instant.parse("2020-10-03T08:16:45Z"),
            MedicalTechnology(
                MedicalTechnologyID("tech1"),
                "Medical Technology 1",
                "Description of medical technology",
                MedicalTechnologyType.ENDOSCOPE,
                inUse = true,
            ),
        ),
        MedicalTechnologyUsage(
            Instant.parse("2020-10-03T08:17:45Z"),
            MedicalTechnology(
                MedicalTechnologyID("tech1"),
                "Medical Technology 1",
                "Description of medical technology",
                MedicalTechnologyType.ENDOSCOPE,
                inUse = false,
            ),
        ),
        MedicalTechnologyUsage(
            Instant.parse("2020-10-03T08:17:45Z"),
            MedicalTechnology(
                MedicalTechnologyID("tech2"),
                "Medical Technology 2",
                "Description of medical technology",
                MedicalTechnologyType.XRAY,
                inUse = true,
            ),
        ),
        MedicalTechnologyUsage(
            Instant.parse("2020-10-03T08:18:00Z"),
            MedicalTechnology(
                MedicalTechnologyID("tech2"),
                "Medical Technology 2",
                "Description of medical technology",
                MedicalTechnologyType.XRAY,
                inUse = false,
            ),
        ),
    )

    val listOfhealthProfessionalTrackingData = listOf(
        TrackingInfo(
            Instant.parse("2020-10-03T08:10:00Z"),
            HealthProfessionalID("hp0"),
            RoomID("preOperatingRoom"),
            TrackType.ENTER,
        ),
        TrackingInfo(
            Instant.parse("2020-10-03T08:10:00Z"),
            HealthProfessionalID("hp1"),
            RoomID("operatingRoom"),
            TrackType.ENTER,
        ),
        TrackingInfo(
            Instant.parse("2020-10-03T08:15:00Z"),
            HealthProfessionalID("hp2"),
            RoomID("operatingRoom"),
            TrackType.ENTER,
        ),
        TrackingInfo(
            Instant.parse("2020-10-03T08:18:00Z"),
            HealthProfessionalID("hp2"),
            RoomID("operatingRoom"),
            TrackType.EXIT,
        ),
    )
}
