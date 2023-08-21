package com.vnstudio.talktoai.data.repositoryimpl

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import com.vnstudio.talktoai.domain.repositories.DataStoreRepository
import com.vnstudio.talktoai.infrastructure.Constants.APP_LANG
import com.vnstudio.talktoai.infrastructure.Constants.APP_THEME
import com.vnstudio.talktoai.infrastructure.Constants.ON_BOARDING_SEEN
import com.vnstudio.talktoai.infrastructure.Constants.REVIEW_VOTE
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.take

class DataStoreRepositoryImpl(private val dataStore: DataStore<Preferences>) : DataStoreRepository {

    override suspend fun setOnBoardingSeen(isOnBoardingSeen: Boolean) {
        dataStore.edit { preferences ->
            preferences[booleanPreferencesKey(ON_BOARDING_SEEN)] = isOnBoardingSeen
        }
    }

    override suspend fun onBoardingSeen(): Flow<Boolean?> {
        return dataStore.data
            .map { preferences ->
                preferences[booleanPreferencesKey(ON_BOARDING_SEEN)]
            }.take(1)
    }

    override suspend fun setAppLang(appLang: String) {
        dataStore.edit { preferences ->
            preferences[stringPreferencesKey(APP_LANG)] = appLang
        }
    }

    override suspend fun getAppLang(): Flow<String?> {
        return dataStore.data
            .map { preferences ->
                preferences[stringPreferencesKey(APP_LANG)]
            }.take(1)
    }

    override suspend fun setAppTheme(appTheme: Int) {
        dataStore.edit { preferences ->
            preferences[intPreferencesKey(APP_THEME)] = appTheme
        }
    }

    override suspend fun getAppTheme(): Flow<Int?> {
        return dataStore.data
            .map { preferences ->
                preferences[intPreferencesKey(APP_THEME)]
            }.take(1)
    }

    override suspend fun setReviewVoted(isReviewVoted: Boolean) {
        dataStore.edit { preferences ->
            preferences[booleanPreferencesKey(REVIEW_VOTE)] = isReviewVoted
        }
    }

    override suspend fun reviewVoted(): Flow<Boolean?> {
        return dataStore.data
            .map { preferences ->
                preferences[booleanPreferencesKey(REVIEW_VOTE)]
            }.take(1)
    }

    override suspend fun clearDataByKeys(keys: List<Preferences.Key<*>>) {
            dataStore.edit { preferences ->
                keys.forEach { key ->
                    preferences.remove(key)
                }
            }
    }
}