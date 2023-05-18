/*
 * Copyright (c) 2023. Smart Operating Block
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package application.presenter.external.model

import kotlinx.serialization.Serializable

/**
 * Module that wraps the REST-API template responses.
 */
object ApiResponses {
    /**
     * Class that represents an [entry] returned as response by an API.
     * It could include also the entry's [url].
     */
    @Serializable
    data class ResponseEntry<out T>(val entry: T, val url: String?)

    /**
     * Class that represents a list of [entries] returned as response to an API request.
     * As the REST API best-practise recommend it is included also the [total] number of the entries.
     */
    @Serializable
    data class ResponseEntryList<out T>(val entries: List<T>, val total: Int)
}
