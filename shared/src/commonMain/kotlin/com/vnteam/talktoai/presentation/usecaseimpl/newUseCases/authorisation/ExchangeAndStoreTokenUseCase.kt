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
            is Result.Failure -> {
                if (result.statusCode == null) {
                    // No HTTP status means a network/IO error — let callers distinguish from auth failure
                    throw TokenRefreshNetworkException(result.errorMessage)
                }
                // HTTP auth error (e.g. 400 TOKEN_EXPIRED) — token is invalid
                null
            }
            is Result.Loading -> null
        }
    }
}

class TokenRefreshNetworkException(message: String?) : Exception(message)
