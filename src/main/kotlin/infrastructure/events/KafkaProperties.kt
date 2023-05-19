/*
 * Copyright (c) 2023. Smart Operating Block
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package infrastructure.events

/**
 * Load the properties needed to initialize the Kafka Consumer.
 * They are needed the [boostrapServerUrl] and the [schemaRegistryUrl].
 */
fun loadConsumerProperties(boostrapServerUrl: String, schemaRegistryUrl: String) = mapOf(
    "bootstrap.servers" to boostrapServerUrl,
    "schema.registry.url" to schemaRegistryUrl,
    "group.id" to "surgery-report-consumer",
    "key.deserializer" to "org.apache.kafka.common.serialization.StringDeserializer",
    "value.deserializer" to "org.apache.kafka.common.serialization.StringDeserializer",
)
