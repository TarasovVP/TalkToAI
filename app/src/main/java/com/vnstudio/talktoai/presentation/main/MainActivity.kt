package com.vnstudio.talktoai.presentation.main

import android.content.Context
import android.content.ContextWrapper
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.ui.platform.ComposeView
import androidx.core.view.WindowCompat
import com.google.accompanist.insets.ProvideWindowInsets
import com.vnstudio.talktoai.CommonExtensions.safeSingleObserve
import com.vnstudio.talktoai.CommonExtensions.setAppLocale
import com.vnstudio.talktoai.di.DataStoreEntryPoint
import com.vnstudio.talktoai.presentation.AppContent
import com.vnstudio.talktoai.ui.theme.TalkToAITheme
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
        viewModel.getOnBoardingSeen()
        viewModel.onBoardingSeenLiveData.safeSingleObserve(this) { isOnboardingSeen ->
            val startDestination = when {
                isOnboardingSeen.not() -> "destination_onboarding_screen"
                viewModel.isLoggedInUser().not() -> "destination_login_screen"
                else -> "destination_chat_screen"
            }
            setContentView(
                ComposeView(this).apply {
                    setContent {
                        ProvideWindowInsets(consumeWindowInsets = false) {
                            TalkToAITheme {
                                AppContent(startDestination)
                            }
                        }
                    }
                }
            )
        }
    }

    override fun attachBaseContext(newBase: Context) {
        val dataStoreRepository = EntryPointAccessors.fromApplication( newBase, DataStoreEntryPoint::class.java ).dataStoreRepository
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