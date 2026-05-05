package com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.messages

import com.vnteam.talktoai.CommonExtensions.getUserAuth
import com.vnteam.talktoai.Constants
import com.vnteam.talktoai.data.network.Result
import com.vnteam.talktoai.domain.enums.isAuthorisedUser
import com.vnteam.talktoai.domain.models.Message
import com.vnteam.talktoai.domain.repositories.MessageRepository
import com.vnteam.talktoai.domain.repositories.PreferencesRepository
import com.vnteam.talktoai.domain.repositories.RealDataBaseRepository
import com.vnteam.talktoai.domain.usecase.UseCase
import com.vnteam.talktoai.utils.NetworkState
import kotlinx.coroutines.flow.firstOrNull

class InsertMessageUseCase(
    private val networkState: NetworkState,
    private val preferencesRepository: PreferencesRepository,
    private val messageRepository: MessageRepository,
    private val realDataBaseRepository: RealDataBaseRepository,
) : UseCase<Message, Result<Unit>> {

    override suspend fun execute(params: Message): Result<Unit> {
        val userAuth = preferencesRepository.getUserEmail().firstOrNull()
        if (userAuth.getUserAuth().isAuthorisedUser()) {
            if (!networkState.isNetworkAvailable()) {
                return Result.Failure(Constants.APP_NETWORK_UNAVAILABLE_REPEAT)
            }
            realDataBaseRepository.insertMessage(params)
        }
        messageRepository.insertMessage(params)
        return Result.Success(Unit)
    }
}