package com.vnteam.talktoai

import com.vnteam.talktoai.data.network.ai.openai.toOpenAiRequest
import com.vnteam.talktoai.data.network.ai.openai.request.OpenAiImageBlock
import com.vnteam.talktoai.data.network.ai.openai.request.OpenAiTextBlock
import com.vnteam.talktoai.data.network.ai.request.Message
import com.vnteam.talktoai.domain.enums.AiProviderType
import com.vnteam.talktoai.domain.models.AiModels
import com.vnteam.talktoai.domain.models.MessageContent
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue

private val testJson = Json { ignoreUnknownKeys = true }

private fun msg(role: String, vararg content: MessageContent) =
    Message(role = role, content = content.toList())

class OpenAiRequestTest {

    @Test
    fun textOnlyMessageMapsToSingleTextBlock() {
        val messages = listOf(msg("user", MessageContent.Text("Hello")))
        val request = messages.toOpenAiRequest(model = "gpt-5.6-luna")
        val blocks = request.messages.first().content
        assertEquals(1, blocks.size)
        assertIs<OpenAiTextBlock>(blocks[0])
        assertEquals("Hello", (blocks[0] as OpenAiTextBlock).text)
    }

    @Test
    fun imageBlockMapsToImageUrlBlockWithDataUrl() {
        val messages = listOf(
            msg(
                "user",
                MessageContent.Text("What is this?"),
                MessageContent.Image(base64Data = "abc123", mimeType = "image/jpeg"),
            )
        )
        val request = messages.toOpenAiRequest(model = "gpt-5.6-terra")
        val blocks = request.messages.first().content
        assertEquals(2, blocks.size)
        assertIs<OpenAiTextBlock>(blocks[0])
        val imageBlock = assertIs<OpenAiImageBlock>(blocks[1])
        assertEquals("data:image/jpeg;base64,abc123", imageBlock.imageUrl.url)
    }

    @Test
    fun imageOnlyMessageMapsToSingleImageUrlBlock() {
        val messages = listOf(
            msg("user", MessageContent.Image(base64Data = "xyz", mimeType = "image/png"))
        )
        val request = messages.toOpenAiRequest(model = "gpt-5.6-sol")
        val blocks = request.messages.first().content
        assertEquals(1, blocks.size)
        val imageBlock = assertIs<OpenAiImageBlock>(blocks[0])
        assertEquals("data:image/png;base64,xyz", imageBlock.imageUrl.url)
    }

    @Test
    fun imageBlockSerializesWithTypeImageUrl() {
        val messages = listOf(
            msg("user", MessageContent.Image(base64Data = "d", mimeType = "image/webp"))
        )
        val request = messages.toOpenAiRequest(model = "gpt-5.6-luna")
        val json = testJson.encodeToString(request)
        assertTrue("\"type\":\"image_url\"" in json.replace(" ", ""))
        assertTrue("data:image/webp;base64,d" in json)
    }

    @Test
    fun textBlockSerializesWithTypeText() {
        val messages = listOf(msg("user", MessageContent.Text("Hi")))
        val request = messages.toOpenAiRequest(model = "gpt-5.6-luna")
        val json = testJson.encodeToString(request)
        assertTrue("\"type\":\"text\"" in json.replace(" ", ""))
    }

    @Test
    fun allOpenAiModelsSupportsVision() {
        AiModels.forProvider(AiProviderType.OPENAI).forEach { model ->
            assertTrue(model.supportsVision, "${model.id} should support vision")
        }
    }

    @Test
    fun allAnthropicModelsSupportsVision() {
        AiModels.forProvider(AiProviderType.ANTHROPIC).forEach { model ->
            assertTrue(model.supportsVision, "${model.id} should support vision")
        }
    }

    @Test
    fun unknownModelDoesNotSupportVision() {
        val result = AiModels.forProvider(AiProviderType.OPENAI).find { it.id == "gpt-3" }?.supportsVision
        assertTrue(result != true)
    }
}
