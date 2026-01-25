package com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.messages

import com.vnteam.talktoai.CommonExtensions.getUserAuth
import com.vnteam.talktoai.Constants
import com.vnteam.talktoai.data.network.Result
import com.vnteam.talktoai.domain.enums.isAuthorisedUser
import com.vnteam.talktoai.domain.repositories.MessageRepository
import com.vnteam.talktoai.domain.repositories.PreferencesRepository
import com.vnteam.talktoai.domain.repositories.RealDataBaseRepository
import com.vnteam.talktoai.domain.usecase.UseCase
import com.vnteam.talktoai.utils.NetworkState
import kotlinx.coroutines.flow.firstOrNull

class DeleteMessagesUseCase(
    private val networkState: NetworkState,
    private val preferencesRepository: PreferencesRepository,
    private val messageRepository: MessageRepository,
    private val realDataBaseRepository: RealDataBaseRepository,
) : UseCase<List<Long>, Result<Unit>> {

    override suspend fun execute(params: List<Long>): Result<Unit> {
        val userAuth = preferencesRepository.getUserEmail().firstOrNull()
        when {
            userAuth.getUserAuth().isAuthorisedUser() -> when {
                networkState.isNetworkAvailable() -> realDataBaseRepository.deleteMessages(params.map { it.toString() })
                else -> return Result.Failure(Constants.APP_NETWORK_UNAVAILABLE_REPEAT)
            }

            else -> messageRepository.deleteMessages(params)
        }
        return Result.Success(Unit)
    }
}