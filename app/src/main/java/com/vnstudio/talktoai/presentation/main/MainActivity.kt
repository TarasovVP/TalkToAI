package com.vnstudio.talktoai.presentation.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.platform.ComposeView
import androidx.core.view.WindowCompat
import com.google.accompanist.insets.ProvideWindowInsets
import com.vnstudio.talktoai.presentation.AppContent
import com.vnstudio.talktoai.ui.theme.TalkToAITheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, true)
        setContentView(
            ComposeView(this).apply {
                setContent {
                    ProvideWindowInsets(consumeWindowInsets = false) {
                        TalkToAITheme {
                            AppContent()
                        }
                    }
                }
            }
        )
    }
}