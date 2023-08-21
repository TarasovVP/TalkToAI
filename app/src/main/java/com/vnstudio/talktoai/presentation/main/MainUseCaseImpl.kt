package com.vnstudio.talktoai.presentation.main

import com.vnstudio.talktoai.data.network.Result
import com.vnstudio.talktoai.domain.models.CurrentUser
import com.vnstudio.talktoai.domain.repositories.DataStoreRepository
import com.vnstudio.talktoai.domain.repositories.RealDataBaseRepository
import com.vnstudio.talktoai.domain.usecases.MainUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MainUseCaseImpl @Inject constructor(private val dataStoreRepository: DataStoreRepository, private val realDataBaseRepository: RealDataBaseRepository) : MainUseCase {

    override suspend fun getOnBoardingSeen(): Flow<Boolean?> {
        return dataStoreRepository.onBoardingSeen()
    }


    override fun getCurrentUser(result: (Result<CurrentUser>) -> Unit) = realDataBaseRepository.getCurrentUser { operationResult ->
        result.invoke(operationResult)
    }

    override suspend fun setReviewVoted(isReviewVoted: Boolean) = dataStoreRepository.setReviewVoted(isReviewVoted)
}