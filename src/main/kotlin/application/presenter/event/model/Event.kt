/*
 * Copyright (c) 2023. Smart Operating Block
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package application.presenter.event.model

/**
 * Interface that models a generic event with no additional fields but only with a [key],
 * a payload data of type [E] and the [dateTime] of the event itself.
 */
interface Event<out E> {
    /** The key of the event. */
    val key: String

    /** The data payload of the event. */
    val data: E

    /** The date time of when the event happened. */
    val dateTime: String
}
