/*
 * Copyright (c) 2023. Smart Operating Block
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package infrastructure.database

import com.mongodb.MongoException
import com.mongodb.client.MongoCollection
import entity.process.SurgicalProcessID
import entity.report.SurgeryReport
import org.litote.kmongo.KMongo
import org.litote.kmongo.eq
import org.litote.kmongo.findOne
import org.litote.kmongo.getCollection
import org.litote.kmongo.setValue
import usecase.repository.SurgeryReportRepository

/**
 * It implements the [SurgeryReportRepository] via a mongodb database.
 * @param[customConnectionString] a custom connection string to use. If provided, it has priority over the
 * one specified in the system environment.
 */
class SurgeryReportDatabase(customConnectionString: String? = null) : SurgeryReportRepository {
    private val connectionString: String

    init {
        // If the client has not provided a custom connection string, then we need to get it from environment.
        if (customConnectionString == null) {
            checkNotNull(System.getenv(MONGODB_CONNECTION_STRING_VARIABLE)) { "mongodb connection string required" }
        }
        connectionString = customConnectionString ?: System.getenv(MONGODB_CONNECTION_STRING_VARIABLE)
    }

    private val database = KMongo.createClient(this.connectionString).getDatabase(DATABASE_NAME)
    private val surgeryReportCollection = this.database.getCollection<SurgeryReport>(SURGERY_REPORT_COLLECTION_NAME)

    override fun createSurgeryReport(surgeryReport: SurgeryReport): Boolean =
        this.surgeryReportCollection.safeMongodbWrite(defaultResult = false) {
            insertOne(surgeryReport).wasAcknowledged()
        }

    override fun integrateSurgeryReport(surgeryProcessID: SurgicalProcessID, informationToAdd: String): Boolean =
        this.surgeryReportCollection.safeMongodbWrite(false) {
            updateOne(
                SurgeryReport::surgicalProcessID eq surgeryProcessID,
                setValue(SurgeryReport::additionalData, informationToAdd),
            ).matchedCount > 0
        }

    override fun findBy(surgeryProcessID: SurgicalProcessID): SurgeryReport? = this.surgeryReportCollection.findOne {
        SurgeryReport::surgicalProcessID eq surgeryProcessID
    }

    override fun getSurgeryReports(): List<SurgeryReport> = this.surgeryReportCollection.find().toList()

    private fun <T, R> MongoCollection<T>.safeMongodbWrite(defaultResult: R, operation: MongoCollection<T>.() -> R): R =
        try {
            operation()
        } catch (exception: MongoException) {
            println(exception)
            defaultResult
        }

    companion object {
        private const val MONGODB_CONNECTION_STRING_VARIABLE = "MONGODB_CONNECTION_STRING"
        private const val DATABASE_NAME = "surgery_report"
        private const val SURGERY_REPORT_COLLECTION_NAME = "surgery_reports"
    }
}
