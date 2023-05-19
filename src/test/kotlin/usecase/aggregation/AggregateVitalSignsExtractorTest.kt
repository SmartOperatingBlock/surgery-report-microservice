/*
 * Copyright (c) 2023. Smart Operating Block
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package usecase.aggregation

import data.SurgicalProcessData.listOfPatientVitalSigns
import entity.healthcareuser.PatientVitalSigns
import entity.healthcareuser.VitalSign
import entity.measurements.AggregateData
import entity.measurements.Percentage
import entity.measurements.Temperature
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.doubles.plusOrMinus
import io.kotest.matchers.shouldBe

class AggregateVitalSignsExtractorTest : StringSpec({
    val average = PatientVitalSigns(
        VitalSign.HeartBeat(70),
        VitalSign.DiastolicBloodPressure(121),
        VitalSign.SystolicBloodPressure(89),
        VitalSign.RespiratoryRate(30),
        VitalSign.SaturationPercentage(Percentage((61.7))),
        VitalSign.BodyTemperature(Temperature(36.0)),
    )

    val maximum = PatientVitalSigns(
        VitalSign.HeartBeat(100),
        VitalSign.DiastolicBloodPressure(150),
        VitalSign.SystolicBloodPressure(100),
        VitalSign.RespiratoryRate(40),
        VitalSign.SaturationPercentage(Percentage((85.0))),
        VitalSign.BodyTemperature(Temperature(37.0)),
    )

    val minimum = PatientVitalSigns(
        VitalSign.HeartBeat(50),
        VitalSign.DiastolicBloodPressure(100),
        VitalSign.SystolicBloodPressure(80),
        VitalSign.RespiratoryRate(22),
        VitalSign.SaturationPercentage(Percentage((40.0))),
        VitalSign.BodyTemperature(Temperature(35.0)),
    )

    val standardDeviation = PatientVitalSigns(
        VitalSign.HeartBeat(21),
        VitalSign.DiastolicBloodPressure(20),
        VitalSign.SystolicBloodPressure(8),
        VitalSign.RespiratoryRate(7),
        VitalSign.SaturationPercentage(Percentage((18.4))),
        VitalSign.BodyTemperature(Temperature(0.81)),
    )

    val aggregateData = AggregateVitalSignsExtractor(listOfPatientVitalSigns).aggregate()
    val tolerance = 0.1

    "It should be able to extract average data of a collection of patient vital signs" {
        aggregateData.average.heartBeat shouldBe average.heartBeat
        aggregateData.average.diastolicBloodPressure shouldBe average.diastolicBloodPressure
        aggregateData.average.systolicBloodPressure shouldBe average.systolicBloodPressure
        aggregateData.average.respiratoryRate shouldBe average.respiratoryRate
        aggregateData.average.saturationPercentage?.percentage?.value shouldBe
            (average.saturationPercentage?.percentage?.value?.plusOrMinus(tolerance))
    }

    "It should be able to extract standard deviation from a collection of patient vital signs" {
        aggregateData.std.heartBeat shouldBe standardDeviation.heartBeat
        aggregateData.std.diastolicBloodPressure shouldBe standardDeviation.diastolicBloodPressure
        aggregateData.std.systolicBloodPressure shouldBe standardDeviation.systolicBloodPressure
        aggregateData.std.respiratoryRate shouldBe standardDeviation.respiratoryRate
        aggregateData.std.saturationPercentage?.percentage?.value shouldBe
            (standardDeviation.saturationPercentage?.percentage?.value?.plusOrMinus(tolerance))
    }

    "It should be able to extract maximum values from a collection of patient vital signs" {
        aggregateData.maximum shouldBe maximum
    }

    "It should be able to extract minimum values from a collection of patient vital signs" {
        aggregateData.minimum shouldBe minimum
    }

    "If the list is empty the extractor should work and return an empty instance of data" {
        AggregateVitalSignsExtractor(listOf()).aggregate() shouldBe AggregateData(
            PatientVitalSigns(),
            PatientVitalSigns(),
            PatientVitalSigns(),
            PatientVitalSigns(),
        )
    }
})
