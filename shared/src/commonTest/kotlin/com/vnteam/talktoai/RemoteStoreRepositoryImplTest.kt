package com.vnteam.talktoai

import com.vnteam.talktoai.data.network.firestore.FirestoreDocument
import com.vnteam.talktoai.data.network.firestore.firestoreInt
import com.vnteam.talktoai.data.network.firestore.firestoreString
import com.vnteam.talktoai.data.repositoryimpl.toFields
import com.vnteam.talktoai.data.repositoryimpl.toMessage
import com.vnteam.talktoai.domain.models.Message
import com.vnteam.talktoai.domain.models.MessageContent
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNull

class RemoteStoreRepositoryImplTest {

    @Test
    fun imageFieldsRoundTripThroughToFieldsAndToMessage() {
        val original = Message(
            id = 1L, chatId = 10L, author = "me",
            content = listOf(
                MessageContent.Text("look"),
                MessageContent.Image(mimeType = "image/jpeg", storageKey = "abc.jpg"),
            ),
            updatedAt = 100L, thumbnailBase64 = "dGh1bWI=",
        )
        val doc = FirestoreDocument(fields = original.toFields())
        val restored = doc.toMessage()!!

        assertEquals("dGh1bWI=", restored.thumbnailBase64)
        val image = restored.content.filterIsInstance<MessageContent.Image>().single()
        assertEquals("abc.jpg", image.storageKey)
        assertEquals("image/jpeg", image.mimeType)
        assertNull(image.base64Data)
    }

    @Test
    fun messageWithNoImageProducesTextOnlyContentOnRoundTrip() {
        val original = Message(id = 2L, chatId = 10L, content = listOf(MessageContent.Text("just text")))
        val doc = FirestoreDocument(fields = original.toFields())
        val restored = doc.toMessage()!!

        assertEquals(1, restored.content.size)
        assertIs<MessageContent.Text>(restored.content[0])
        assertNull(restored.thumbnailBase64)
    }

    @Test
    fun documentWithNoImageFieldsAtAllDecodesAsPlainText() {
        // Simulates a message written by this app before this feature existed.
        val doc = FirestoreDocument(
            fields = mapOf(
                "id" to firestoreInt(3L),
                "chatId" to firestoreInt(10L),
                "message" to firestoreString("old message"),
            )
        )
        val restored = doc.toMessage()!!
        assertEquals(1, restored.content.size)
        assertIs<MessageContent.Text>(restored.content[0])
        assertEquals("old message", (restored.content[0] as MessageContent.Text).text)
        assertNull(restored.thumbnailBase64)
    }
}
