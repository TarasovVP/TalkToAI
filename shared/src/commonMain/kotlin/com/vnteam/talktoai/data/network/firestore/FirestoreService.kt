package com.vnteam.talktoai.data.network.firestore

import com.vnteam.talktoai.data.network.AuthEventBus
import com.vnteam.talktoai.data.network.NetworkConstants
import com.vnteam.talktoai.data.network.Result
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.patch
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.isSuccess
import secrets.Secrets

class FirestoreService(private val client: FirestoreHttpClient) {

    private val base =
        "/v1/projects/${Secrets.FIRESTORE_PROJECT_ID}/databases/(default)/documents"

    suspend fun setDocument(
        path: String,
        fields: Map<String, FirestoreValue>,
        idToken: String,
    ): Boolean = runCatching {
        val response = client.httpClient.patch("$base/$path") {
            header(NetworkConstants.OPENAI_AUTHORIZATION_HEADER, "Bearer $idToken")
            setBody(FirestoreDocument(fields = fields))
        }
        if (!response.status.isSuccess()) {
            if (response.status.value in 401..403) AuthEventBus.emitUnauthorized()
            false
        } else true
    }.getOrElse { false }

    suspend fun deleteDocument(path: String, idToken: String): Boolean = runCatching {
        val response = client.httpClient.delete("$base/$path") {
            header(NetworkConstants.OPENAI_AUTHORIZATION_HEADER, "Bearer $idToken")
        }
        val ok = response.status.isSuccess()
        if (!ok) {
            if (response.status.value in 401..403) AuthEventBus.emitUnauthorized()
        }
        ok
    }.getOrDefault(false)

    suspend fun listDocuments(collectionPath: String, idToken: String): Result<List<FirestoreDocument>> =
        runCatching {
            val response = client.httpClient.get("$base/$collectionPath") {
                header(NetworkConstants.OPENAI_AUTHORIZATION_HEADER, "Bearer $idToken")
            }
            if (response.status.isSuccess()) {
                Result.Success(response.body<FirestoreListResponse>().documents.orEmpty())
            } else {
                if (response.status.value in 401..403) AuthEventBus.emitUnauthorized()
                Result.Failure("Firestore error ${response.status.value}", statusCode = response.status.value)
            }
        }.getOrElse { Result.Failure(it.message ?: "Unknown error") }

    suspend fun runQuery(
        parentPath: String,
        query: FirestoreStructuredQuery,
        idToken: String,
    ): List<FirestoreDocument> = runCatching {
        val response = client.httpClient.post("$base/$parentPath:runQuery") {
            header(NetworkConstants.OPENAI_AUTHORIZATION_HEADER, "Bearer $idToken")
            setBody(query)
        }
        if (response.status.isSuccess()) {
            response.body<List<FirestoreQueryResult>>()
                .mapNotNull { it.document }
        } else {
            if (response.status.value in 401..403) AuthEventBus.emitUnauthorized()
            emptyList()
        }
    }.getOrDefault(emptyList())

    suspend fun getDocument(path: String, idToken: String): FirestoreDocument? = runCatching {
        val response = client.httpClient.get("$base/$path") {
            header(NetworkConstants.OPENAI_AUTHORIZATION_HEADER, "Bearer $idToken")
        }
        if (response.status.isSuccess()) {
            response.body<FirestoreDocument>()
        } else {
            if (response.status.value in 401..403) AuthEventBus.emitUnauthorized()
            null
        }
    }.getOrNull()
}
