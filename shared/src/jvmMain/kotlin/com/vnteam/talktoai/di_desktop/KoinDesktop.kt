package com.vnteam.talktoai.di_desktop

import com.vnteam.talktoai.di.appModule
import org.koin.core.context.startKoin

fun doInitKoin() = startKoin {
    modules(appModule, desktopModule)
}