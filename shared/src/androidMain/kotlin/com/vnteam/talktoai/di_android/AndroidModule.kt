package com.vnteam.talktoai.di_android

import com.vnteam.talktoai.data.database.DatabaseDriverFactory
import com.vnteam.talktoai.data.local.PreferencesFactory
import com.vnteam.talktoai.utils.NetworkState
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
}