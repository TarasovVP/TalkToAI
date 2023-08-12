package com.vn.talktoai.domain.repositories

interface SettingsRepository {

    suspend fun changeSettings()
}