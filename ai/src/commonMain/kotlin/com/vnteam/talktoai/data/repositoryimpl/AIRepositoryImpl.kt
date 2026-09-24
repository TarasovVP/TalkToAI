package com.vnteam.talktoai.data.repositoryimpl

import com.vnteam.talktoai.data.network.Result
import com.vnteam.talktoai.data.network.ai.AiProvider
import com.vnteam.talktoai.data.network.ai.AiTextResponse
import com.vnteam.talktoai.data.network.ai.request.Message
import com.vnteam.talktoai.data.network.isModelNotSupportedError
import com.vnteam.talktoai.data.network.isTemperatureDeprecatedError
import com.vnteam.talktoai.data.network.parseErrorMessage
import com.vnteam.talktoai.domain.enums.AiProviderType
import com.vnteam.talktoai.domain.models.AiModels
import com.vnteam.talktoai.domain.repositories.AIRepository
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.flow

class AIRepositoryImpl(
    private val openAiProvider: AiProvider,
    private val anthropicProvider: AiProvider,
) : AIRepository {

    override fun sendRequest(
        model: String,
        messages: List<Message>,
        apiKey: String?,
        providerType: AiProviderType,
        temperature: Float?,
    ) = flow {
        doSendRequest(this, model, messages, apiKey, providerType, temperature, isRetry = false)
    }

    private suspend fun doSendRequest(
        collector: FlowCollector<Result<AiTextResponse>>,
        model: String,
        messages: List<Message>,
        apiKey: String?,
        providerType: AiProviderType,
        temperature: Float?,
        isRetry: Boolean,
        fallbackFrom: String? = null,
    ) {
        val provider = when (providerType) {
            AiProviderType.OPENAI -> openAiProvider
            AiProviderType.ANTHROPIC -> anthropicProvider
        }
        var terminalFailure: Result.Failure? = null
        provider.sendMessage(model, messages, apiKey, temperature).collect { result ->
            when (result) {
                is Result.Success -> {
                    val data = result.data
                    collector.emit(
                        if (fallbackFrom != null && data != null) {
                            Result.Success(data.copy(fallbackFrom = fallbackFrom))
                        } else {
                            result
                        }
                    )
                }

                is Result.Failure -> terminalFailure = result
                else -> Unit
            }
        }
        val failure = terminalFailure ?: return
        val rawBody = failure.errorMessage.orEmpty()
        val statusCode = failure.statusCode ?: 0

        if (!isRetry && isTemperatureDeprecatedError(statusCode, rawBody) && temperature != null) {
            collector.emit(Result.Success(AiTextResponse(model = model, content = "", fallbackFrom = fallbackFrom)))
            doSendRequest(collector, model, messages, apiKey, providerType, null, isRetry = true, fallbackFrom = fallbackFrom)
            return
        }

        if (!isRetry && isModelNotSupportedError(statusCode, rawBody)) {
            val balanced = AiModels.balancedFor(providerType)
            if (balanced.id != model) {
                collector.emit(Result.Success(AiTextResponse(model = balanced.id, content = "", fallbackFrom = model)))
                doSendRequest(collector, balanced.id, messages, apiKey, providerType, temperature, isRetry = true, fallbackFrom = model)
                return
            }
        }

        collector.emit(Result.Failure(parseErrorMessage(rawBody), statusCode))
    }
}
