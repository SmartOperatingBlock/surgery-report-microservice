/*
 * Copyright (c) 2023. Smart Operating Block
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package entity.medicaldevice

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe

class ImplantableMedicalDeviceTest : StringSpec({
    val implantableMedicalDevice = ImplantableMedicalDevice(
        ImplantableMedicalDeviceID("imd1"),
        ImplantableMedicalDeviceType.PACEMAKER,
    )
    val implantableMedicalDeviceUpdated = ImplantableMedicalDevice(
        ImplantableMedicalDeviceID("imd1"),
        ImplantableMedicalDeviceType.CATHETER,
    )
    val differentImplantableMedicalDevice = ImplantableMedicalDevice(
        ImplantableMedicalDeviceID("imd2"),
        ImplantableMedicalDeviceType.PACEMAKER,
    )

    "implantable medical device id should not be empty" {
        shouldThrow<IllegalArgumentException> { ImplantableMedicalDeviceID("") }
    }

    listOf(
        differentImplantableMedicalDevice,
        null,
        4,
    ).forEach {
        "a implantable medical device should not be equal to other devices with different id, other classes or null" {
            implantableMedicalDevice shouldNotBe it
        }
    }

    "two implantable medical devices are equal only based on their id" {
        implantableMedicalDevice shouldBe implantableMedicalDeviceUpdated
    }

    "two equal implantable medical devices should have the same hashcode" {
        implantableMedicalDevice.hashCode() shouldBe implantableMedicalDeviceUpdated.hashCode()
    }

    "two different implantable medical devices should not have the same hashcode" {
        implantableMedicalDevice.hashCode() shouldNotBe differentImplantableMedicalDevice.hashCode()
    }
})
