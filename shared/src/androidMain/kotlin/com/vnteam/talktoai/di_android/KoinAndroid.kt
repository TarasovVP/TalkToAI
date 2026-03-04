package com.vnteam.talktoai.di_android

import android.app.Application
import com.vnteam.talktoai.di.appModule
import com.vnteam.talktoai.di.authModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

fun doInitKoin(application: Application) = startKoin {
    androidLogger()
    androidContext(application)
    modules(authModule + appModule + androidModule)
}