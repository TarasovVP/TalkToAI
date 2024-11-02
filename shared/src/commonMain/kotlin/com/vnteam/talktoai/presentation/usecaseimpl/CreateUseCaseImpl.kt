package com.vnteam.talktoai.presentation.usecaseimpl

import com.vnteam.talktoai.domain.models.Chat
import com.vnteam.talktoai.domain.repositories.ApiRepository
import com.vnteam.talktoai.domain.repositories.ChatDBRepository
import com.vnteam.talktoai.domain.usecase.CreateUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flowOf

class CreateUseCaseImpl(private val apiRepository: ApiRepository, private val chatDbRepository: ChatDBRepository) :
    CreateUseCase {

    override suspend fun getDemoObjectById(id: String): Flow<Chat?> {
        val dbDemoObject = chatDbRepository.getChatById(id).firstOrNull()
        return if (dbDemoObject != null) {
            flowOf(dbDemoObject)
        } else {
            apiRepository.getDemoObjectById(id)
        }
    }

    override suspend fun insertDemoObjectToDB(chat: Chat): Flow<Unit> {
        return chatDbRepository.insertChatsToDB(listOf(chat))
    }


    override suspend fun createDemoObject(chat: Chat): Flow<Unit> {
        return apiRepository.insertDemoObjectsToApi(listOf(chat))
    }
}