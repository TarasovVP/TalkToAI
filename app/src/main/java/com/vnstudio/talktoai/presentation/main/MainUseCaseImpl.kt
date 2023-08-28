package com.vnstudio.talktoai.presentation.main

import com.vnstudio.talktoai.domain.sealed_classes.Result
import com.vnstudio.talktoai.domain.models.CurrentUser
import com.vnstudio.talktoai.domain.repositories.AuthRepository
import com.vnstudio.talktoai.domain.repositories.DataStoreRepository
import com.vnstudio.talktoai.domain.repositories.RealDataBaseRepository
import com.vnstudio.talktoai.domain.usecases.MainUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MainUseCaseImpl @Inject constructor(private val authRepository: AuthRepository, private val dataStoreRepository: DataStoreRepository, private val realDataBaseRepository: RealDataBaseRepository) : MainUseCase {

    override suspend fun getOnBoardingSeen(): Flow<Boolean?> {
        return dataStoreRepository.onBoardingSeen()
    }

    override fun isLoggedInUser(): Boolean {
        return authRepository.isLoggedInUser()
    }

    override fun getCurrentUser(result: (Result<CurrentUser>) -> Unit) = realDataBaseRepository.getCurrentUser { operationResult ->
        result.invoke(operationResult)
    }

    override suspend fun setReviewVoted(isReviewVoted: Boolean) = dataStoreRepository.setReviewVoted(isReviewVoted)
}