package com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.authorisation

import com.vnteam.talktoai.data.network.Result
import com.vnteam.talktoai.data.network.auth.request.ChangePasswordBody
import com.vnteam.talktoai.data.network.auth.response.ChangePasswordResponse
import com.vnteam.talktoai.domain.repositories.AuthRepository
import com.vnteam.talktoai.domain.usecase.UseCase

class ChangePasswordUseCase(
    private val repository: AuthRepository
) : UseCase<Pair<String, String>, Result<ChangePasswordResponse>> {

    override suspend fun execute(params: Pair<String, String>): Result<ChangePasswordResponse> {
        // TODO implement current password check
        //val idToken = preferencesRepository.getIdToken().firstOrNull()
        // TODO implement idToken
        val body = ChangePasswordBody("idToken", params.second)
        return repository.changePassword(body)
    }
}