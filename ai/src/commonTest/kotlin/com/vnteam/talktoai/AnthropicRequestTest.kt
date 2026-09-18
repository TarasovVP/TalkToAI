package com.vnteam.talktoai

import com.vnteam.talktoai.data.network.ai.anthropic.toAnthropicRequest
import com.vnteam.talktoai.data.network.ai.anthropic.request.AnthropicImageBlock
import com.vnteam.talktoai.data.network.ai.anthropic.request.AnthropicImageSource
import com.vnteam.talktoai.data.network.ai.anthropic.request.AnthropicMessage
import com.vnteam.talktoai.data.network.ai.anthropic.request.AnthropicRequest
import com.vnteam.talktoai.data.network.ai.anthropic.request.AnthropicTextBlock
import com.vnteam.talktoai.data.network.ai.request.Message
import com.vnteam.talktoai.domain.models.MessageContent
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertTrue

private val testJson = Json { ignoreUnknownKeys = true }

private fun msg(role: String, text: String) =
    Message(role = role, content = listOf(MessageContent.Text(text)))

class AnthropicRequestTest {

    @Test
    fun mapperExtractsSystemIntoSystemField() {
        val messages = listOf(
            msg("system", "Be helpful"),
            msg("user", "Hello"),
            msg("assistant", "Hi"),
        )
        val request = messages.toAnthropicRequest(model = "claude-3-5-sonnet-20241022")
        assertEquals("Be helpful", request.system)
    }

    @Test
    fun mapperDoesNotIncludeSystemRoleInMessages() {
        val messages = listOf(
            msg("system", "Be helpful"),
            msg("user", "Hello"),
            msg("assistant", "Hi"),
        )
        val request = messages.toAnthropicRequest(model = "claude-3-5-sonnet-20241022")
        assertTrue(request.messages.none { it.role == "system" })
    }

    @Test
    fun mapperDoesNotDuplicateSystemTextInMessages() {
        val messages = listOf(
            msg("system", "Be helpful"),
            msg("user", "Hello"),
        )
        val request = messages.toAnthropicRequest(model = "claude-3-5-sonnet-20241022")
        assertFalse(request.messages.any { m ->
            m.content.filterIsInstance<AnthropicTextBlock>().any { it.text == "Be helpful" }
        })
    }

    @Test
    fun mapperPreservesUserAndAssistantMessages() {
        val messages = listOf(
            msg("system", "Be helpful"),
            msg("user", "Hello"),
            msg("assistant", "Hi"),
            msg("user", "How are you?"),
        )
        val request = messages.toAnthropicRequest(model = "claude-3-5-sonnet-20241022")
        assertEquals(3, request.messages.size)
        assertEquals("user", request.messages[0].role)
        assertEquals("assistant", request.messages[1].role)
        assertEquals("user", request.messages[2].role)
    }

    @Test
    fun mapperJoinsMultipleSystemMessagesWithNewline() {
        val messages = listOf(
            msg("system", "Global context"),
            msg("system", "Chat context"),
            msg("user", "Hello"),
        )
        val request = messages.toAnthropicRequest(model = "claude-3-5-sonnet-20241022")
        assertEquals("Global context\nChat context", request.system)
        assertEquals(1, request.messages.size)
    }

    @Test
    fun mapperSetsSystemNullWhenNoSystemMessages() {
        val messages = listOf(msg("user", "Hello"), msg("assistant", "Hi"))
        val request = messages.toAnthropicRequest(model = "claude-3-5-sonnet-20241022")
        assertEquals(null, request.system)
    }

    @Test
    fun mapperPassesTemperatureToRequest() {
        val request = listOf(msg("user", "Hello"))
            .toAnthropicRequest(model = "claude-haiku-4-5-20251001", temperature = 0.5f)
        assertEquals(0.5f, request.temperature)
    }

    @Test
    fun mapperDefaultsTemperatureToNull() {
        val request = listOf(msg("user", "Hello"))
            .toAnthropicRequest(model = "claude-haiku-4-5-20251001")
        assertEquals(null, request.temperature)
    }

    @Test
    fun imageBlockMapsToAnthropicImageBlock() {
        val messages = listOf(
            Message(
                role = "user",
                content = listOf(
                    MessageContent.Text("What is this?"),
                    MessageContent.Image(base64Data = "abc123", mimeType = "image/jpeg"),
                )
            )
        )
        val request = messages.toAnthropicRequest(model = "claude-3-5-sonnet-20241022")
        val blocks = request.messages.first().content
        assertEquals(2, blocks.size)
        assertIs<AnthropicTextBlock>(blocks[0])
        val imageBlock = assertIs<AnthropicImageBlock>(blocks[1])
        assertEquals("image/jpeg", imageBlock.source.mediaType)
        assertEquals("abc123", imageBlock.source.data)
        assertEquals("base64", imageBlock.source.type)
    }

    @Test
    fun textOnlyMessageMapsToSingleTextBlock() {
        val messages = listOf(msg("user", "Hello"))
        val request = messages.toAnthropicRequest(model = "claude-3-5-sonnet-20241022")
        val blocks = request.messages.first().content
        assertEquals(1, blocks.size)
        assertIs<AnthropicTextBlock>(blocks[0])
        assertEquals("Hello", (blocks[0] as AnthropicTextBlock).text)
    }

    @Test
    fun textBlockSerializesWithTypeField() {
        val request = AnthropicRequest(
            model = "claude-3-5-sonnet-20241022",
            messages = listOf(AnthropicMessage("user", listOf(AnthropicTextBlock(text = "Hi")))),
        )
        val json = testJson.encodeToString(request)
        assertTrue("\"type\":\"text\"" in json.replace(" ", ""))
    }

    @Test
    fun imageBlockSerializesWithTypeFieldAndSource() {
        val request = AnthropicRequest(
            model = "claude-3-5-sonnet-20241022",
            messages = listOf(
                AnthropicMessage(
                    "user",
                    listOf(
                        AnthropicImageBlock(
                            source = AnthropicImageSource(
                                mediaType = "image/png",
                                data = "base64data"
                            )
                        )
                    )
                )
            ),
        )
        val json = testJson.encodeToString(request)
        assertTrue("\"type\":\"image\"" in json.replace(" ", ""))
        assertTrue("\"media_type\":\"image/png\"" in json.replace(" ", ""))
    }

    @Test
    fun serializationWithNullSystemOmitsSystemKey() {
        val request = AnthropicRequest(
            model = "claude-3-5-sonnet-20241022",
            system = null,
            messages = listOf(AnthropicMessage("user", listOf(AnthropicTextBlock(text = "Hello")))),
        )
        val json = testJson.encodeToString(request)
        assertFalse("system" in json)
    }

    @Test
    fun serializationWithNullTemperatureOmitsTemperatureKey() {
        val request = AnthropicRequest(
            model = "claude-3-5-sonnet-20241022",
            temperature = null,
            messages = listOf(AnthropicMessage("user", listOf(AnthropicTextBlock(text = "Hello")))),
        )
        val json = testJson.encodeToString(request)
        assertFalse("temperature" in json)
    }
}
