package com.vnteam.talktoai.di_desktop

import com.vnteam.talktoai.di.appModule
import com.vnteam.talktoai.di.aiModule
import com.vnteam.talktoai.di.authModule
import org.koin.core.context.startKoin

fun doInitKoin() = startKoin {
    modules(authModule, aiModule, appModule, desktopModule)
}