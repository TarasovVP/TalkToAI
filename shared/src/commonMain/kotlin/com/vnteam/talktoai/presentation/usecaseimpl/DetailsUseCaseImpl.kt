package com.vnteam.talktoai.presentation.usecaseimpl

import com.vnteam.talktoai.domain.models.Chat
import com.vnteam.talktoai.domain.repositories.ApiRepository
import com.vnteam.talktoai.domain.repositories.ChatDBRepository
import com.vnteam.talktoai.domain.usecase.DetailsUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flowOf

class DetailsUseCaseImpl(private val chatDbRepository: ChatDBRepository, private val apiRepository: ApiRepository) :
    DetailsUseCase {

    override suspend fun getDemoObjectById(id: String): Flow<Chat?> {
        val dbDemoObject = chatDbRepository.getChatById(id).firstOrNull()
        return if (dbDemoObject != null) {
            flowOf(dbDemoObject)
        } else {
            apiRepository.getDemoObjectById(id)
        }
    }
}