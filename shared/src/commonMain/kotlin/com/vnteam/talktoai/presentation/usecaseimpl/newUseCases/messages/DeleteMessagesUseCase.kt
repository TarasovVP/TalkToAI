package com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.messages

import com.vnteam.talktoai.CommonExtensions.getUserAuth
import com.vnteam.talktoai.Constants
import com.vnteam.talktoai.data.filestorage.ImageStorage
import com.vnteam.talktoai.data.network.Result
import com.vnteam.talktoai.domain.enums.isAuthorisedUser
import com.vnteam.talktoai.domain.models.imageStorageKeys
import com.vnteam.talktoai.domain.repositories.MessageRepository
import com.vnteam.talktoai.domain.repositories.PreferencesRepository
import com.vnteam.talktoai.domain.repositories.RemoteStoreRepository
import com.vnteam.talktoai.domain.usecase.UseCase
import com.vnteam.talktoai.utils.NetworkState
import kotlinx.coroutines.flow.firstOrNull

class DeleteMessagesUseCase(
    private val networkState: NetworkState,
    private val preferencesRepository: PreferencesRepository,
    private val messageRepository: MessageRepository,
    private val remoteStoreRepository: RemoteStoreRepository,
    private val imageStorage: ImageStorage,
) : UseCase<List<Long>, Result<Unit>> {

    override suspend fun execute(params: List<Long>): Result<Unit> {
        val storageKeys = messageRepository.getMessagesByIds(params).imageStorageKeys()
        val userAuth = preferencesRepository.getUserEmail().firstOrNull()
        if (userAuth.getUserAuth().isAuthorisedUser()) {
            if (!networkState.isNetworkAvailable()) {
                return Result.Failure(Constants.APP_NETWORK_UNAVAILABLE_REPEAT)
            }
            remoteStoreRepository.deleteMessages(params.map { it.toString() }).firstOrNull()
        }
        messageRepository.deleteMessages(params)
        imageStorage.deleteAll(storageKeys)
        return Result.Success(Unit)
    }
}