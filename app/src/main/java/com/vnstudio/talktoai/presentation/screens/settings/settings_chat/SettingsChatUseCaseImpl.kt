package com.vnstudio.talktoai.presentation.screens.settings.settings_chat

import com.google.firebase.auth.FirebaseAuth
import com.vnstudio.talktoai.domain.repositories.DataStoreRepository
import com.vnstudio.talktoai.domain.repositories.RealDataBaseRepository
import com.vnstudio.talktoai.domain.usecases.SettingsChatUseCase
import kotlinx.coroutines.flow.Flow


class SettingsChatUseCaseImpl(
    private val realDataBaseRepository: RealDataBaseRepository,
    private val dataStoreRepository: DataStoreRepository,
    private val firebaseAuth: FirebaseAuth,
) : SettingsChatUseCase {

    override suspend fun getChatSettings(): Flow<Boolean?> {
        return dataStoreRepository.onBoardingSeen()
    }
}