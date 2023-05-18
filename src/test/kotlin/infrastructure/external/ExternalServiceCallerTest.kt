/*
 * Copyright (c) 2023. Smart Operating Block
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package infrastructure.external

import entity.healthcareuser.HealthcareUser
import entity.healthcareuser.TaxCode
import entity.healthprofessional.HealthProfessionalID
import entity.measurements.Humidity
import entity.measurements.Luminosity
import entity.measurements.Percentage
import entity.measurements.Presence
import entity.measurements.Temperature
import entity.room.RoomEnvironmentalData
import entity.room.RoomID
import entity.tracking.TrackType
import entity.tracking.TrackingInfo
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.utils.io.ByteReadChannel
import java.time.Instant

class ExternalServiceCallerTest : StringSpec({
    val notExistentTaxCode = TaxCode("taxcode1")
    val notExistentRoom = RoomID("notExistentRoom")
    val healthcareUserResult = HealthcareUser(TaxCode("RSSMRA98E18F205J"), "Mario", "Rossi")
    val fromDateTime = Instant.parse("2023-03-14T22:00:00.00Z")
    val toDateTime = Instant.parse("2023-03-14T23:00:00.00Z")
    val roomTrackingDataResult = listOf(
        TrackingInfo(
            Instant.parse("2023-03-14T22:39:58.295Z"),
            HealthProfessionalID("12345678"),
            RoomID("room"),
            TrackType.ENTER,
        ),
        TrackingInfo(
            Instant.parse("2023-03-14T22:41:20.136Z"),
            HealthProfessionalID("12345678"),
            RoomID("room"),
            TrackType.EXIT,
        ),
    )
    val roomEnvironmentalDataResult = listOf(
        Instant.parse("2023-03-14T22:39:58.295Z") to RoomEnvironmentalData(
            Temperature(55.5),
            Humidity(Percentage(34.2)),
            presence = Presence(false),
        ),
        Instant.parse("2023-03-14T22:41:20.136Z") to RoomEnvironmentalData(
            Temperature(55.7),
            Humidity(Percentage(36.2)),
            Luminosity(150.0),
            Presence(true),
        ),
    )

    "it should be able to handle the absence of the requested healthcare user" {
        val externalServiceCaller = ExternalServiceCaller(
            MockEngine { respond(content = "", status = HttpStatusCode.NotFound) },
            avoidCheckOnMicroserviceUrl = true,
        )
        externalServiceCaller.getHealthcareUser(notExistentTaxCode) shouldBe null
    }

    "it should be able to obtain an healthcare user from its tax code" {
        val externalServiceCaller = ExternalServiceCaller(
            MockEngine {
                respond(
                    content = ByteReadChannel(ApiResultRawData.healthcareUserResultRawData),
                    status = HttpStatusCode.OK,
                    headers = headersOf(HttpHeaders.ContentType, "application/json"),
                )
            },
            avoidCheckOnMicroserviceUrl = true,
        )
        externalServiceCaller.getHealthcareUser(healthcareUserResult.taxCode) shouldBe healthcareUserResult
    }

    "it should be able to handle the absence of tracking data about a room" {
        val externalServiceCaller = ExternalServiceCaller(
            MockEngine { respond(content = "", status = HttpStatusCode.NoContent) },
            avoidCheckOnMicroserviceUrl = true,
        )
        externalServiceCaller.getHealthProfessionalTrackingInfo(
            notExistentRoom,
            Instant.now(),
            Instant.now(),
        ) shouldBe listOf()
    }

    "it should be able to obtain the tracking data from a room where information is present" {
        val externalServiceCaller = ExternalServiceCaller(
            MockEngine {
                respond(
                    content = ByteReadChannel(ApiResultRawData.roomTrackingResultRawData),
                    status = HttpStatusCode.OK,
                    headers = headersOf(HttpHeaders.ContentType, "application/json"),
                )
            },
            avoidCheckOnMicroserviceUrl = true,
        )
        externalServiceCaller.getHealthProfessionalTrackingInfo(
            RoomID("room"),
            fromDateTime,
            toDateTime,
        ) shouldBe roomTrackingDataResult
    }

    "it should be able to handle the absence of a room for which request historical data" {
        val externalServiceCaller = ExternalServiceCaller(
            MockEngine { respond(content = "", status = HttpStatusCode.NotFound) },
            avoidCheckOnMicroserviceUrl = true,
        )
        externalServiceCaller.getRoomEnvironmentalData(notExistentRoom, Instant.now(), Instant.now()) shouldBe listOf()
    }

    "it should be able to handle historical data about a room environmental data" {
        val externalServiceCaller = ExternalServiceCaller(
            MockEngine {
                respond(
                    content = ByteReadChannel(ApiResultRawData.roomEnvironmentalResultRawData),
                    status = HttpStatusCode.OK,
                    headers = headersOf(HttpHeaders.ContentType, "application/json"),
                )
            },
            avoidCheckOnMicroserviceUrl = true,
        )
        externalServiceCaller.getRoomEnvironmentalData(
            RoomID("room"),
            fromDateTime,
            toDateTime,
        ) shouldBe roomEnvironmentalDataResult
    }
})
