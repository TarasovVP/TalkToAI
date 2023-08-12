package com.vn.talktoai.presentation.settings

import com.vn.talktoai.domain.repositories.SettingsRepository
import com.vn.talktoai.domain.usecases.SettingsUseCase
import javax.inject.Inject

class SettingsUseCaseImpl @Inject constructor(private val settingsRepository: SettingsRepository) : SettingsUseCase {

    override suspend fun changeSettings() = settingsRepository.changeSettings()
}