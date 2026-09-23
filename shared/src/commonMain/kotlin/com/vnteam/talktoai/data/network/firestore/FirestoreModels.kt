package com.vnteam.talktoai.data.network.firestore

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonEncoder
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.booleanOrNull
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.doubleOrNull
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.put

@Serializable(with = FirestoreValueSerializer::class)
data class FirestoreValue(
    val stringValue: String? = null,
    val integerValue: String? = null,
    val doubleValue: Double? = null,
    val booleanValue: Boolean? = null,
    val isNull: Boolean = false,
)

object FirestoreValueSerializer : KSerializer<FirestoreValue> {
    override val descriptor: SerialDescriptor = buildClassSerialDescriptor("FirestoreValue")

    override fun serialize(encoder: Encoder, value: FirestoreValue) {
        val jsonEncoder = encoder as? JsonEncoder
            ?: error("FirestoreValue can only be serialized as Json")
        val jsonObject = when {
            value.stringValue != null -> buildJsonObject { put("stringValue", JsonPrimitive(value.stringValue)) }
            value.integerValue != null -> buildJsonObject { put("integerValue", JsonPrimitive(value.integerValue)) }
            value.doubleValue != null -> buildJsonObject { put("doubleValue", JsonPrimitive(value.doubleValue)) }
            value.booleanValue != null -> buildJsonObject { put("booleanValue", JsonPrimitive(value.booleanValue)) }
            else -> buildJsonObject { put("nullValue", JsonNull) }
        }
        jsonEncoder.encodeJsonElement(jsonObject)
    }

    override fun deserialize(decoder: Decoder): FirestoreValue {
        val jsonDecoder = decoder as? JsonDecoder
            ?: error("FirestoreValue can only be deserialized from Json")
        val obj = jsonDecoder.decodeJsonElement().jsonObject
        return FirestoreValue(
            stringValue = obj["stringValue"]?.jsonPrimitive?.contentOrNull,
            integerValue = obj["integerValue"]?.jsonPrimitive?.contentOrNull,
            doubleValue = obj["doubleValue"]?.jsonPrimitive?.doubleOrNull,
            booleanValue = obj["booleanValue"]?.jsonPrimitive?.booleanOrNull,
            isNull = obj.containsKey("nullValue"),
        )
    }
}

@Serializable
data class FirestoreDocument(
    val name: String? = null,
    val fields: Map<String, FirestoreValue>? = null,
)

@Serializable
data class FirestoreListResponse(
    val documents: List<FirestoreDocument>? = null,
)

@Serializable
data class FirestoreQueryResult(
    val document: FirestoreDocument? = null,
)

@Serializable
data class FirestoreStructuredQuery(
    val structuredQuery: FirestoreQuery,
)

@Serializable
data class FirestoreQuery(
    val from: List<FirestoreCollectionSelector>,
    val where: FirestoreFilter? = null,
)

@Serializable
data class FirestoreCollectionSelector(
    val collectionId: String,
)

@Serializable
data class FirestoreFilter(
    val fieldFilter: FirestoreFieldFilter,
)

@Serializable
data class FirestoreFieldFilter(
    val field: FirestoreFieldReference,
    val op: String,
    val value: FirestoreValue,
)

@Serializable
data class FirestoreFieldReference(
    val fieldPath: String,
)

fun firestoreString(value: String?) =
    value?.let { FirestoreValue(stringValue = it) } ?: FirestoreValue(isNull = true)

fun firestoreInt(value: Long?) =
    value?.let { FirestoreValue(integerValue = it.toString()) } ?: FirestoreValue(isNull = true)

fun firestoreDouble(value: Double?) =
    value?.let { FirestoreValue(doubleValue = it) } ?: FirestoreValue(isNull = true)

fun firestoreBool(value: Boolean) = FirestoreValue(booleanValue = value)
