package com.vnstudio.talktoai.presentation.settings.settings_sign_up

import com.vnstudio.talktoai.domain.sealed_classes.Result
import com.vnstudio.talktoai.domain.models.CurrentUser
import com.vnstudio.talktoai.domain.repositories.AuthRepository
import com.vnstudio.talktoai.domain.repositories.DataStoreRepository
import com.vnstudio.talktoai.domain.repositories.RealDataBaseRepository
import com.vnstudio.talktoai.domain.usecases.SettingsSignUpUseCase
import javax.inject.Inject

class SettingsSignUpUseCaseImpl @Inject constructor(
    private val dataStoreRepository: DataStoreRepository,
    private val authRepository: AuthRepository,
    private val realDataBaseRepository: RealDataBaseRepository
): SettingsSignUpUseCase {

    override fun fetchSignInMethodsForEmail(email: String, result: (Result<List<String>>) -> Unit) = authRepository.fetchSignInMethodsForEmail(email) { authResult ->
        result.invoke(authResult)
    }

    override fun createUserWithGoogle(idToken: String, result: (Result<Unit>) -> Unit) = authRepository.signInWithGoogle(idToken) { authResult ->
        result.invoke(authResult)
    }

    override fun createUserWithEmailAndPassword(email: String, password: String, result: (Result<String>) -> Unit) = authRepository.createUserWithEmailAndPassword(email, password) { authResult ->
        result.invoke(authResult)
    }

    override fun signInWithEmailAndPassword(email: String, password: String, result: (Result<Unit>) -> Unit) = authRepository.signInWithEmailAndPassword(email, password) { authResult ->
        result.invoke(authResult)
    }

    override fun createCurrentUser(currentUser: CurrentUser, result: (Result<Unit>) -> Unit) = realDataBaseRepository.createCurrentUser(currentUser) { authResult ->
        result.invoke(authResult)
    }

    override fun updateCurrentUser(currentUser: CurrentUser, result: (Result<Unit>) -> Unit) = realDataBaseRepository.updateCurrentUser(currentUser) { authResult ->
        result.invoke(authResult)
    }
}