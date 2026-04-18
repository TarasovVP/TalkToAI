package com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.chats

import com.vnteam.talktoai.domain.repositories.ChatRepository
import com.vnteam.talktoai.domain.repositories.MessageRepository

class ClearLocalDataUseCase(
    private val chatRepository: ChatRepository,
    private val messageRepository: MessageRepository,
) {
    suspend fun execute() {
        messageRepository.clearMessages()
        chatRepository.clearChats()
    }
}
