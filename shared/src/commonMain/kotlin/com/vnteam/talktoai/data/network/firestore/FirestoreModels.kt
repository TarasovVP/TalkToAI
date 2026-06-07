package com.vnteam.talktoai.data.network.firestore

import kotlinx.serialization.Serializable

@Serializable
data class FirestoreValue(
    val stringValue: String? = null,
    val integerValue: String? = null,
    val doubleValue: Double? = null,
    val booleanValue: Boolean? = null,
    val nullValue: String? = null,
)

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
    value?.let { FirestoreValue(stringValue = it) } ?: FirestoreValue(nullValue = "NULL_VALUE")

fun firestoreInt(value: Long?) =
    value?.let { FirestoreValue(integerValue = it.toString()) }
        ?: FirestoreValue(nullValue = "NULL_VALUE")

fun firestoreDouble(value: Double?) =
    value?.let { FirestoreValue(doubleValue = it) } ?: FirestoreValue(nullValue = "NULL_VALUE")

fun firestoreBool(value: Boolean) = FirestoreValue(booleanValue = value)
