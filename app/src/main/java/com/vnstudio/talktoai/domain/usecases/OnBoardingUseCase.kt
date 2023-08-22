package com.vnstudio.talktoai.domain.usecases

interface OnBoardingUseCase {

    suspend fun setOnBoardingSeen(onBoardingSeen: Boolean)
}