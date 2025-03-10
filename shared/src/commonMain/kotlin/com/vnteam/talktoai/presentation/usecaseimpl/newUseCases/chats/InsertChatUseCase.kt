package com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.chats

import com.vnteam.talktoai.CommonExtensions.getUserAuth
import com.vnteam.talktoai.Constants
import com.vnteam.talktoai.data.network.Result
import com.vnteam.talktoai.domain.enums.isAuthorisedUser
import com.vnteam.talktoai.domain.models.Chat
import com.vnteam.talktoai.domain.repositories.ChatRepository
import com.vnteam.talktoai.domain.repositories.PreferencesRepository
import com.vnteam.talktoai.domain.repositories.RealDataBaseRepository
import com.vnteam.talktoai.domain.usecase.UseCase
import com.vnteam.talktoai.utils.NetworkState
import kotlinx.coroutines.flow.firstOrNull

class InsertChatUseCase(
    private val networkState: NetworkState,
    private val preferencesRepository: PreferencesRepository,
    private val chatRepository: ChatRepository,
    private val realDataBaseRepository: RealDataBaseRepository
) : UseCase<Chat, Result<Unit>> {

    override suspend fun execute(params: Chat): Result<Unit> {
        val userAuth = preferencesRepository.getUserEmail().firstOrNull()
        when {
            userAuth.getUserAuth().isAuthorisedUser() -> when {
                networkState.isNetworkAvailable() -> realDataBaseRepository.insertChat(params)
                else -> return Result.Failure(Constants.APP_NETWORK_UNAVAILABLE_REPEAT)
            }

            else -> chatRepository.insertChat(params)
        }
        return Result.Success(Unit)
    }
}