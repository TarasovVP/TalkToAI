package com.vnstudio.talktoai.presentation.screens.settings.settings_list

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.vnstudio.talktoai.CommonExtensions.EMPTY
import com.vnstudio.talktoai.R
import com.vnstudio.talktoai.domain.models.Feedback
import com.vnstudio.talktoai.domain.models.InfoMessage
import com.vnstudio.talktoai.domain.sealed_classes.NavigationScreen
import com.vnstudio.talktoai.infrastructure.Constants.APP_LANG_RU
import com.vnstudio.talktoai.infrastructure.Constants.APP_LANG_UK
import com.vnstudio.talktoai.presentation.components.FeedbackDialog
import com.vnstudio.talktoai.presentation.screens.base.ExceptionMessageHandler
import com.vnstudio.talktoai.presentation.theme.Neutral50
import com.vnstudio.talktoai.presentation.theme.Primary800
import com.vnstudio.talktoai.presentation.theme.Primary900
import java.util.*

@Composable
fun SettingsList(
    currentRouteState: String?,
    modifier: Modifier,
    infoMessageState: MutableState<InfoMessage?>,
    onNextScreen: (String) -> Unit
) {

    val viewModel: SettingsListViewModel = hiltViewModel()
    val showFeedbackDialog = remember { mutableStateOf(false) }
    val inputValue = remember { mutableStateOf(TextFieldValue(String.EMPTY)) }

    LaunchedEffect(Unit) {
        viewModel.getAppLanguage()
    }
    val appLanguageState = viewModel.appLanguageLiveData.observeAsState()
    val successFeedbackState = viewModel.successFeedbackLiveData.observeAsState()
    val feedbackSendSuccess = stringResource(id = R.string.settings_feedback_send_success)
    LaunchedEffect(successFeedbackState.value) {
        successFeedbackState.value?.let {
            infoMessageState.value = InfoMessage(message = feedbackSendSuccess)
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 24.dp)
    ) {
        SettingsItem(
            stringResource(id = R.string.settings_chat),
            R.drawable.ic_settings_chat,
            currentRouteState == NavigationScreen.SettingsChatScreen().route
        ) {
            onNextScreen.invoke(NavigationScreen.SettingsChatScreen().route)
        }
        SettingsItem(
            stringResource(id = R.string.settings_account),
            R.drawable.ic_settings_account,
            currentRouteState == NavigationScreen.SettingsAccountScreen().route
        ) {
            onNextScreen.invoke(NavigationScreen.SettingsAccountScreen().route)
        }
        SettingsItem(
            stringResource(id = R.string.settings_language),
            R.drawable.ic_settings_language,
            currentRouteState == NavigationScreen.SettingsLanguageScreen().route,
            flagDrawable(appLanguageState.value)
        ) {
            onNextScreen.invoke(NavigationScreen.SettingsLanguageScreen().route)
        }
        SettingsItem(
            stringResource(id = R.string.settings_theme),
            R.drawable.ic_settings_theme,
            currentRouteState == NavigationScreen.SettingsThemeScreen().route
        ) {
            onNextScreen.invoke(NavigationScreen.SettingsThemeScreen().route)
        }
        SettingsItem(
            stringResource(id = R.string.settings_feedback),
            R.drawable.ic_settings_feedback,
            currentRouteState == NavigationScreen.SettingsPrivacyPolicyScreen().route
        ) {
            showFeedbackDialog.value = true
        }
        SettingsItem(
            stringResource(id = R.string.settings_privacy_policy),
            R.drawable.ic_settings_privacy,
            currentRouteState == NavigationScreen.SettingsPrivacyPolicyScreen().route
        ) {
            onNextScreen.invoke(NavigationScreen.SettingsPrivacyPolicyScreen().route)
        }
    }

    FeedbackDialog(
        inputValue = inputValue,
        showDialog = showFeedbackDialog,
        onDismiss = {
            showFeedbackDialog.value = false
                    },
        onConfirmationClick = { feedback ->
            showFeedbackDialog.value = false
            viewModel.insertFeedback(Feedback(viewModel.currentUserEmail(), feedback, Date().time))
        }
    )
    ExceptionMessageHandler(infoMessageState, viewModel.exceptionLiveData)
}

@Composable
fun SettingsItem(title: String, mainIcon: Int, isCurrent: Boolean, secondaryIcon: Int? = null, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp)
            .let { modifier ->
                if (isCurrent.not()) {
                    modifier.clickable {
                        onClick.invoke()
                    }
                } else {
                    modifier
                }
            },
        backgroundColor = if (isCurrent) Primary800 else Primary900,
        elevation = 1.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Image(imageVector = ImageVector.vectorResource(id = mainIcon), contentDescription = title,
                modifier = Modifier
                .padding(8.dp))
            Text(text = title, color = Neutral50, modifier = Modifier.weight(1f).padding(vertical = 8.dp))
            secondaryIcon?.let { Image(imageVector = ImageVector.vectorResource(id = it), contentDescription = title, modifier = Modifier
                .padding(8.dp)) }
        }
    }
}

fun flagDrawable(appLang: String?): Int {
    return when (appLang) {
        APP_LANG_UK -> R.drawable.ic_flag_ua
        APP_LANG_RU -> R.drawable.ic_flag_ru
        else -> R.drawable.ic_flag_en
    }
}