package com.vnstudio.talktoai.presentation.screens.onboarding.onboarding

import com.vnstudio.talktoai.domain.repositories.DataStoreRepository
import com.vnstudio.talktoai.domain.usecases.OnBoardingUseCase
import javax.inject.Inject

class OnBoardingUseCaseImpl @Inject constructor(
    private val dataStoreRepository: DataStoreRepository
): OnBoardingUseCase {

    override suspend fun setOnBoardingSeen(onBoardingSeen: Boolean) {
        return dataStoreRepository.setOnBoardingSeen(true)
    }
}