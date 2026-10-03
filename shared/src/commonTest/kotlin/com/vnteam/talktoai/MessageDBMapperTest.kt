package com.vnteam.talktoai

import com.vnteam.talktoai.data.mapperimpls.MessageDBMapperImpl
import com.vnteam.talktoai.domain.models.Message
import com.vnteam.talktoai.domain.models.MessageContent
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertNull
import kotlin.test.assertTrue

private val testJson = Json { ignoreUnknownKeys = true }

class MessageDBMapperTest {

    private val mapper = MessageDBMapperImpl()

    private fun makeMessageDB(
        id: Long = 1L,
        message: String? = null,
        contentJson: String? = null,
        isComplete: Long? = null,
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
        isComplete = isComplete,
        inputTokens = null,
        outputTokens = null,
        cacheReadTokens = null,
        cacheWriteTokens = null,
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
        // base64Data is @Transient and never round-trips through contentJson, by design
        // (that's what fixes the original CursorWindow overflow) - only mimeType/storageKey
        // persist. Passing base64Data here is deliberate: it proves encoding ignores it.
        val content = listOf(
            MessageContent.Text("Look at this"),
            MessageContent.Image(base64Data = "abc123", mimeType = "image/jpeg", storageKey = "key.jpg"),
        )
        val db = makeMessageDB(message = "Look at this", contentJson = testJson.encodeToString(content))
        val result = mapper.mapFromImplModel(db)
        assertEquals(2, result.content.size)
        val text = assertIs<MessageContent.Text>(result.content[0])
        assertEquals("Look at this", text.text)
        val image = assertIs<MessageContent.Image>(result.content[1])
        assertEquals(null, image.base64Data)
        assertEquals("image/jpeg", image.mimeType)
        assertEquals("key.jpg", image.storageKey)
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
    fun mapToImplModelKeepsImageBlockWithoutBase64Data() {
        val msg = Message(
            id = 3L,
            content = listOf(
                MessageContent.Text("With image"),
                MessageContent.Image(base64Data = "data", mimeType = "image/png", storageKey = "key.png"),
            ),
        )
        val db = mapper.mapToImplModel(msg)
        // Raw-string assertion, not a round-trip through the same data class:
        // this is what actually catches a regression in @Transient on base64Data.
        assertTrue(db.contentJson!!.contains("\"storageKey\":\"key.png\""))
        assertTrue(db.contentJson!!.contains("\"mimeType\":\"image/png\""))
        assertFalse(db.contentJson!!.contains("base64Data"))
        assertFalse(db.contentJson!!.contains("\"data\""))

        val decoded = testJson.decodeFromString<List<MessageContent>>(db.contentJson!!)
        assertEquals(2, decoded.size)
        val image = assertIs<MessageContent.Image>(decoded[1])
        assertEquals(null, image.base64Data)
        assertEquals("key.png", image.storageKey)
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

    @Test
    fun nullIsCompleteReadsAsTrue() {
        val db = makeMessageDB(isComplete = null)
        assertEquals(true, mapper.mapFromImplModel(db).isComplete)
    }

    @Test
    fun zeroIsCompleteReadsAsFalse() {
        val db = makeMessageDB(isComplete = 0L)
        assertEquals(false, mapper.mapFromImplModel(db).isComplete)
    }

    @Test
    fun oneIsCompleteReadsAsTrue() {
        val db = makeMessageDB(isComplete = 1L)
        assertEquals(true, mapper.mapFromImplModel(db).isComplete)
    }

    @Test
    fun mapToImplModelEncodesIncompleteAsZero() {
        val msg = Message(id = 1L, content = listOf(MessageContent.Text("partial")), isComplete = false)
        val db = mapper.mapToImplModel(msg)
        assertEquals(0L, db.isComplete)
    }

    @Test
    fun mapToImplModelEncodesCompleteAsOne() {
        val msg = Message(id = 1L, content = listOf(MessageContent.Text("done")), isComplete = true)
        val db = mapper.mapToImplModel(msg)
        assertEquals(1L, db.isComplete)
    }

    @Test
    fun legacyRowReadsTokenFieldsAsNull() {
        val result = mapper.mapFromImplModel(makeMessageDB(message = "old"))
        assertNull(result.inputTokens)
        assertNull(result.outputTokens)
        assertNull(result.cacheReadTokens)
        assertNull(result.cacheWriteTokens)
    }

    @Test
    fun tokenFieldsRoundTrip() {
        val db = mapper.mapToImplModel(Message(inputTokens = 12, outputTokens = 10, cacheReadTokens = 1, cacheWriteTokens = 2))
        val back = mapper.mapFromImplModel(db)
        assertEquals(12, back.inputTokens)
        assertEquals(10, back.outputTokens)
        assertEquals(1, back.cacheReadTokens)
        assertEquals(2, back.cacheWriteTokens)
    }
}
