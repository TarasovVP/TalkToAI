package com.vnstudio.talktoai.presentation.screens.settings.settings_privacy_policy

import android.webkit.WebView
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import org.koin.androidx.compose.koinViewModel
import com.vnstudio.talktoai.CommonExtensions.EMPTY
import com.vnstudio.talktoai.CommonExtensions.initWebView

@Composable
fun SettingsPrivacyPolicyScreen(progressVisibilityState: MutableState<Boolean>) {

    val viewModel: SettingsPrivacyPolicyViewModel = koinViewModel()
    val privacyPolicyUrlState = remember { mutableStateOf(String.EMPTY) }

    LaunchedEffect(Unit) {
        progressVisibilityState.value = true
        viewModel.getAppLanguage()
    }
    val appLanguageState = viewModel.appLanguageLiveData.collectAsState()
    LaunchedEffect(appLanguageState.value) {
        appLanguageState.value?.let { lang ->
            viewModel.getPrivacyPolicy(lang)
        }
    }
    val privacyPolicyState = viewModel.privacyPolicyLiveData.collectAsState()
    LaunchedEffect(privacyPolicyState.value) {
        privacyPolicyState.value?.let { url ->
            privacyPolicyUrlState.value = url
        }
    }
    privacyPolicyState.value.takeIf { it.isNullOrEmpty().not() }?.let { url ->
        AppWebView(url, progressVisibilityState)
    }
}

@Composable
fun AppWebView(webUrl: String, progressVisibilityState: MutableState<Boolean>) {
    AndroidView(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        factory = { context ->
            WebView(context).apply {
                initWebView(webUrl) {
                    progressVisibilityState.value = false
                }
            }
        }
    )
}