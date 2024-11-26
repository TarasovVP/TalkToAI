package com.vnteam.talktoai.presentation.usecaseimpl

import com.vnteam.talktoai.domain.repositories.PreferencesRepository
import com.vnteam.talktoai.domain.usecase.OnBoardingUseCase
import kotlinx.coroutines.flow.Flow

class OnBoardingUseCaseImpl(
    private val preferencesRepository: PreferencesRepository,
) : OnBoardingUseCase {

    override suspend fun setOnBoardingSeen(onBoardingSeen: Boolean) {
        preferencesRepository.setOnBoardingSeen(onBoardingSeen)
    }

    override suspend fun getIsBoardingSeen(): Flow<Boolean?> {
        return preferencesRepository.getIsBoardingSeen()
    }
}