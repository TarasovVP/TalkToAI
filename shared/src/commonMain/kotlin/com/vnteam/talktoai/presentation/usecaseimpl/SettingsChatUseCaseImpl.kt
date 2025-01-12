package com.vnteam.talktoai.presentation.usecaseimpl

import com.vnteam.talktoai.data.network.Result
import com.vnteam.talktoai.domain.repositories.PreferencesRepository
import com.vnteam.talktoai.domain.repositories.RealDataBaseRepository
import com.vnteam.talktoai.domain.usecase.SettingsChatUseCase
import kotlinx.coroutines.flow.Flow


class SettingsChatUseCaseImpl(
    private val realDataBaseRepository: RealDataBaseRepository,
    private val preferencesRepository: PreferencesRepository,
    /*private val firebaseAuth: FirebaseAuth,*/
) : SettingsChatUseCase {

    override suspend fun getChatSettings(): Flow<Result<Boolean?>> {
        return preferencesRepository.getIsBoardingSeen()
    }
}