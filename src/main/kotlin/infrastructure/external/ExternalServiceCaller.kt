/*
 * Copyright (c) 2023. Smart Operating Block
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package infrastructure.external

import application.presenter.api.model.apiresponse.ApiResponses
import application.presenter.external.model.BuildingManagementDtoModel
import application.presenter.external.model.PatientManagementIntegrationDtoModel
import application.presenter.external.model.StaffTrackingDtoModel
import application.presenter.external.serialization.ExternalServiceSerialization.toHealthProfessionalTrackingInfo
import application.presenter.external.serialization.ExternalServiceSerialization.toHealthcareUser
import application.presenter.external.serialization.ExternalServiceSerialization.toRoomEnvironmentalData
import entity.healthcareuser.HealthcareUser
import entity.healthcareuser.TaxCode
import entity.healthprofessional.HealthProfessionalID
import entity.room.RoomEnvironmentalData
import entity.room.RoomID
import entity.tracking.TrackingInfo
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.http.HttpStatusCode
import io.ktor.http.appendPathSegments
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.runBlocking
import usecase.repository.HealthProfessionalRepository
import usecase.repository.HealthcareUserRepository
import usecase.repository.RoomRepository
import java.time.Instant

/**
 * The aim of this class is to provide the mean to access external microservices that have the data
 * needed by [HealthcareUserRepository].
 */
class ExternalServiceCaller(engine: HttpClientEngine = OkHttp.create(), avoidCheckOnMicroserviceUrl: Boolean = false) :
    HealthcareUserRepository, HealthProfessionalRepository, RoomRepository {
    private val patientManagementIntegrationUrl: String
    private val staffTrackingUrl: String
    private val buildingManagement: String

    init {
        if (!avoidCheckOnMicroserviceUrl) {
            checkNotNull(System.getenv(PATIENT_MANAGEMENT_URL)) {
                "Patient Management Integration microservice url required"
            }
            checkNotNull(System.getenv(STAFF_TRACKING_URL)) { "Staff Tracking microservice url required" }
            checkNotNull(System.getenv(BUILDING_MANAGEMENT_URL)) { "Building Management microservice url required" }
        }
        patientManagementIntegrationUrl = System.getenv(PATIENT_MANAGEMENT_URL).orEmpty()
        staffTrackingUrl = System.getenv(STAFF_TRACKING_URL).orEmpty()
        buildingManagement = System.getenv(BUILDING_MANAGEMENT_URL).orEmpty()
    }

    private val httpClient = HttpClient(engine) {
        install(ContentNegotiation) {
            json()
        }
    }

    override fun getHealthcareUser(taxCode: TaxCode): HealthcareUser? = runBlocking {
        httpClient.get(this@ExternalServiceCaller.patientManagementIntegrationUrl) {
            url {
                appendPathSegments("patients", taxCode.value)
            }
        }.let {
            when (it.status) {
                HttpStatusCode.NotFound, HttpStatusCode.InternalServerError -> null
                else -> it.body<PatientManagementIntegrationDtoModel.HealthcareUserResultDto>().toHealthcareUser()
            }
        }
    }

    override fun getHealthProfessionalTrackingInfo(
        roomID: RoomID,
        from: Instant,
        to: Instant,
    ): List<TrackingInfo<HealthProfessionalID>> = runBlocking {
        httpClient.get(this@ExternalServiceCaller.staffTrackingUrl) {
            url {
                appendPathSegments("rooms-tracking-data", roomID.value)
                parameters.append("from", from.toString())
                parameters.append("to", to.toString())
            }
        }.let { response ->
            when (response.status) {
                HttpStatusCode.NoContent, HttpStatusCode.InternalServerError -> listOf()
                else ->
                    response.body<ApiResponses.ResponseEntryList<StaffTrackingDtoModel.TrackingInfoResultDto>>()
                        .entries
                        .map { it.toHealthProfessionalTrackingInfo() }
            }
        }
    }

    override fun getRoomEnvironmentalData(
        roomID: RoomID,
        from: Instant,
        to: Instant,
    ): List<Pair<Instant, RoomEnvironmentalData>> = runBlocking {
        httpClient.get(this@ExternalServiceCaller.buildingManagement) {
            url {
                appendPathSegments("rooms", "data", roomID.value)
                parameters.append("from", from.toString())
                parameters.append("to", to.toString())
            }
        }.let { response ->
            when (response.status) {
                HttpStatusCode.NotFound, HttpStatusCode.InternalServerError -> listOf()
                else ->
                    response.body<
                        ApiResponses.ResponseEntryList<
                            ApiResponses.ResponseTimedEntry<BuildingManagementDtoModel.EnvironmentalDataApiDto>,
                            >,
                        >()
                        .entries
                        .map { Instant.parse(it.date) to it.entry.toRoomEnvironmentalData() }
            }
        }
    }

    companion object {
        private const val PATIENT_MANAGEMENT_URL = "PATIENT_MANAGEMENT_INTEGRATION_MICROSERVICE_URL"
        private const val STAFF_TRACKING_URL = "STAFF_TRACKING_MICROSERVICE_URL"
        private const val BUILDING_MANAGEMENT_URL = "BUILDING_MANAGEMENT_MICROSERVICE_URL"
    }
}
