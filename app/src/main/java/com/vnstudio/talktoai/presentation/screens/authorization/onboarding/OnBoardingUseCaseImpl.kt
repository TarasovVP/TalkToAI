package com.vnstudio.talktoai.presentation.screens.authorization.onboarding

import com.vnstudio.talktoai.domain.repositories.DataStoreRepository
import com.vnstudio.talktoai.domain.usecases.OnBoardingUseCase


class OnBoardingUseCaseImpl(
    private val dataStoreRepository: DataStoreRepository,
) : OnBoardingUseCase {

    override suspend fun setOnBoardingSeen(onBoardingSeen: Boolean) {
        return dataStoreRepository.setOnBoardingSeen(true)
    }
}