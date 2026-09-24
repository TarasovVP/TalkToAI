package com.vnteam.talktoai

import com.vnteam.talktoai.data.network.Result
import com.vnteam.talktoai.data.network.ai.AiProvider
import com.vnteam.talktoai.data.network.ai.AiTextResponse
import com.vnteam.talktoai.data.network.ai.request.Message
import com.vnteam.talktoai.data.repositoryimpl.AIRepositoryImpl
import com.vnteam.talktoai.domain.enums.AiProviderType
import com.vnteam.talktoai.domain.models.MessageContent
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest

private class FakeAiProvider(
    private val responsesPerCall: List<Flow<Result<AiTextResponse>>>,
) : AiProvider {
    val requestedModels = mutableListOf<String>()

    override fun sendMessage(
        model: String,
        messages: List<Message>,
        apiKey: String?,
        temperature: Float?,
    ): Flow<Result<AiTextResponse>> {
        val response = responsesPerCall[requestedModels.size]
        requestedModels.add(model)
        return response
    }
}

private fun messages() = listOf(Message(role = "user", content = listOf(MessageContent.Text("Hi"))))

private const val MODEL_NOT_SUPPORTED_BODY =
    """{"error":{"type":"invalid_request_error","message":"The model 'gpt-5.6-luna' does not exist"}}"""

class AIRepositoryStreamingTest {

    @Test
    fun retryAfterPartialTextResetsBeforeSecondAttempt() = runTest {
        val firstAttempt = flow {
            emit(Result.Success(AiTextResponse(model = "gpt-5.6-luna", content = "Sorry, ")))
            emit(Result.Failure(MODEL_NOT_SUPPORTED_BODY, statusCode = 400))
        }
        val secondAttempt = flow {
            emit(Result.Success(AiTextResponse(model = "gpt-5.6-terra", content = "H")))
            emit(Result.Success(AiTextResponse(model = "gpt-5.6-terra", content = "Hello")))
        }
        val provider = FakeAiProvider(listOf(firstAttempt, secondAttempt))
        val repository = AIRepositoryImpl(openAiProvider = provider, anthropicProvider = provider)

        val results = repository.sendRequest(
            model = "gpt-5.6-luna",
            messages = messages(),
            apiKey = null,
            providerType = AiProviderType.OPENAI,
            temperature = null,
        ).toList()

        val successes = results.filterIsInstance<Result.Success<AiTextResponse>>()
        assertEquals(listOf("gpt-5.6-luna", "gpt-5.6-terra"), provider.requestedModels, "Expected a retry against the balanced model")

        val resetIndex = successes.indexOfFirst { it.data!!.content == "" }
        assertTrue(resetIndex >= 0, "Expected an explicit reset-to-empty emit before the retried attempt")
        val afterReset = successes.subList(resetIndex + 1, successes.size)
        assertTrue(afterReset.isNotEmpty(), "Expected content from the retried attempt after the reset")
        afterReset.forEach {
            assertTrue(!it.data!!.content.contains("Sorry"), "Retried attempt's text must not contain the first attempt's text: ${it.data!!.content}")
        }
        assertEquals("Hello", afterReset.last().data!!.content)
    }

    @Test
    fun midStreamNetworkDropDoesNotRetryAndPreservesPartialText() = runTest {
        val onlyAttempt = flow {
            emit(Result.Success(AiTextResponse(model = "gpt-5.6-terra", content = "Hi")))
            emit(Result.Success(AiTextResponse(model = "gpt-5.6-terra", content = "Hi there")))
            emit(Result.Failure("Connection closed before response was complete"))
        }
        val provider = FakeAiProvider(listOf(onlyAttempt))
        val repository = AIRepositoryImpl(openAiProvider = provider, anthropicProvider = provider)

        val results = repository.sendRequest(
            model = "gpt-5.6-terra",
            messages = messages(),
            apiKey = null,
            providerType = AiProviderType.OPENAI,
            temperature = null,
        ).toList()

        assertEquals(1, provider.requestedModels.size, "No retry should have been attempted")
        val successes = results.filterIsInstance<Result.Success<AiTextResponse>>()
        assertEquals(listOf("Hi", "Hi there"), successes.map { it.data!!.content }, "Partial text emits must be untouched")
        assertIs<Result.Failure>(results.last())
    }
}
