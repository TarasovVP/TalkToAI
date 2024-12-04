package com.vnstudio.talktoai.presentation.screens.settings.settings_privacy_policy

import android.webkit.WebView
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.vnstudio.talktoai.CommonExtensions.EMPTY
import com.vnstudio.talktoai.CommonExtensions.initWebView
import com.vnstudio.talktoai.domain.models.ScreenState
import com.vnstudio.talktoai.infrastructure.Constants.PRIVACY_POLICY
import com.vnstudio.talktoai.presentation.components.stringRes
import org.koin.androidx.compose.koinViewModel

@Composable
fun SettingsPrivacyPolicyContent(screenState: ScreenState) {

    val viewModel: SettingsPrivacyPolicyViewModel = koinViewModel()
    val privacyPolicyUrlState = remember { mutableStateOf(String.EMPTY) }

    LaunchedEffect(Unit) {
        screenState.progressVisibilityState.value = true
        viewModel.getAppLanguage()
    }
    val appLanguageState = viewModel.appLanguageLiveData.collectAsState()
    LaunchedEffect(appLanguageState.value) {
        appLanguageState.value.let { lang ->
            viewModel.getPrivacyPolicy(lang)
        }
    }
    val privacyPolicyState = viewModel.privacyPolicyLiveData.collectAsState()
    val stringRes = LocalStringResources.current
    LaunchedEffect(privacyPolicyState.value) {
        privacyPolicyState.value.let { url ->
            privacyPolicyUrlState.value =
                if (url == PRIVACY_POLICY) stringRes.PRIVACY_POLICY else url
        }
    }
    privacyPolicyState.value.takeIf { it.isNotEmpty() }?.let { url ->
        AppWebView(url, screenState.progressVisibilityState)
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