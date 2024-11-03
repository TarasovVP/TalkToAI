package com.vnteam.talktoai.di_desktop

import com.vnteam.talktoai.data.database.DatabaseDriverFactory
import com.vnteam.talktoai.data.local.PreferencesFactory
import org.koin.dsl.module

val desktopModule = module {
    single {
        DatabaseDriverFactory()
    }
    single {
        PreferencesFactory()
    }
}