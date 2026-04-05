package com.vnteam.talktoai

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.vnteam.talktoai.presentation.App
import com.vnteam.talktoai.utils.GoogleSignInInitializer
import com.vnteam.talktoai.utils.ShareUtils
import org.koin.android.ext.android.inject
import org.koin.compose.koinInject

class MainActivity : ComponentActivity() {

    private val googleSignInInitializer: GoogleSignInInitializer by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        googleSignInInitializer.initialize(this)
        setContent {
            koinInject<ShareUtils>().setActivityProvider { this }
            App(koinInject())
        }
    }
}