package com.vn.talktoai.presentation.main

import com.vn.talktoai.data.database.db_entities.Chat
import com.vn.talktoai.domain.repositories.ChatRepository
import com.vn.talktoai.domain.usecases.MainUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MainUseCaseImpl @Inject constructor(private val chatRepository: ChatRepository) : MainUseCase {

    override suspend fun insertChat(chat: Chat) = chatRepository.insertChat(chat)

    override suspend fun getChats(): Flow<List<Chat>> = chatRepository.getChats()

    override suspend fun updateChat(chat: Chat) = chatRepository.updateChat(chat)

    override suspend fun deleteChat(chat: Chat) = chatRepository.deleteChat(chat)
}