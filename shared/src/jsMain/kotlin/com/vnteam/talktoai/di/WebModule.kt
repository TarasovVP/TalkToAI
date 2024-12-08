package com.vnteam.talktoai.di

import com.vnteam.talktoai.data.database.DatabaseDriverFactory
import com.vnteam.talktoai.data.local.PreferencesFactory
import com.vnteam.talktoai.utils.NetworkState
import org.koin.dsl.module

val webModule = module {
    single {
        DatabaseDriverFactory()
    }
    single {
        PreferencesFactory()
    }
    single {
        NetworkState()
    }
}