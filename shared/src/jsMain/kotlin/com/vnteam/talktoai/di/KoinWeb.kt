package com.vnteam.talktoai.di

import org.koin.core.KoinApplication
import org.koin.core.context.startKoin

fun doInitKoin(): KoinApplication = startKoin {
    modules(authModule, aiModule, settingsModule, appModule, webModule)
}