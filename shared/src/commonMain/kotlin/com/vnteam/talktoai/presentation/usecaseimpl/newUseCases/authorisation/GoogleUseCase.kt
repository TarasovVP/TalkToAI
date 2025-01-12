package com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.authorisation

import com.vnteam.talktoai.domain.repositories.AuthRepository

class GoogleUseCase(private val authRepository: AuthRepository) {

    fun googleSignOut() {
        authRepository.googleSignOut()
    }

    fun googleSignIn() {
        authRepository.googleSignIn()
    }
}