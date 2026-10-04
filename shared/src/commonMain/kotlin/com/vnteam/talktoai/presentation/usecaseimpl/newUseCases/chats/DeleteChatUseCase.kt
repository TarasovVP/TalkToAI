package com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.chats

import com.vnteam.talktoai.CommonExtensions.getUserAuth
import com.vnteam.talktoai.Constants
import com.vnteam.talktoai.Constants.DEFAULT_CHAT_ID
import com.vnteam.talktoai.data.filestorage.ImageStorage
import com.vnteam.talktoai.data.network.Result
import com.vnteam.talktoai.domain.enums.isAuthorisedUser
import com.vnteam.talktoai.domain.models.Chat
import com.vnteam.talktoai.domain.models.imageStorageKeys
import com.vnteam.talktoai.domain.repositories.ChatRepository
import com.vnteam.talktoai.domain.repositories.MessageRepository
import com.vnteam.talktoai.domain.repositories.PreferencesRepository
import com.vnteam.talktoai.domain.repositories.RemoteStoreRepository
import com.vnteam.talktoai.domain.usecase.UseCase
import com.vnteam.talktoai.utils.NetworkState
import kotlinx.coroutines.flow.firstOrNull

class DeleteChatUseCase(
    private val networkState: NetworkState,
    private val preferencesRepository: PreferencesRepository,
    private val chatRepository: ChatRepository,
    private val messageRepository: MessageRepository,
    private val remoteStoreRepository: RemoteStoreRepository,
    private val imageStorage: ImageStorage,
) : UseCase<Chat, Result<Unit>> {

    override suspend fun execute(params: Chat): Result<Unit> {
        val chatId = params.id ?: DEFAULT_CHAT_ID
        val storageKeys = messageRepository.getMessagesFromChat(chatId).firstOrNull().orEmpty().imageStorageKeys()
        val userAuth = preferencesRepository.getUserEmail().firstOrNull()
        if (userAuth.getUserAuth().isAuthorisedUser()) {
            if (!networkState.isNetworkAvailable()) {
                return Result.Failure(Constants.APP_NETWORK_UNAVAILABLE_REPEAT)
            }
            remoteStoreRepository.deleteMessagesByChatId(chatId).firstOrNull()
            when (val result = remoteStoreRepository.deleteChat(params).firstOrNull()) {
                is Result.Failure -> return result
                is Result.Success -> Unit
                else -> return Result.Failure("Firestore delete chat failed")
            }
        }
        chatRepository.deleteChat(params)
        messageRepository.deleteMessagesFromChat(chatId)
        imageStorage.deleteAll(storageKeys)
        return Result.Success(Unit)
    }
}
