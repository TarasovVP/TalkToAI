package com.vnteam.talktoai.di

import com.vnteam.talktoai.di.aiModule
import com.vnteam.talktoai.di.authModule
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin

fun doInitKoin(): KoinApplication = startKoin {
    modules(authModule, aiModule, appModule, webModule)
}