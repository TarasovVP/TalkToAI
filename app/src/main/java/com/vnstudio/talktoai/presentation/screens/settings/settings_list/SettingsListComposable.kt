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
import java.util.*

@Composable
fun SettingsListScreen(infoMessageState: MutableState<InfoMessage?>, onNextScreen: (String) -> Unit) {

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
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        SettingsItem(
            stringResource(id = R.string.settings_chat),
            R.drawable.ic_settings_chat
        ) {
            onNextScreen.invoke(NavigationScreen.SettingsChatScreen().route)
        }
        SettingsItem(
            stringResource(id = R.string.settings_account),
            R.drawable.ic_settings_account
        ) {
            onNextScreen.invoke(NavigationScreen.SettingsAccountScreen().route)
        }
        SettingsItem(
            stringResource(id = R.string.settings_language),
            R.drawable.ic_settings_language,
            flagDrawable(appLanguageState.value)
        ) {
            onNextScreen.invoke(NavigationScreen.SettingsLanguageScreen().route)
        }
        SettingsItem(
            stringResource(id = R.string.settings_theme),
            R.drawable.ic_settings_theme
        ) {
            onNextScreen.invoke(NavigationScreen.SettingsThemeScreen().route)
        }
        SettingsItem(
            stringResource(id = R.string.settings_feedback),
            R.drawable.ic_settings_feedback
        ) {
            showFeedbackDialog.value = true
        }
        SettingsItem(
            stringResource(id = R.string.settings_privacy_policy),
            R.drawable.ic_settings_privacy
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
fun SettingsItem(title: String, mainIcon: Int, secondaryIcon: Int? = null, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp)
            .clickable {
                onClick.invoke()
            },
        elevation = 1.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Image(imageVector = ImageVector.vectorResource(id = mainIcon), contentDescription = title,
                modifier = Modifier
                .padding(end = 8.dp))
            Text(text = title, modifier = Modifier.weight(1f))
            secondaryIcon?.let { Image(imageVector = ImageVector.vectorResource(id = it), contentDescription = title) }
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