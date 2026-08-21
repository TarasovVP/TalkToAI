package com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.authorisation

import com.vnteam.talktoai.data.network.Result
import com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.settings.RefreshTokenUseCase
import kotlinx.coroutines.flow.firstOrNull

class TokenRefreshManager(
    private val refreshTokenUseCase: RefreshTokenUseCase,
    private val exchangeAndStoreTokenUseCase: ExchangeAndStoreTokenUseCase,
) {
    // Returns new idToken on success, null on auth failure; throws TokenRefreshNetworkException on transient errors.
    suspend fun tryRefreshToken(): String? {
        val stored = (refreshTokenUseCase.get().firstOrNull() as? Result.Success)?.data
        if (stored.isNullOrEmpty()) return null
        return exchangeAndStoreTokenUseCase.execute(stored)
    }
}
