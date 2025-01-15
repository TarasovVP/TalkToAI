package com.vnteam.talktoai.presentation.viewmodels

import com.vnteam.talktoai.data.network.onSuccess
import com.vnteam.talktoai.domain.models.RemoteUser
import com.vnteam.talktoai.presentation.uistates.SignUpUIState
import com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.authorisation.CreateUserWithEmailAndPasswordUseCase
import com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.authorisation.CreateUserWithGoogleUseCase
import com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.authorisation.FetchSignInMethodsForEmailUseCase
import com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.authorisation.GoogleUseCase
import com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.remote.InsertRemoteUserUseCase
import com.vnteam.talktoai.utils.NetworkState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class SignUpViewModel(
    private val networkState: NetworkState,
    private val fetchSignInMethodsForEmailUseCase: FetchSignInMethodsForEmailUseCase,
    private val createUserWithGoogleUseCase: CreateUserWithGoogleUseCase,
    private val createUserWithEmailAndPasswordUseCase: CreateUserWithEmailAndPasswordUseCase,
    private val insertRemoteUserUseCase: InsertRemoteUserUseCase,
    private val googleUseCase: GoogleUseCase
) : BaseViewModel() {

    private val _uiState = MutableStateFlow(SignUpUIState())
    val uiState: StateFlow<SignUpUIState> = _uiState.asStateFlow()

    fun fetchSignInMethodsForEmail(email: String, idToken: String? = null) {
        launchWithNetworkCheck(networkState) {
            fetchSignInMethodsForEmailUseCase.execute(email).onSuccess { result ->
                when {
                    result.isNullOrEmpty().not() -> updateUIState(SignUpUIState(accountExist = true))
                    idToken.isNullOrEmpty() -> updateUIState(SignUpUIState(createEmailAccount = true))
                    else -> updateUIState(SignUpUIState(createGoogleAccount = idToken))
                }
            }
        }
    }

    fun createUserWithGoogle(idToken: String) {
        launchWithNetworkCheck(networkState) {
            createUserWithGoogleUseCase.execute(idToken).onSuccess {
                updateUIState(SignUpUIState(successSignUp = true))
            }
        }
    }

    fun createUserWithEmailAndPassword(email: String, password: String) {
        launchWithNetworkCheck(networkState) {
            createUserWithEmailAndPasswordUseCase.execute(Pair(email, password)).onSuccess {
                updateUIState(SignUpUIState(successSignUp = true))
            }
        }
    }

    fun insertRemoteUser(remoteUser: RemoteUser) {
        launchWithNetworkCheck(networkState) {
            insertRemoteUserUseCase.execute(remoteUser).onSuccess {
                updateUIState(SignUpUIState(createCurrentUser = true))
            }
        }
    }

    private fun updateUIState(newUIState: SignUpUIState) {
        _uiState.value = newUIState
    }

    fun googleSignOut() {
        googleUseCase.googleSignOut()
    }

    fun googleSignIn() {
        googleUseCase.googleSignIn()
    }
}