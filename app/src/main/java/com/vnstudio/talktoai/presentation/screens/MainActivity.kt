package com.vnstudio.talktoai.presentation.screens

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.platform.ComposeView
import androidx.core.view.WindowCompat
import com.google.accompanist.insets.ProvideWindowInsets
import com.vnstudio.talktoai.presentation.screens.main.AppContent
import com.vnstudio.talktoai.presentation.screens.main.MainViewModel
import com.vnstudio.talktoai.presentation.theme.TalkToAITheme

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

    //TODO uncomment
    /*override fun attachBaseContext(newBase: Context) {
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
    }*/
}