package com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.settings

import com.vnteam.talktoai.data.asPreferenceResult
import com.vnteam.talktoai.data.network.Result
import com.vnteam.talktoai.domain.repositories.PreferencesRepository
import com.vnteam.talktoai.domain.usecase.DataUseCase
import kotlinx.coroutines.flow.Flow

class GlobalContextUseCase(private val preferencesRepository: PreferencesRepository) :
    DataUseCase<String, Flow<Result<String?>>> {

    override fun get() = preferencesRepository.getGlobalSystemContext().asPreferenceResult()

    override suspend fun set(params: String) {
        preferencesRepository.setGlobalSystemContext(params)
    }
}
