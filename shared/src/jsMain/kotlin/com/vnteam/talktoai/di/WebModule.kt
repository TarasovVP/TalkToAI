package com.vnteam.talktoai.di

import com.vnteam.talktoai.data.database.DatabaseDriverFactory
import com.vnteam.talktoai.data.local.PreferencesFactory
import com.vnteam.talktoai.data.sdk.GoogleAuthHandler
import com.vnteam.talktoai.utils.AnimationUtils
import com.vnteam.talktoai.utils.NetworkState
import com.vnteam.talktoai.utils.ShareUtils
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
    single {
        GoogleAuthHandler()
    }
    single {
        AnimationUtils()
    }
    single {
        ShareUtils()
    }
}