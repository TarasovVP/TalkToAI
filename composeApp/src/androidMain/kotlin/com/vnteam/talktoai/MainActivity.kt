package com.vnteam.talktoai

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.vnteam.talktoai.data.sdk.GoogleAuthHandler
import com.vnteam.talktoai.utils.ShareUtils
import org.koin.compose.koinInject
import presentation.App

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            koinInject<ShareUtils>().setActivityProvider { this }
            koinInject<GoogleAuthHandler>().setActivityProvider { this }
            App(koinInject())
        }
    }
}