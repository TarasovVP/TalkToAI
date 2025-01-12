package com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.settings

import com.vnteam.talktoai.data.network.Result
import com.vnteam.talktoai.domain.repositories.RealDataBaseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

class PrivacyPolicyUseCase(private val realDataBaseRepository: RealDataBaseRepository) {

    fun getPrivacyPolicy(appLang: String): Flow<Result<String>> {
        return realDataBaseRepository.getPrivacyPolicy(appLang).map {
            Result.Success(it)
        }.catch {
            Result.Failure(it.message)
        }
    }
}