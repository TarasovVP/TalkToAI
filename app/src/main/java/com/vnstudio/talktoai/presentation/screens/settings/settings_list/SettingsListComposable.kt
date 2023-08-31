package com.vnstudio.talktoai.presentation.screens.settings.settings_list

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.vnstudio.talktoai.CommonExtensions.EMPTY
import com.vnstudio.talktoai.R
import com.vnstudio.talktoai.domain.models.Feedback
import com.vnstudio.talktoai.domain.models.InfoMessage
import com.vnstudio.talktoai.domain.sealed_classes.NavigationScreen
import com.vnstudio.talktoai.flagDrawable
import com.vnstudio.talktoai.presentation.components.DrawerItem
import com.vnstudio.talktoai.presentation.components.ExceptionMessageHandler
import com.vnstudio.talktoai.presentation.components.FeedbackDialog
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
        DrawerItem(
            stringResource(id = R.string.settings_chat),
            R.drawable.ic_settings_chat,
            currentRouteState == NavigationScreen.SettingsChatScreen().route
        ) {
            onNextScreen.invoke(NavigationScreen.SettingsChatScreen().route)
        }
        DrawerItem(
            stringResource(id = R.string.settings_account),
            R.drawable.ic_settings_account,
            currentRouteState == NavigationScreen.SettingsAccountScreen().route
        ) {
            onNextScreen.invoke(NavigationScreen.SettingsAccountScreen().route)
        }
        DrawerItem(
            stringResource(id = R.string.settings_language),
            R.drawable.ic_settings_language,
            currentRouteState == NavigationScreen.SettingsLanguageScreen().route,
            LocalConfiguration.current.locales.flagDrawable()
        ) {
            onNextScreen.invoke(NavigationScreen.SettingsLanguageScreen().route)
        }
        DrawerItem(
            stringResource(id = R.string.settings_theme),
            R.drawable.ic_settings_theme,
            currentRouteState == NavigationScreen.SettingsThemeScreen().route
        ) {
            onNextScreen.invoke(NavigationScreen.SettingsThemeScreen().route)
        }
        DrawerItem(
            stringResource(id = R.string.settings_feedback),
            R.drawable.ic_settings_feedback,
            currentRouteState == NavigationScreen.SettingsPrivacyPolicyScreen().route
        ) {
            showFeedbackDialog.value = true
        }
        DrawerItem(
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

