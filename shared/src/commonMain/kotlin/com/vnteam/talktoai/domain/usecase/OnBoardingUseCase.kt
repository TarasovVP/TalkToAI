package com.vnteam.talktoai.domain.usecase

import com.vnteam.talktoai.data.network.NetworkResult
import kotlinx.coroutines.flow.Flow

interface OnBoardingUseCase {

    suspend fun setOnBoardingSeen(onBoardingSeen: Boolean)

    suspend fun getIsBoardingSeen(): Flow<NetworkResult<Boolean?>>
}