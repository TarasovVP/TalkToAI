package com.vnteam.talktoai

import com.vnteam.talktoai.data.mapperimpls.MessageDBMapperImpl
import com.vnteam.talktoai.domain.models.Message
import com.vnteam.talktoai.domain.models.MessageContent
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNull

private val testJson = Json { ignoreUnknownKeys = true }

class MessageDBMapperTest {

    private val mapper = MessageDBMapperImpl()

    private fun makeMessageDB(
        id: Long = 1L,
        message: String? = null,
        contentJson: String? = null,
    ) = MessageDB(
        id = id,
        chatId = null,
        author = "user",
        message = message,
        updatedAt = null,
        status = null,
        errorMessage = null,
        truncated = 0L,
        contentJson = contentJson,
    )

    @Test
    fun legacyRowWithNullContentJsonFallsBackToTextBlock() {
        val db = makeMessageDB(message = "Hello from old row", contentJson = null)
        val result = mapper.mapFromImplModel(db)
        assertEquals(1, result.content.size)
        val text = assertIs<MessageContent.Text>(result.content[0])
        assertEquals("Hello from old row", text.text)
    }

    @Test
    fun legacyRowWithNullMessageProducesEmptyTextBlock() {
        val db = makeMessageDB(message = null, contentJson = null)
        val result = mapper.mapFromImplModel(db)
        assertEquals(1, result.content.size)
        val text = assertIs<MessageContent.Text>(result.content[0])
        assertEquals("", text.text)
    }

    @Test
    fun rowWithTextOnlyContentJsonDeserializesCorrectly() {
        val content = listOf(MessageContent.Text("Hello stored"))
        val db = makeMessageDB(message = "Hello stored", contentJson = testJson.encodeToString(content))
        val result = mapper.mapFromImplModel(db)
        assertEquals(1, result.content.size)
        val text = assertIs<MessageContent.Text>(result.content[0])
        assertEquals("Hello stored", text.text)
    }

    @Test
    fun rowWithImageContentJsonDeserializesImageBlock() {
        val content = listOf(
            MessageContent.Text("Look at this"),
            MessageContent.Image(base64Data = "abc123", mimeType = "image/jpeg"),
        )
        val db = makeMessageDB(message = "Look at this", contentJson = testJson.encodeToString(content))
        val result = mapper.mapFromImplModel(db)
        assertEquals(2, result.content.size)
        val text = assertIs<MessageContent.Text>(result.content[0])
        assertEquals("Look at this", text.text)
        val image = assertIs<MessageContent.Image>(result.content[1])
        assertEquals("abc123", image.base64Data)
        assertEquals("image/jpeg", image.mimeType)
    }

    @Test
    fun corruptContentJsonFallsBackToMessageText() {
        val db = makeMessageDB(message = "Fallback text", contentJson = "not valid json {{{{")
        val result = mapper.mapFromImplModel(db)
        assertEquals(1, result.content.size)
        val text = assertIs<MessageContent.Text>(result.content[0])
        assertEquals("Fallback text", text.text)
    }

    @Test
    fun mapToImplModelPreservesTextAsMessage() {
        val msg = Message(
            id = 2L,
            content = listOf(MessageContent.Text("Round trip")),
        )
        val db = mapper.mapToImplModel(msg)
        assertEquals("Round trip", db.message)
    }

    @Test
    fun mapToImplModelEncodesContentJson() {
        val msg = Message(
            id = 3L,
            content = listOf(MessageContent.Text("With image"), MessageContent.Image("data", "image/png")),
        )
        val db = mapper.mapToImplModel(msg)
        val decoded = testJson.decodeFromString<List<MessageContent>>(db.contentJson!!)
        assertEquals(2, decoded.size)
        assertIs<MessageContent.Text>(decoded[0])
        assertIs<MessageContent.Image>(decoded[1])
    }

    @Test
    fun roundTripTextOnlyMessageIsSymmetric() {
        val original = Message(
            id = 10L,
            chatId = 5L,
            author = "assistant",
            content = listOf(MessageContent.Text("Hello back")),
        )
        val db = mapper.mapToImplModel(original)
        val restored = mapper.mapFromImplModel(db)
        assertEquals(original.content, restored.content)
        assertEquals(original.author, restored.author)
        assertEquals(original.chatId, restored.chatId)
    }

    @Test
    fun computedMessagePropertyMatchesFirstTextBlock() {
        val msg = Message(content = listOf(MessageContent.Text("computed")))
        assertEquals("computed", msg.message)
    }

    @Test
    fun computedMessagePropertyJoinsMultipleTextBlocks() {
        val msg = Message(
            content = listOf(
                MessageContent.Text("Part1"),
                MessageContent.Image("x", "image/jpeg"),
                MessageContent.Text("Part2"),
            )
        )
        assertEquals("Part1Part2", msg.message)
    }

    @Test
    fun emptyContentListGivesEmptyMessageProperty() {
        val msg = Message(content = emptyList())
        assertEquals("", msg.message)
    }
}
