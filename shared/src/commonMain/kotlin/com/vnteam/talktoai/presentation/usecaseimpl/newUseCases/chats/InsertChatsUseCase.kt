package com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.chats

import com.vnteam.talktoai.data.network.Result
import com.vnteam.talktoai.domain.models.Chat
import com.vnteam.talktoai.domain.repositories.ChatRepository
import com.vnteam.talktoai.domain.usecase.UseCase

class InsertChatsUseCase(
    private val chatRepository: ChatRepository,
) : UseCase<List<Chat>, Result<Unit>> {

    override suspend fun execute(params: List<Chat>): Result<Unit> {
        chatRepository.insertChats(params)
        return Result.Success(Unit)
    }
}