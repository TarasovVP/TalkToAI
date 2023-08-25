package com.vnstudio.talktoai.presentation.settings.settings_chat

import com.google.firebase.auth.FirebaseAuth

import kotlinx.coroutines.flow.Flow
import com.vnstudio.talktoai.domain.repositories.DataStoreRepository
import com.vnstudio.talktoai.domain.repositories.RealDataBaseRepository
import com.vnstudio.talktoai.domain.usecases.SettingsBlockerUseCase
import javax.inject.Inject

class SettingsChatUseCaseImpl @Inject constructor(
    private val realDataBaseRepository: RealDataBaseRepository,
    private val dataStoreRepository: DataStoreRepository,
    private val firebaseAuth: FirebaseAuth
): SettingsBlockerUseCase {

    override suspend fun getBlockerTurnOn(): Flow<Boolean?> {
        return dataStoreRepository.onBoardingSeen()
    }
}