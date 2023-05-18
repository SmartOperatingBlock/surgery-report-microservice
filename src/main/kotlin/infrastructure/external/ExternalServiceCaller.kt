/*
 * Copyright (c) 2023. Smart Operating Block
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package infrastructure.external

import application.presenter.external.model.ApiResponses
import application.presenter.external.model.HealthcareUserResultDto
import application.presenter.external.model.TrackingInfoResultDto
import application.presenter.external.serialization.ExternalServiceSerialization.toHealthProfessionalTrackingInfo
import application.presenter.external.serialization.ExternalServiceSerialization.toHealthcareUser
import entity.healthcareuser.HealthcareUser
import entity.healthcareuser.TaxCode
import entity.healthprofessional.HealthProfessionalID
import entity.room.RoomID
import entity.tracking.TrackingInfo
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.http.HttpStatusCode
import io.ktor.http.appendPathSegments
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.runBlocking
import usecase.repository.HealthProfessionalRepository
import usecase.repository.HealthcareUserRepository
import java.time.Instant

/**
 * The aim of this class is to provide the mean to access external microservices that have the data
 * needed by [HealthcareUserRepository].
 */
class ExternalServiceCaller : HealthcareUserRepository, HealthProfessionalRepository {
    init {
        checkNotNull(System.getenv(PATIENT_MANAGEMENT_URL)) {
            "Patient Management Integration microservice url required"
        }
        checkNotNull(System.getenv(STAFF_TRACKING_URL)) { "Staff Tracking microservice url required" }
    }

    private val httpClient = HttpClient() {
        install(ContentNegotiation) {
            json()
        }
    }

    override fun getHealthcareUser(taxCode: TaxCode): HealthcareUser? =
        runBlocking {
            httpClient.get(System.getenv(PATIENT_MANAGEMENT_URL)) {
                url {
                    appendPathSegments("patients", taxCode.value)
                }
            }.let {
                when (it.status) {
                    HttpStatusCode.NotFound, HttpStatusCode.InternalServerError -> null
                    else -> it.body<HealthcareUserResultDto>().toHealthcareUser()
                }
            }
        }

    override fun getHealthProfessionalTrackingInfo(
        roomID: RoomID,
        from: Instant,
        to: Instant,
    ): List<TrackingInfo<HealthProfessionalID>> =
        runBlocking {
            httpClient.get(System.getenv(STAFF_TRACKING_URL)) {
                url {
                    appendPathSegments("rooms-tracking-data", roomID.value)
                    parameters.append("from", from.toString())
                    parameters.append("to", to.toString())
                }
            }.let { response ->
                when (response.status) {
                    HttpStatusCode.NoContent, HttpStatusCode.InternalServerError -> listOf()
                    else ->
                        response.body<ApiResponses.ResponseEntryList<TrackingInfoResultDto>>()
                            .entries
                            .map { it.toHealthProfessionalTrackingInfo() }
                }
            }
        }

    companion object {
        private const val PATIENT_MANAGEMENT_URL = "PATIENT_MANAGEMENT_INTEGRATION_MICROSERVICE_URL"
        private const val STAFF_TRACKING_URL = "STAFF_TRACKING_MICROSERVICE_URL"
    }
}
