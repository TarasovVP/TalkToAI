package com.vnteam.talktoai.presentation.viewmodels.authorisation

import com.vnteam.talktoai.CommonExtensions.EMPTY
import com.vnteam.talktoai.data.network.onSuccess
import com.vnteam.talktoai.domain.usecase.execute
import com.vnteam.talktoai.presentation.uistates.LoginUIState
import com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.authorisation.ExchangeTokenUseCase
import com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.authorisation.ResetPasswordUseCase
import com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.authorisation.SignInAnonymouslyUseCase
import com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.authorisation.SignInWithEmailAndPasswordUseCase
import com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.settings.IdTokenUseCase
import com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.settings.UidUseCase
import com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.settings.UserEmailUseCase
import com.vnteam.talktoai.presentation.viewmodels.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class LoginViewModel(
    private val signInWithEmailAndPasswordUseCase: SignInWithEmailAndPasswordUseCase,
    private val signInAnonymouslyUseCase: SignInAnonymouslyUseCase,
    private val resetPasswordUseCase: ResetPasswordUseCase,
    private val userEmailUseCase: UserEmailUseCase,
    private val idTokenUseCase: IdTokenUseCase,
    private val uidUseCase: UidUseCase,
    private val exchangeTokenUseCase: ExchangeTokenUseCase,
) : BaseViewModel() {

    private val _uiState = MutableStateFlow(LoginUIState())
    val uiState = _uiState.asStateFlow()

    fun signInWithEmailAndPassword(email: String, password: String) {
        launchWithErrorHandling {
            when (val result = signInWithEmailAndPasswordUseCase.execute(Pair(email, password))) {
                is com.vnteam.talktoai.data.network.Result.Success -> {
                    hideProgress()
                    val firebaseIdToken = exchangeForFirebaseToken(result.data?.refreshToken)
                    val finalToken = firebaseIdToken ?: result.data?.idToken.orEmpty()
                    idTokenUseCase.set(finalToken)
                    userEmailUseCase.set(result.data?.email.orEmpty())
                    uidUseCase.set(result.data?.localId.orEmpty())
                    updateUIState(LoginUIState(emailSignInSuccess = true))
                }
                is com.vnteam.talktoai.data.network.Result.Failure -> onError(Exception(result.errorMessage))
                is com.vnteam.talktoai.data.network.Result.Loading -> showProgress()
            }
        }
    }

    fun signInAnonymously() {
        launchWithErrorHandling {
            when (val result = signInAnonymouslyUseCase.execute()) {
                is com.vnteam.talktoai.data.network.Result.Success -> {
                    hideProgress()
                    val firebaseIdToken = exchangeForFirebaseToken(result.data?.refreshToken)
                    idTokenUseCase.set(firebaseIdToken ?: result.data?.idToken.orEmpty())
                    userEmailUseCase.set(String.EMPTY)
                    uidUseCase.set(result.data?.localId.orEmpty())
                    updateUIState(LoginUIState(anonymousSignInSuccess = true))
                }
                is com.vnteam.talktoai.data.network.Result.Failure -> onError(Exception(result.errorMessage))
                is com.vnteam.talktoai.data.network.Result.Loading -> showProgress()
            }
        }
    }

    fun resetPassword(email: String) {
        launchWithResult {
            resetPasswordUseCase.execute(email).onSuccess {
                updateUIState(LoginUIState(successPasswordReset = true))
            }
        }
    }

    private suspend fun exchangeForFirebaseToken(refreshToken: String?): String? {
        if (refreshToken.isNullOrEmpty()) {
            return null
        }
        val result = exchangeTokenUseCase.execute(refreshToken)
        val idToken = (result as? com.vnteam.talktoai.data.network.Result.Success)?.data?.idToken
        return idToken
    }

    private fun updateUIState(newUIState: LoginUIState) {
        _uiState.value = newUIState
    }
}
