package com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.chats

import com.vnteam.talktoai.Constants.DEFAULT_CHAT_ID
import com.vnteam.talktoai.data.network.Result
import com.vnteam.talktoai.domain.models.Chat
import com.vnteam.talktoai.domain.repositories.ChatRepository
import com.vnteam.talktoai.domain.usecase.UseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

class GetChatWithIdUseCase(private val chatRepository: ChatRepository) :
    UseCase<Long, Flow<Result<Chat>>> {

    override suspend fun execute(params: Long): Flow<Result<Chat>> {
        return when (params) {
            DEFAULT_CHAT_ID -> chatRepository.getLastUpdatedChat()
            else -> chatRepository.getChatById(params.toString())
        }.map {
            Result.Success(it ?: Chat())
        }.catch {
            Result.Failure(it.message)
        }
    }
}