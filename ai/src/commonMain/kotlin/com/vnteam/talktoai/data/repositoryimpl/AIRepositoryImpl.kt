package com.vnteam.talktoai.data.repositoryimpl

import com.vnteam.talktoai.data.network.Result
import com.vnteam.talktoai.data.network.UNKNOWN_ERROR
import com.vnteam.talktoai.data.network.ai.AiTextResponse
import com.vnteam.talktoai.data.network.ai.anthropic.AnthropicProvider
import com.vnteam.talktoai.data.network.ai.openai.OpenAiProvider
import com.vnteam.talktoai.data.network.ai.request.Message
import com.vnteam.talktoai.data.network.isModelNotSupportedError
import com.vnteam.talktoai.data.network.isTemperatureDeprecatedError
import com.vnteam.talktoai.data.network.parseErrorMessage
import com.vnteam.talktoai.domain.enums.AiProviderType
import com.vnteam.talktoai.domain.models.AiModels
import com.vnteam.talktoai.domain.repositories.AIRepository
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow

class AIRepositoryImpl(
    private val openAiProvider: OpenAiProvider,
    private val anthropicProvider: AnthropicProvider,
) : AIRepository {

    override fun sendRequest(
        model: String,
        messages: List<Message>,
        apiKey: String?,
        providerType: AiProviderType,
        temperature: Float?,
    ) = flow {
        emit(doSendRequest(model, messages, apiKey, providerType, temperature, isRetry = false))
    }

    private suspend fun doSendRequest(
        model: String,
        messages: List<Message>,
        apiKey: String?,
        providerType: AiProviderType,
        temperature: Float?,
        isRetry: Boolean,
    ): Result<AiTextResponse> {
        val provider = when (providerType) {
            AiProviderType.OPENAI -> openAiProvider
            AiProviderType.ANTHROPIC -> anthropicProvider
        }
        val result = provider.sendMessage(model, messages, apiKey, temperature).firstOrNull()
            ?: return Result.Failure(UNKNOWN_ERROR)

        if (result is Result.Success) return result

        val failure = result as Result.Failure
        val rawBody = failure.errorMessage.orEmpty()
        val statusCode = failure.statusCode ?: 0

        if (!isRetry && isTemperatureDeprecatedError(statusCode, rawBody) && temperature != null) {
            return doSendRequest(model, messages, apiKey, providerType, null, isRetry = true)
        }

        if (!isRetry && isModelNotSupportedError(statusCode, rawBody)) {
            val balanced = AiModels.balancedFor(providerType)
            if (balanced.id != model) {
                val retryResult = doSendRequest(balanced.id, messages, apiKey, providerType, temperature, isRetry = true)
                return when (retryResult) {
                    is Result.Success -> Result.Success(retryResult.data!!.copy(fallbackFrom = model))
                    else -> retryResult
                }
            }
        }

        return Result.Failure(parseErrorMessage(rawBody), statusCode)
    }
}
