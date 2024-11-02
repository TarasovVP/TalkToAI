package com.vnteam.talktoai

import android.app.Application
import com.vnteam.talktoai.di_android.doInitKoin

class AppApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        doInitKoin(this)
    }
}