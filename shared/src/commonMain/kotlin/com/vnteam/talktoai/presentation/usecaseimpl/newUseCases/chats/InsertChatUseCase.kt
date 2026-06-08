package com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.chats

import com.vnteam.talktoai.CommonExtensions.getUserAuth
import com.vnteam.talktoai.Constants
import com.vnteam.talktoai.SettingsConstants
import com.vnteam.talktoai.data.network.Result
import com.vnteam.talktoai.domain.enums.isAuthorisedUser
import com.vnteam.talktoai.domain.models.Chat
import com.vnteam.talktoai.domain.repositories.ChatRepository
import com.vnteam.talktoai.domain.repositories.PreferencesRepository
import com.vnteam.talktoai.domain.repositories.RemoteStoreRepository
import com.vnteam.talktoai.domain.usecase.UseCase
import com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.settings.AiModelUseCase
import com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.settings.TemperatureUseCase
import com.vnteam.talktoai.utils.NetworkState
import kotlinx.coroutines.flow.firstOrNull

class InsertChatUseCase(
    private val networkState: NetworkState,
    private val preferencesRepository: PreferencesRepository,
    private val chatRepository: ChatRepository,
    private val remoteStoreRepository: RemoteStoreRepository,
    private val aiModelUseCase: AiModelUseCase,
    private val temperatureUseCase: TemperatureUseCase,
) : UseCase<Chat, Result<Chat>> {

    override suspend fun execute(params: Chat): Result<Chat> {
        val chat = params.withDefaultAiSettings()
        val userAuth = preferencesRepository.getUserEmail().firstOrNull()
        val authState = userAuth.getUserAuth()
        if (authState.isAuthorisedUser()) {
            val network = networkState.isNetworkAvailable()
            if (!network) {
                return Result.Failure(Constants.APP_NETWORK_UNAVAILABLE_REPEAT)
            }
            remoteStoreRepository.insertChat(chat).firstOrNull()
        }
        chatRepository.insertChat(chat)
        return Result.Success(chat)
    }

    private suspend fun Chat.withDefaultAiSettings(): Chat {
        val model = aiModel ?: (
                aiModelUseCase.get().firstOrNull() as? Result.Success
                )?.data?.takeIf { it.isNotBlank() } ?: SettingsConstants.AI_MODEL_DEFAULT
        val temp = temperature ?: (
                temperatureUseCase.get().firstOrNull() as? Result.Success
                )?.data ?: SettingsConstants.AI_TEMPERATURE_DEFAULT

        return copy(aiModel = model, temperature = temp)
    }
}
