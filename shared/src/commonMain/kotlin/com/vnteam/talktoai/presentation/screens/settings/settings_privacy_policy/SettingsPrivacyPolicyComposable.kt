package com.vnteam.talktoai.presentation.screens.settings.settings_privacy_policy

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.vnteam.talktoai.Constants.PRIVACY_POLICY
import com.vnteam.talktoai.presentation.ui.resources.LocalStringResources
import com.vnteam.talktoai.presentation.viewmodels.settings.SettingsPrivacyPolicyViewModel
import org.koin.compose.viewmodel.koinViewModel
import com.vnteam.talktoai.presentation.updateScreenState

@Composable
fun SettingsPrivacyPolicyContent() {
    val viewModel: SettingsPrivacyPolicyViewModel = koinViewModel()
    updateScreenState(viewModel.progressVisibilityState.collectAsState().value)

    LaunchedEffect(Unit) {
        viewModel.getAppLanguage()
    }
    val appLanguageState = viewModel.appLanguageLiveData.collectAsState()
    LaunchedEffect(appLanguageState.value) {
        viewModel.getPrivacyPolicy(appLanguageState.value)
    }

    val privacyPolicyState = viewModel.privacyPolicyLiveData.collectAsState()
    val stringRes = LocalStringResources.current

    val contentToShow = when {
        privacyPolicyState.value == PRIVACY_POLICY -> stringRes.PRIVACY_POLICY
        privacyPolicyState.value.isNotEmpty() -> privacyPolicyState.value
        else -> null
    }

    contentToShow?.let { content ->
        if (content.startsWith("http")) {
            AppWebView(content) {}
        } else {
            PrivacyPolicyTextContent(content)
        }
    }
}

@Composable
fun PrivacyPolicyTextContent(htmlContent: String) {
    val plainText = remember(htmlContent) { htmlContent.stripHtml() }
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        item {
            Text(text = plainText)
        }
    }
}

private fun String.stripHtml(): String {
    return this
        .replace(Regex("<!\\[CDATA\\["), "")
        .replace(Regex("]]>"), "")
        .replace(Regex("<style[^>]*>.*?</style>", setOf(RegexOption.IGNORE_CASE, RegexOption.DOT_MATCHES_ALL)), "")
        .replace(Regex("<head[^>]*>.*?</head>", setOf(RegexOption.IGNORE_CASE, RegexOption.DOT_MATCHES_ALL)), "")
        .replace(Regex("<br\\s*/?>", RegexOption.IGNORE_CASE), "\n")
        .replace(Regex("<p[^>]*>", RegexOption.IGNORE_CASE), "\n")
        .replace(Regex("</p>", RegexOption.IGNORE_CASE), "\n")
        .replace(Regex("<li[^>]*>", RegexOption.IGNORE_CASE), "\n• ")
        .replace(Regex("<[^>]+>"), "")
        .replace(Regex("&[a-z]+;"), " ")
        .replace(Regex("\n{3,}"), "\n\n")
        .trim()
}

@Composable
fun AppWebView(webUrl: String, onWebViewInit: () -> Unit) {
    // TODO implement platform WebView for remote URLs
}
