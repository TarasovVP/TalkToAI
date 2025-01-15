package com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.messages

import com.vnteam.talktoai.data.network.Result
import com.vnteam.talktoai.domain.models.Message
import com.vnteam.talktoai.domain.repositories.MessageRepository
import com.vnteam.talktoai.domain.usecase.UseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

class GetMessagesUseCase(private val messageRepository: MessageRepository):
    UseCase<Nothing?, Flow<Result<List<Message>>>> {

    override suspend fun execute(params: Nothing?): Flow<Result<List<Message>>> {
        return messageRepository.getMessages().map {
            Result.Success(it)
        }.catch {
            Result.Failure(it.message)
        }
    }
}