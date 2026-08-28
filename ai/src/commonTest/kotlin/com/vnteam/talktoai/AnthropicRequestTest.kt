package com.vnteam.talktoai

import com.vnteam.talktoai.data.network.ai.anthropic.toAnthropicRequest
import com.vnteam.talktoai.data.network.ai.anthropic.request.AnthropicContentBlock
import com.vnteam.talktoai.data.network.ai.anthropic.request.AnthropicMessage
import com.vnteam.talktoai.data.network.ai.anthropic.request.AnthropicRequest
import com.vnteam.talktoai.data.network.ai.openai.request.MessageApi
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

private val testJson = Json { ignoreUnknownKeys = true }

class AnthropicRequestTest {

    // --- Mapper tests ---

    @Test
    fun mapperExtractsSystemIntoSystemField() {
        val messages = listOf(
            MessageApi(role = "system", content = "Be helpful"),
            MessageApi(role = "user", content = "Hello"),
            MessageApi(role = "assistant", content = "Hi"),
        )
        val request = messages.toAnthropicRequest(model = "claude-3-5-sonnet-20241022", temperature = 0.7f)
        assertEquals("Be helpful", request.system)
    }

    @Test
    fun mapperDoesNotIncludeSystemRoleInMessages() {
        val messages = listOf(
            MessageApi(role = "system", content = "Be helpful"),
            MessageApi(role = "user", content = "Hello"),
            MessageApi(role = "assistant", content = "Hi"),
        )
        val request = messages.toAnthropicRequest(model = "claude-3-5-sonnet-20241022", temperature = 0.7f)
        assertTrue(request.messages.none { it.role == "system" })
    }

    @Test
    fun mapperDoesNotDuplicateSystemTextInMessages() {
        val messages = listOf(
            MessageApi(role = "system", content = "Be helpful"),
            MessageApi(role = "user", content = "Hello"),
        )
        val request = messages.toAnthropicRequest(model = "claude-3-5-sonnet-20241022", temperature = 0.7f)
        assertFalse(request.messages.any { msg -> msg.content.any { it.text == "Be helpful" } })
    }

    @Test
    fun mapperPreservesUserAndAssistantMessages() {
        val messages = listOf(
            MessageApi(role = "system", content = "Be helpful"),
            MessageApi(role = "user", content = "Hello"),
            MessageApi(role = "assistant", content = "Hi"),
            MessageApi(role = "user", content = "How are you?"),
        )
        val request = messages.toAnthropicRequest(model = "claude-3-5-sonnet-20241022", temperature = 0.7f)
        assertEquals(3, request.messages.size)
        assertEquals("user", request.messages[0].role)
        assertEquals("assistant", request.messages[1].role)
        assertEquals("user", request.messages[2].role)
    }

    @Test
    fun mapperJoinsMultipleSystemMessagesWithNewline() {
        val messages = listOf(
            MessageApi(role = "system", content = "Global context"),
            MessageApi(role = "system", content = "Chat context"),
            MessageApi(role = "user", content = "Hello"),
        )
        val request = messages.toAnthropicRequest(model = "claude-3-5-sonnet-20241022", temperature = 0.7f)
        assertEquals("Global context\nChat context", request.system)
        assertEquals(1, request.messages.size)
    }

    @Test
    fun mapperSetsSystemNullWhenNoSystemMessages() {
        val messages = listOf(
            MessageApi(role = "user", content = "Hello"),
            MessageApi(role = "assistant", content = "Hi"),
        )
        val request = messages.toAnthropicRequest(model = "claude-3-5-sonnet-20241022", temperature = 0.7f)
        assertEquals(null, request.system)
    }

    // --- Serialization tests ---

    @Test
    fun serializationWithNullSystemOmitsSystemKey() {
        val request = AnthropicRequest(
            model = "claude-3-5-sonnet-20241022",
            temperature = 0.7f,
            system = null,
            messages = listOf(AnthropicMessage("user", listOf(AnthropicContentBlock(text = "Hello")))),
        )
        val json = testJson.encodeToString(request)
        assertFalse("system" in json)
    }

    @Test
    fun serializationWithFilledSystemIncludesSystemValue() {
        val request = AnthropicRequest(
            model = "claude-3-5-sonnet-20241022",
            temperature = 0.7f,
            system = "Be helpful",
            messages = listOf(AnthropicMessage("user", listOf(AnthropicContentBlock(text = "Hello")))),
        )
        val json = testJson.encodeToString(request)
        assertTrue("\"system\"" in json)
        assertTrue("Be helpful" in json)
    }
}
