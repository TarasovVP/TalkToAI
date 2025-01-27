package com.vnteam.talktoai.di_android

import com.vnteam.talktoai.data.database.DatabaseDriverFactory
import com.vnteam.talktoai.data.local.PreferencesFactory
import com.vnteam.talktoai.data.sdk.GoogleAuthHandler
import com.vnteam.talktoai.utils.AnimationUtils
import com.vnteam.talktoai.utils.NetworkState
import com.vnteam.talktoai.utils.ShareUtils
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val androidModule = module {
    single {
        DatabaseDriverFactory(androidContext())
    }
    single {
        PreferencesFactory(androidContext())
    }
    single {
        NetworkState(androidContext())
    }
    single {
        GoogleAuthHandler(androidContext())
    }
    single {
        AnimationUtils()
    }
    single {
        ShareUtils()
    }
}