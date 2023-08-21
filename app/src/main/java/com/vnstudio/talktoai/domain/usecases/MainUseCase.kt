package com.vnstudio.talktoai.domain.usecases

import com.vnstudio.talktoai.domain.models.CurrentUser
import kotlinx.coroutines.flow.Flow
import com.vnstudio.talktoai.data.network.Result

interface  MainUseCase {

    suspend fun getOnBoardingSeen(): Flow<Boolean?>

    fun getCurrentUser(result: (Result<CurrentUser>) -> Unit)

    suspend fun setReviewVoted(isReviewVoted: Boolean)
}