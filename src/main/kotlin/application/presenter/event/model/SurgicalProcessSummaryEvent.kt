/*
 * Copyright (c) 2023. Smart Operating Block
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package application.presenter.event.model

import kotlinx.serialization.Serializable

/**
 * Surgical process summary event.
 */
@Serializable
data class SurgicalProcessSummaryEvent(
    override val key: String,
    override val data: SurgicalProcessSummaryPayload,
    override val dateTime: String,
) : Event<SurgicalProcessSummaryPayload> {
    companion object {
        /** Surgical Process summary event key. */
        const val SURGICAL_PROCESS_SUMMARY_EVENT_KEY = "SURGERY_REPORT_EVENT"
    }
}
