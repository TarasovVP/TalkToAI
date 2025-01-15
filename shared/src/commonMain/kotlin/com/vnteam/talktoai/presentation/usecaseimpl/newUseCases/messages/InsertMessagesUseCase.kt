package com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.messages

import com.vnteam.talktoai.data.network.Result
import com.vnteam.talktoai.domain.models.Message
import com.vnteam.talktoai.domain.repositories.MessageRepository
import com.vnteam.talktoai.domain.usecase.UseCase

class InsertMessagesUseCase(
    private val messageRepository: MessageRepository
) : UseCase<List<Message>, Result<Unit>> {

    override suspend fun execute(params: List<Message>): Result<Unit> {
        messageRepository.insertMessages(params)
        return Result.Success(Unit)
    }
}