/*
 * Copyright (c) 2023. Smart Operating Block
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package usecase

import entity.healthcareuser.PatientID
import entity.healthcareuser.PatientVitalSigns
import entity.healthcareuser.VitalSign
import entity.healthprofessional.HealthProfessionalID
import entity.measurements.Humidity
import entity.measurements.Luminosity
import entity.measurements.Percentage
import entity.measurements.Presence
import entity.measurements.Temperature
import entity.process.SurgicalProcess
import entity.process.SurgicalProcessID
import entity.process.SurgicalProcessState
import entity.process.SurgicalProcessStep
import entity.room.RoomEnvironmentalData
import entity.room.RoomID
import java.time.Instant

/**
 * Module that wraps some data about simple surgical processes.
 */
object SurgicalProcessData {
    val listOfPatientVitalSigns = listOf(
        PatientVitalSigns(
            VitalSign.HeartBeat(60),
            VitalSign.DiastolicBloodPressure(100),
            VitalSign.SystolicBloodPressure(80),
            VitalSign.RespiratoryRate(30),
            VitalSign.SaturationPercentage(Percentage(85.0)),
            VitalSign.BodyTemperature(Temperature(36.0)),
        ),
        PatientVitalSigns(
            VitalSign.HeartBeat(50),
            VitalSign.DiastolicBloodPressure(150),
            VitalSign.SystolicBloodPressure(100),
            VitalSign.RespiratoryRate(40),
            VitalSign.SaturationPercentage(Percentage(40.0)),
            VitalSign.BodyTemperature(Temperature(37.0)),
        ),
        PatientVitalSigns(
            VitalSign.HeartBeat(100),
            VitalSign.DiastolicBloodPressure(115),
            VitalSign.SystolicBloodPressure(87),
            VitalSign.RespiratoryRate(22),
            VitalSign.SaturationPercentage(Percentage(60.0)),
            VitalSign.BodyTemperature(Temperature(35.0)),
        ),
    )

    val listOfRoomEnvironmentalData = listOf(
        RoomEnvironmentalData(
            Temperature(30.0),
            Humidity(Percentage(50.0)),
            Luminosity(100.0),
            Presence(false),
        ),
        RoomEnvironmentalData(
            Temperature(35.0),
            Humidity(Percentage(55.0)),
            Luminosity(150.0),
            Presence(true),
        ),
        RoomEnvironmentalData(
            Temperature(32.0),
            Humidity(Percentage(80.0)),
            Luminosity(300.0),
            Presence(true),
        ),
    )

    val simpleSurgicalProcess = SurgicalProcess(
        SurgicalProcessID("process"),
        "description",
        PatientID("patient"),
        HealthProfessionalID("health professional"),
        RoomID("room1"),
        RoomID("room2"),
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
}
