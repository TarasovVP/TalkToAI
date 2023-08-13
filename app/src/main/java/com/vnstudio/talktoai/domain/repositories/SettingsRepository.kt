package com.vnstudio.talktoai.domain.repositories

interface SettingsRepository {

    suspend fun changeSettings()
}