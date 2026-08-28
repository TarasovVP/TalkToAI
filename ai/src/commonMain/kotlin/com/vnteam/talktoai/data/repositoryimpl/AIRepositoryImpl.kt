package com.vnteam.talktoai.data.repositoryimpl

import com.vnteam.talktoai.data.network.Result
import com.vnteam.talktoai.data.network.UNKNOWN_ERROR
import com.vnteam.talktoai.data.network.ai.AiTextResponse
import com.vnteam.talktoai.data.network.ai.openai.OpenAiService
import com.vnteam.talktoai.data.network.ai.openai.request.ApiRequest
import com.vnteam.talktoai.data.network.ai.openai.response.ApiResponse
import com.vnteam.talktoai.data.network.isModelNotSupportedError
import com.vnteam.talktoai.data.network.parseErrorMessage
import com.vnteam.talktoai.domain.enums.AiProviderType
import com.vnteam.talktoai.domain.models.AiModels
import com.vnteam.talktoai.domain.repositories.AIRepository
import io.ktor.client.call.body
import io.ktor.client.statement.bodyAsText
import kotlinx.coroutines.flow.flow

class AIRepositoryImpl(
    private val aIService: OpenAiService,
) : AIRepository {

    override fun sendRequest(apiRequest: ApiRequest, apiKey: String?) = flow {
        emit(doSendRequest(apiRequest, apiKey, isRetry = false))
    }

    private suspend fun doSendRequest(
        apiRequest: ApiRequest,
        apiKey: String?,
        isRetry: Boolean,
    ): Result<AiTextResponse> {
        val response = try {
            aIService.sendRequest(apiRequest, apiKey)
        } catch (e: Exception) {
            return Result.Failure(e.message ?: UNKNOWN_ERROR)
        }

        if (response.status.value in 200..299) {
            return try {
                val apiResp = response.body<ApiResponse>()
                Result.Success(AiTextResponse(
                    model = apiResp.model.orEmpty(),
                    content = apiResp.choices?.firstOrNull()?.message?.content.orEmpty(),
                ))
            } catch (e: Exception) {
                Result.Failure(e.message ?: UNKNOWN_ERROR)
            }
        }

        val body = response.bodyAsText()
        val errorMessage = parseErrorMessage(body)

        if (!isRetry && isModelNotSupportedError(response.status.value, body)) {
            val balanced = AiModels.balancedFor(AiProviderType.OPENAI)
            if (balanced.id == apiRequest.model) {
                return Result.Failure(errorMessage, response.status.value)
            }
            val retryResult = doSendRequest(apiRequest.copy(model = balanced.id), apiKey, isRetry = true)
            return when (retryResult) {
                is Result.Success -> Result.Success(
                    retryResult.data!!.copy(fallbackFrom = apiRequest.model)
                )
                else -> retryResult
            }
        }

        return Result.Failure(errorMessage, response.status.value)
    }
}
