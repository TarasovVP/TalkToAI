package com.vnteam.talktoai

import com.vnteam.talktoai.domain.models.Message
import com.vnteam.talktoai.domain.models.MessageContent
import com.vnteam.talktoai.domain.models.imageStorageKeys
import kotlin.test.Test
import kotlin.test.assertEquals

class MessageContentTest {

    @Test
    fun imageStorageKeysCollectsKeysAcrossMultipleMessages() {
        val messages = listOf(
            Message(id = 1L, content = listOf(
                MessageContent.Text("hi"),
                MessageContent.Image(mimeType = "image/png", storageKey = "a.png"),
            )),
            Message(id = 2L, content = listOf(MessageContent.Text("no image here"))),
            Message(id = 3L, content = listOf(
                MessageContent.Image(mimeType = "image/jpeg", storageKey = "b.jpg"),
            )),
        )
        assertEquals(listOf("a.png", "b.jpg"), messages.imageStorageKeys())
    }

    @Test
    fun imageStorageKeysSkipsImageBlocksWithNullStorageKey() {
        val messages = listOf(
            Message(id = 1L, content = listOf(MessageContent.Image(mimeType = "image/png", storageKey = null))),
        )
        assertEquals(emptyList(), messages.imageStorageKeys())
    }

    @Test
    fun imageStorageKeysReturnsEmptyForNoMessages() {
        assertEquals(emptyList(), emptyList<Message>().imageStorageKeys())
    }
}
