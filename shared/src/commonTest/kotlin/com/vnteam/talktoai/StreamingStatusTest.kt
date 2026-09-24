package com.vnteam.talktoai

import com.vnteam.talktoai.domain.enums.MessageStatus
import com.vnteam.talktoai.presentation.viewmodels.chats.streamingStatusFor
import kotlin.test.Test
import kotlin.test.assertEquals

class StreamingStatusTest {

    @Test
    fun emptyContentKeepsRequestingStatus() {
        assertEquals(MessageStatus.REQUESTING, streamingStatusFor(""))
    }

    @Test
    fun nonEmptyContentClearsRequestingStatus() {
        assertEquals(MessageStatus.STREAMING, streamingStatusFor("H"))
    }
}
