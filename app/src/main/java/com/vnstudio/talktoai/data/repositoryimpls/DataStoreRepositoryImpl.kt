package com.vnstudio.talktoai.data.repositoryimpls

import com.vnstudio.talktoai.data.local.PreferenceManager
import com.vnstudio.talktoai.domain.repositories.DataStoreRepository
import com.vnstudio.talktoai.infrastructure.Constants.APP_LANG
import com.vnstudio.talktoai.infrastructure.Constants.APP_THEME
import com.vnstudio.talktoai.infrastructure.Constants.ON_BOARDING_SEEN
import com.vnstudio.talktoai.infrastructure.Constants.REVIEW_VOTE
import kotlinx.coroutines.flow.Flow

class DataStoreRepositoryImpl(private val preferenceManager: PreferenceManager) :
    DataStoreRepository {

    override suspend fun setOnBoardingSeen(isOnBoardingSeen: Boolean) {
        preferenceManager.setBoolean(ON_BOARDING_SEEN, isOnBoardingSeen)
    }

    override suspend fun onBoardingSeen(): Flow<Boolean?> {
        return preferenceManager.getBoolean(ON_BOARDING_SEEN)
    }

    override suspend fun setAppLang(appLang: String) {
        preferenceManager.setString(APP_LANG, appLang)
    }

    override suspend fun getAppLang(): Flow<String?> {
        return preferenceManager.getString(APP_LANG)
    }

    override suspend fun setAppTheme(appTheme: Int) {
        preferenceManager.setInt(APP_THEME, appTheme)
    }

    override suspend fun getAppTheme(): Flow<Int?> {
        return preferenceManager.getInt(APP_THEME)
    }

    override suspend fun setReviewVoted(isReviewVoted: Boolean) {
        preferenceManager.setBoolean(REVIEW_VOTE, isReviewVoted)
    }

    override suspend fun reviewVoted(): Flow<Boolean?> {
        return preferenceManager.getBoolean(REVIEW_VOTE)
    }

    override suspend fun clearDataByKeys(keys: List<String>) {
        preferenceManager.clearPreferences(keys)
    }
}