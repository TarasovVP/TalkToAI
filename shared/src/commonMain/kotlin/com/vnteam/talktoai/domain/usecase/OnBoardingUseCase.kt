package com.vnteam.talktoai.domain.usecase

import kotlinx.coroutines.flow.Flow

interface OnBoardingUseCase {

    suspend fun setOnBoardingSeen(onBoardingSeen: Boolean)

    suspend fun getIsBoardingSeen(): Flow<Boolean?>
}