package com.vnteam.talktoai

import com.vnteam.talktoai.domain.models.Message
import com.vnteam.talktoai.domain.models.MessageContent
import com.vnteam.talktoai.presentation.mapperimpls.MessageUIMapperImpl
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class MessageUIMapperTest {

    private val mapper = MessageUIMapperImpl()

    @Test
    fun mapToImplModelExtractsImageBlockIntoAttachedImage() {
        val message = Message(
            id = 1L,
            content = listOf(
                MessageContent.Text("look"),
                MessageContent.Image(mimeType = "image/png", storageKey = "key.png"),
            ),
        )
        val ui = mapper.mapToImplModel(message)
        assertEquals("key.png", ui.attachedImage?.storageKey)
        assertEquals("image/png", ui.attachedImage?.mimeType)
    }

    @Test
    fun mapToImplModelLeavesAttachedImageNullForTextOnlyMessage() {
        val message = Message(id = 2L, content = listOf(MessageContent.Text("just text")))
        val ui = mapper.mapToImplModel(message)
        assertNull(ui.attachedImage)
    }

    @Test
    fun mapFromImplModelStillFoldsAttachedImageIntoContent() {
        val ui = mapper.mapToImplModel(Message(id = 3L, content = listOf(MessageContent.Text("x"))))
        ui.attachedImage = MessageContent.Image(base64Data = "b64", mimeType = "image/jpeg")
        ui.message = "x"
        val message = mapper.mapFromImplModel(ui)
        assertEquals(2, message.content.size)
    }
}
