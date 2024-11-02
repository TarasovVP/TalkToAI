package com.vnteam.talktoai.presentation.usecaseimpl

import com.vnteam.talktoai.domain.repositories.ApiRepository
import com.vnteam.talktoai.domain.repositories.ChatDBRepository
import com.vnteam.talktoai.domain.usecase.ListUseCase
import kotlinx.coroutines.flow.Flow

class ListUseCaseImpl(private val apiRepository: ApiRepository, private val chatDbRepository: ChatDBRepository) :
    ListUseCase {

    override suspend fun getDemoObjectsFromApi(): Flow<List<Chat>?> {
        return apiRepository.getDemoObjectsFromApi()
    }

    override suspend fun clearChats() {
        return chatDbRepository.clearChats()
    }

    override suspend fun insertDemoObjectsToDB(chats: List<Chat>): Flow<Unit> {
        return chatDbRepository.insertChatsToDB(chats)
    }

    override suspend fun getDemoObjectsFromDB(): Flow<List<Chat>> {
        return chatDbRepository.getChatsFromDB()
    }

    override suspend fun deleteDemoObjectById(id: String): Flow<Unit> {
        return apiRepository.deleteDemoObjectById(id)
    }
}