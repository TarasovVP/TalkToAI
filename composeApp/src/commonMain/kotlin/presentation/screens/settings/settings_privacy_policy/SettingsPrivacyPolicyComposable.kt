package presentation.screens.settings.settings_privacy_policy

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.vnteam.talktoai.CommonExtensions.EMPTY
import com.vnteam.talktoai.Constants.PRIVACY_POLICY
import com.vnteam.talktoai.presentation.ui.resources.LocalStringResources
import com.vnteam.talktoai.presentation.uimodels.screen.ScreenState
import com.vnteam.talktoai.presentation.viewmodels.SettingsPrivacyPolicyViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SettingsPrivacyPolicyContent(screenState: ScreenState) {

    val viewModel: SettingsPrivacyPolicyViewModel = koinViewModel()
    val privacyPolicyUrlState = remember { mutableStateOf(String.EMPTY) }

    LaunchedEffect(Unit) {
        screenState.isProgressVisible = true
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
        AppWebView(url, mutableStateOf(screenState.isProgressVisible))
    }
}

@Composable
fun AppWebView(webUrl: String, progressVisibilityState: MutableState<Boolean>) {
    // TODO implement webview
    /*AndroidView(
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
    )*/
}