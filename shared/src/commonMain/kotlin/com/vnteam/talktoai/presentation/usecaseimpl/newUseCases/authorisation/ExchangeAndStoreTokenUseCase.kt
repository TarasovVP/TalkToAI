package com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.authorisation

import com.vnteam.talktoai.data.network.Result
import com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.settings.RefreshTokenUseCase

class ExchangeAndStoreTokenUseCase(
    private val exchangeTokenUseCase: ExchangeTokenUseCase,
    private val refreshTokenUseCase: RefreshTokenUseCase,
) {
    suspend fun execute(originalRefreshToken: String): String? {
        return when (val result = exchangeTokenUseCase.execute(originalRefreshToken)) {
            is Result.Success -> {
                result.data?.refreshToken?.let { refreshTokenUseCase.set(it) }
                result.data?.idToken
            }
            else -> {
                // Exchange failed — persist original token so next attempt can retry
                refreshTokenUseCase.set(originalRefreshToken)
                null
            }
        }
    }
}
