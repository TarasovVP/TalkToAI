package com.vnteam.talktoai.presentation.usecaseimpl

import com.vnteam.talktoai.domain.repositories.PreferencesRepository
import com.vnteam.talktoai.domain.usecase.OnBoardingUseCase
import kotlinx.coroutines.flow.Flow

class OnBoardingUseCaseImpl(
    private val preferencesRepository: PreferencesRepository,
) : OnBoardingUseCase {

    override suspend fun setOnBoardingSeen(onBoardingSeen: Boolean) {
        return preferencesRepository.setOnBoardingSeen(true)
    }

    override suspend fun getIsBoardingSeen(): Flow<Boolean?> {
        TODO("Not yet implemented")
    }
}