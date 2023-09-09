package com.vnstudio.talktoai.presentation.screens

import android.content.Context
import android.content.ContextWrapper
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.core.view.WindowCompat
import com.google.accompanist.insets.ProvideWindowInsets
import com.vnstudio.talktoai.CommonExtensions.setAppLocale
import com.vnstudio.talktoai.di.DataStoreEntryPoint
import com.vnstudio.talktoai.presentation.screens.main.AppContent
import com.vnstudio.talktoai.presentation.screens.main.MainViewModel
import com.vnstudio.talktoai.presentation.theme.Primary50
import com.vnstudio.talktoai.presentation.theme.TalkToAITheme
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.EntryPointAccessors
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import java.util.*

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    val viewModel: MainViewModel by viewModels()

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

    override fun attachBaseContext(newBase: Context) {
        val dataStoreRepository = EntryPointAccessors.fromApplication(
            newBase,
            DataStoreEntryPoint::class.java
        ).dataStoreRepository
        val appTheme = runBlocking {
            dataStoreRepository.getAppTheme().first()
        } ?: AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
        AppCompatDelegate.setDefaultNightMode(appTheme)
        val appLang = runBlocking {
            dataStoreRepository.getAppLang().first()
        } ?: Locale.getDefault().language
        super.attachBaseContext(ContextWrapper(newBase.setAppLocale(appLang)))
    }
}