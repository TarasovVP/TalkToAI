package com.vnstudio.talktoai.presentation.onboarding.signup

import com.vnstudio.talktoai.domain.sealed_classes.Result
import com.vnstudio.talktoai.domain.models.CurrentUser
import com.vnstudio.talktoai.domain.repositories.AuthRepository
import com.vnstudio.talktoai.domain.repositories.RealDataBaseRepository
import com.vnstudio.talktoai.domain.usecases.SignUpUseCase
import javax.inject.Inject

class SignUpUseCaseImpl @Inject constructor(
    private val authRepository: AuthRepository,
    private val realDataBaseRepository: RealDataBaseRepository): SignUpUseCase {

    override fun fetchSignInMethodsForEmail(email: String, result: (Result<List<String>>) -> Unit) = authRepository.fetchSignInMethodsForEmail(email) { authResult ->
        result.invoke(authResult)
    }

    override fun createUserWithGoogle(idToken: String, result: (Result<Unit>) -> Unit) = authRepository.signInWithGoogle(idToken) { authResult ->
        result.invoke(authResult)
    }

    override fun createUserWithEmailAndPassword(email: String, password: String, result: (Result<String>) -> Unit) = authRepository.createUserWithEmailAndPassword(email, password) { authResult ->
        result.invoke(authResult)
    }

    override fun createCurrentUser(currentUser: CurrentUser, result: (Result<Unit>) -> Unit) = realDataBaseRepository.createCurrentUser(currentUser) { authResult ->
        result.invoke(authResult)
    }
}