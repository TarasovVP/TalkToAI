package com.vnstudio.talktoai.presentation.screens.settings.settings_list

import com.vnstudio.talktoai.domain.sealed_classes.Result
import com.vnstudio.talktoai.domain.models.Feedback
import com.vnstudio.talktoai.domain.repositories.DataStoreRepository
import com.vnstudio.talktoai.domain.repositories.RealDataBaseRepository
import com.vnstudio.talktoai.domain.usecases.SettingsListUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SettingsListUseCaseImpl @Inject constructor(
    private val dataStoreRepository: DataStoreRepository,
    private val realDataBaseRepository: RealDataBaseRepository
): SettingsListUseCase {

    override suspend fun getAppLanguage(): Flow<String?> {
        return dataStoreRepository.getAppLang()
    }

    override fun insertFeedback(feedback: Feedback, result: (Result<Unit>) -> Unit) = realDataBaseRepository.insertFeedback(feedback) {
        result.invoke(it)
    }
}