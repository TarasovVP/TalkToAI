package com.vnteam.talktoai

import com.vnteam.talktoai.data.network.ai.openai.request.ApiRequest
import com.vnteam.talktoai.data.network.ai.openai.request.OpenAiMessage
import com.vnteam.talktoai.data.network.ai.openai.request.OpenAiTextBlock
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

private val testJson = Json { ignoreUnknownKeys = true }

private fun textMsg(role: String, text: String) =
    OpenAiMessage(role = role, content = listOf(OpenAiTextBlock(text = text)))

class ApiRequestTest {

    @Test
    fun serializationAlwaysIncludesMaxCompletionTokens() {
        val request = ApiRequest(
            model = "gpt-5.6-luna",
            messages = listOf(textMsg("user", "Hello")),
        )
        val json = testJson.encodeToString(request)
        assertTrue("\"max_completion_tokens\"" in json)
        assertTrue("8192" in json)
    }

    @Test
    fun serializationWithNullTemperatureOmitsTemperatureKey() {
        val request = ApiRequest(
            model = "gpt-5.6-luna",
            messages = listOf(textMsg("user", "Hello")),
            temperature = null,
        )
        val json = testJson.encodeToString(request)
        assertFalse("temperature" in json)
    }

    @Test
    fun serializationWithFilledTemperatureIncludesTemperatureValue() {
        val request = ApiRequest(
            model = "gpt-5.6-luna",
            messages = listOf(textMsg("user", "Hello")),
            temperature = 0.7f,
        )
        val json = testJson.encodeToString(request)
        assertTrue("\"temperature\"" in json)
    }
}
