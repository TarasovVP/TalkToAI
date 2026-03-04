package com.vnteam.talktoai

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.vnteam.talktoai.presentation.App
import com.vnteam.talktoai.utils.ShareUtils
import org.koin.compose.koinInject

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val lifecycle = lifecycle
        println("googleTAG MainActivity onCreate lifecycle: ${lifecycle.currentState}")
        setContent {
            koinInject<ShareUtils>().setActivityProvider { this }
            App(koinInject())
        }
    }
}