/*
 * Copyright (c) 2023. Smart Operating Block
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package application.handler

import application.presenter.event.model.Event

/**
 * Interface that models a handler of [Event] that comes from the external.
 */
interface EventHandler {
    /**
     * Understand if the handler can handle this [event].
     */
    fun canHandle(event: Event<*>): Boolean

    /**
     * Consume an [event].
     */
    fun consume(event: Event<*>)
}
