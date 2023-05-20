/*
 * Copyright (c) 2023. Smart Operating Block
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package infrastructure.external.testdouble

import data.SurgicalProcessData
import entity.healthcareuser.HealthcareUser
import usecase.repository.HealthProfessionalRepository
import usecase.repository.HealthcareUserRepository
import usecase.repository.RoomRepository

/**
 * Some repository test doubles that can be used in tests.
 */
object RepositoryTestDoubles {
    val healthProfessionalRepository = HealthProfessionalRepository { _, _, _ ->
        SurgicalProcessData.listOfhealthProfessionalTrackingData
    }
    val roomRepository = RoomRepository { _, _, _ -> SurgicalProcessData.listOfTimedRoomEnvironmentalData }
    val healthcareUserRepository = HealthcareUserRepository { _ ->
        SurgicalProcessData.simpleSurgicalProcess.taxCode?.let { HealthcareUser(it, "Mario", "Rossi") }
    }
}
