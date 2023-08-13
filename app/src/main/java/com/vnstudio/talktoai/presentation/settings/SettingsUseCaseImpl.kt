package com.vnstudio.talktoai.presentation.settings

import com.vnstudio.talktoai.domain.repositories.SettingsRepository
import com.vnstudio.talktoai.domain.usecases.SettingsUseCase
import javax.inject.Inject

class SettingsUseCaseImpl @Inject constructor(private val settingsRepository: SettingsRepository) : SettingsUseCase {

    override suspend fun changeSettings() = settingsRepository.changeSettings()
}