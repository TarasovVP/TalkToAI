package com.vnteam.talktoai.presentation.viewmodels.settings

import com.vnteam.talktoai.Constants
import com.vnteam.talktoai.data.network.Result
import com.vnteam.talktoai.data.network.getDataOrNull
import com.vnteam.talktoai.domain.models.RemoteUser
import com.vnteam.talktoai.domain.usecase.execute
import com.vnteam.talktoai.presentation.uistates.SettingsSignUpUIState
import com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.authorisation.SignInWithEmailAndPasswordUseCase
import com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.chats.GetChatsUseCase
import com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.messages.GetMessagesUseCase
import com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.remote.InsertRemoteUserUseCase
import com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.remote.SyncRemoteUserUseCase
import com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.remote.UpdateRemoteUserUseCase
import com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.settings.IdTokenUseCase
import com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.settings.UidUseCase
import com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.settings.UserEmailUseCase
import com.vnteam.talktoai.presentation.viewmodels.BaseViewModel
import com.vnteam.talktoai.utils.NetworkState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull

class SettingsLoginViewModel(
    private val networkState: NetworkState,
    private val signInWithEmailAndPasswordUseCase: SignInWithEmailAndPasswordUseCase,
    private val getChatsUseCase: GetChatsUseCase,
    private val getMessagesUseCase: GetMessagesUseCase,
    private val insertRemoteUserUseCase: InsertRemoteUserUseCase,
    private val updateRemoteUserUseCase: UpdateRemoteUserUseCase,
    private val idTokenUseCase: IdTokenUseCase,
    private val userEmailUseCase: UserEmailUseCase,
    private val uidUseCase: UidUseCase,
    private val syncRemoteUserUseCase: SyncRemoteUserUseCase,
) : BaseViewModel() {

    private val _uiState = MutableStateFlow(SettingsSignUpUIState())
    val uiState = _uiState.asStateFlow()

    fun signInWithEmailAndPassword(email: String, password: String) {
        launchWithErrorHandling {
            when (val result = signInWithEmailAndPasswordUseCase.execute(Pair(email, password))) {
                is Result.Success -> {
                    idTokenUseCase.set(result.data?.idToken.orEmpty())
                    userEmailUseCase.set(result.data?.email.orEmpty())
                    uidUseCase.set(result.data?.localId.orEmpty())
                    updateUIState(SettingsSignUpUIState(successAuthorisation = true))
                }

                is Result.Failure -> onError(Exception(result.errorMessage))
                is Result.Loading -> Unit
            }
        }
    }

    fun createRemoteUser(isExistUser: Boolean) {
        launchWithErrorHandling {
            val chats = getChatsUseCase.execute().getDataOrNull()
            val messages = getMessagesUseCase.execute().getDataOrNull()
            val remoteUser = RemoteUser().apply {
                this.chats.addAll(chats.orEmpty())
                this.messages.addAll(messages.orEmpty())
            }
            updateUIState(SettingsSignUpUIState(remoteUser = Pair(isExistUser, remoteUser)))
        }
    }

    fun insertRemoteCurrentUser(remoteUser: RemoteUser) {
        launchWithErrorHandling {
            if (!networkState.isNetworkAvailable()) {
                onError(Exception(Constants.APP_NETWORK_UNAVAILABLE_REPEAT))
                return@launchWithErrorHandling
            }
            val result = insertRemoteUserUseCase.execute(remoteUser).firstOrNull()
            if (result is Result.Success) {
                syncRemoteUserUseCase.execute()
                updateUIState(SettingsSignUpUIState(successRemoteUser = true))
            }
        }
    }

    fun updateRemoteCurrentUser(remoteUser: RemoteUser) {
        launchWithErrorHandling {
            if (!networkState.isNetworkAvailable()) {
                onError(Exception(Constants.APP_NETWORK_UNAVAILABLE_REPEAT))
                return@launchWithErrorHandling
            }
            val result = updateRemoteUserUseCase.execute(remoteUser).firstOrNull()
            if (result is Result.Success) {
                syncRemoteUserUseCase.execute()
                updateUIState(SettingsSignUpUIState(successRemoteUser = true))
            }
        }
    }

    fun syncRemoteUser() {
        launchWithErrorHandling {
            if (!networkState.isNetworkAvailable()) {
                onError(Exception(Constants.APP_NETWORK_UNAVAILABLE_REPEAT))
                return@launchWithErrorHandling
            }
            syncRemoteUserUseCase.execute()
            updateUIState(SettingsSignUpUIState(successRemoteUser = true))
        }
    }

    private fun updateUIState(newUIState: SettingsSignUpUIState) {
        _uiState.value = newUIState
    }
}
