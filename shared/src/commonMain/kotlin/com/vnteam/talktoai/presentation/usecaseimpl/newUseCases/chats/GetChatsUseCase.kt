package com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.chats

import com.vnteam.talktoai.data.network.Result
import com.vnteam.talktoai.domain.models.Chat
import com.vnteam.talktoai.domain.repositories.ChatRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

class GetChatsUseCase(private val chatRepository: ChatRepository) {

    fun execute(): Flow<Result<List<Chat>>> {
        return chatRepository.getChats().map {
            Result.Success(it)
        }.catch {
            Result.Failure(it.message)
        }
    }
}