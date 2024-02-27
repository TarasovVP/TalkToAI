package com.vnstudio.talktoai

import android.app.Application
import com.google.android.gms.ads.MobileAds
import com.google.firebase.analytics.FirebaseAnalytics
import com.vnstudio.talktoai.CommonExtensions.registerForNetworkUpdates
import com.vnstudio.talktoai.di.appModule
import com.vnstudio.talktoai.domain.resources.getStringResourcesByLocale
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import java.util.Locale

class TalkToAIApp : Application() {

    var isNetworkAvailable: Boolean? = null
    var stringRes: Any? = null

    override fun onCreate() {
        super.onCreate()
        MobileAds.initialize(this)
        FirebaseAnalytics.getInstance(this)
        registerForNetworkUpdates { isAvailable ->
            isNetworkAvailable = isAvailable
        }
        startKoin {
            androidContext(this@TalkToAIApp)
            modules(appModule)
        }
        stringRes = getStringResourcesByLocale(Locale.getDefault().language)
    }
}