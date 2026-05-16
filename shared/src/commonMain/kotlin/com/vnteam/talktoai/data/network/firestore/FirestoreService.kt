package com.vnteam.talktoai.data.network.firestore

import com.vnteam.talktoai.data.network.NetworkConstants
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
            header(NetworkConstants.AUTHORIZATION, "Bearer $idToken")
            setBody(FirestoreDocument(fields = fields))
        }
        val ok = response.status.isSuccess()
        ok
    }.getOrElse { e ->
        false
    }

    suspend fun deleteDocument(path: String, idToken: String): Boolean = runCatching {
        val response = client.httpClient.delete("$base/$path") {
            header(NetworkConstants.AUTHORIZATION, "Bearer $idToken")
        }
        response.status.isSuccess()
    }.getOrDefault(false)

    suspend fun listDocuments(collectionPath: String, idToken: String): List<FirestoreDocument> =
        runCatching {
            val response = client.httpClient.get("$base/$collectionPath") {
                header(NetworkConstants.AUTHORIZATION, "Bearer $idToken")
            }
            if (response.status.isSuccess()) {
                response.body<FirestoreListResponse>().documents.orEmpty()
            } else {
                emptyList()
            }
        }.getOrDefault(emptyList())

    suspend fun runQuery(
        parentPath: String,
        query: FirestoreStructuredQuery,
        idToken: String,
    ): List<FirestoreDocument> = runCatching {
        val response = client.httpClient.post("$base/$parentPath:runQuery") {
            header(NetworkConstants.AUTHORIZATION, "Bearer $idToken")
            setBody(query)
        }
        if (response.status.isSuccess()) {
            response.body<List<FirestoreQueryResult>>()
                .mapNotNull { it.document }
        } else {
            emptyList()
        }
    }.getOrDefault(emptyList())

    suspend fun getDocument(path: String, idToken: String): FirestoreDocument? = runCatching {
        val response = client.httpClient.get("$base/$path") {
            header(NetworkConstants.AUTHORIZATION, "Bearer $idToken")
        }
        if (response.status.isSuccess()) response.body<FirestoreDocument>() else null
    }.getOrNull()
}
