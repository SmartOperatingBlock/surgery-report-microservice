/*
 * Copyright (c) 2023. Smart Operating Block
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package entity.room

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe

class RoomTest : StringSpec({
    val room = Room(RoomID("r1"), RoomType.OPERATING_ROOM)
    val roomUpdated = Room(RoomID("r1"), RoomType.PRE_OPERATING_ROOM)
    val differentRoom = Room(RoomID("r2"), RoomType.OPERATING_ROOM)

    "room id should not be empty" {
        shouldThrow<IllegalArgumentException> { RoomID("") }
    }

    listOf(
        differentRoom,
        null,
        4,
    ).forEach {
        "a room should not be equal to other rooms with different id, other classes or null" {
            room shouldNotBe it
        }
    }

    "two rooms are equal only based on their id" {
        room shouldBe roomUpdated
    }

    "two equal rooms should have the same hashcode" {
        room.hashCode() shouldBe roomUpdated.hashCode()
    }

    "two different rooms should not have the same hashcode" {
        room.hashCode() shouldNotBe differentRoom.hashCode()
    }
})
