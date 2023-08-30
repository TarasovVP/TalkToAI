package com.vnstudio.talktoai.presentation.screens.settings.settings_privacy_policy

import android.webkit.WebView
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import com.vnstudio.talktoai.CommonExtensions.EMPTY
import com.vnstudio.talktoai.CommonExtensions.initWebView

@Composable
fun SettingsPrivacyPolicyScreen() {

    val viewModel: SettingsPrivacyPolicyViewModel = hiltViewModel()
    val privacyPolicyUrlState = remember { mutableStateOf(String.EMPTY) }

    LaunchedEffect(Unit) {
        viewModel.getAppLanguage()
    }
    val appLanguageState = viewModel.appLanguageLiveData.observeAsState()
    LaunchedEffect(appLanguageState.value) {
        appLanguageState.value?.let { lang ->
            viewModel.getPrivacyPolicy(lang)
        }
    }
    val privacyPolicyState = viewModel.privacyPolicyLiveData.observeAsState()
    LaunchedEffect(privacyPolicyState.value) {
        privacyPolicyState.value?.let { url ->
            privacyPolicyUrlState.value = url
        }
    }
    privacyPolicyState.value.takeIf { it.isNullOrEmpty().not()}?.let { url ->
        AppWebView(url)
    }
}

@Composable
fun AppWebView(webUrl: String) {
    AndroidView(
        modifier = Modifier
            .fillMaxSize().verticalScroll(rememberScrollState()),
        factory = { context ->
            WebView(context).apply {
                initWebView(webUrl) {

                }
            }
        }
    )
}