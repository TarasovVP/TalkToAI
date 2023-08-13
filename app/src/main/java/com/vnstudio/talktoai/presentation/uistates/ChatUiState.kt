package com.vnstudio.talktoai.presentation.uistates

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.toMutableStateList
import com.vnstudio.talktoai.data.database.db_entities.Message

data class ChatUiState(var choiceList: List<Message>, var loading: Boolean) {
    private val messagesStateList: MutableList<Message> = choiceList.toMutableStateList()
    val messages: List<Message> = messagesStateList

    var isLoadingState = mutableStateOf(loading)

    fun addMessage(message: Message) {
        messagesStateList.add(message)
    }

    fun clearMessages() {
        messagesStateList.clear()
    }
}
