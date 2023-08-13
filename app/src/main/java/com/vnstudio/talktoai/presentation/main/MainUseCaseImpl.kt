package com.vnstudio.talktoai.presentation.main

import com.vnstudio.talktoai.data.database.db_entities.Chat
import com.vnstudio.talktoai.domain.repositories.ChatRepository
import com.vnstudio.talktoai.domain.usecases.MainUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MainUseCaseImpl @Inject constructor(private val chatRepository: ChatRepository) : MainUseCase {

    override suspend fun insertChat(chat: Chat) = chatRepository.insertChat(chat)

    override suspend fun getChats(): Flow<List<Chat>> = chatRepository.getChats()

    override suspend fun updateChat(chat: Chat) = chatRepository.updateChat(chat)

    override suspend fun deleteChat(chat: Chat) = chatRepository.deleteChat(chat)
}