package com.vnstudio.talktoai.presentation.settings.settings_list

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import com.vnstudio.talktoai.CommonExtensions.EMPTY
import com.vnstudio.talktoai.R
import com.vnstudio.talktoai.domain.models.Feedback
import com.vnstudio.talktoai.domain.models.InfoMessage
import com.vnstudio.talktoai.domain.sealed_classes.NavigationScreen
import com.vnstudio.talktoai.presentation.base.ExceptionMessageHandler
import com.vnstudio.talktoai.presentation.components.PrimaryButton
import com.vnstudio.talktoai.ui.theme.Primary500
import java.util.*

@Composable
fun SettingsListScreen(infoMessageState: MutableState<InfoMessage?>, onNextScreen: (String) -> Unit) {

    val viewModel: SettingsListViewModel = hiltViewModel()
    val showFeedbackDialog = remember { mutableStateOf(false) }
    val inputValue = remember { mutableStateOf(TextFieldValue(String.EMPTY)) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        SettingsItem(
            "Hастройки чата",
            R.drawable.ic_settings_chat
        ) {
            onNextScreen.invoke(NavigationScreen.SettingsChatScreen().route)
        }
        SettingsItem(
            "Ваш аккаунт",
            R.drawable.ic_settings_account
        ) {
            onNextScreen.invoke(NavigationScreen.SettingsAccountScreen().route)
        }
        SettingsItem(
            "Язык",
            R.drawable.ic_settings_language
        ) {
            onNextScreen.invoke(NavigationScreen.SettingsLanguageScreen().route)
        }
        SettingsItem(
            "Выбор темы",
            R.drawable.ic_settings_theme
        ) {
            onNextScreen.invoke(NavigationScreen.SettingsThemeScreen().route)
        }
        SettingsItem(
            "Написать разработчику",
            R.drawable.ic_settings_feedback
        ) {
            showFeedbackDialog.value = true
        }
        SettingsItem(
            "Политика конфиденциальности",
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
            viewModel.insertFeedback(Feedback(viewModel.firebaseAuth.currentUser?.email.orEmpty(), feedback, Date().time))
        }
    )
    ExceptionMessageHandler(infoMessageState, viewModel.exceptionLiveData)
}

@Composable
fun SettingsItem(title: String, icon: Int, onClick: () -> Unit) {
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
            Image(imageVector = ImageVector.vectorResource(id = icon), contentDescription = title,
                modifier = Modifier
                .padding(end = 8.dp))
            Text(text = title)
        }
    }
}

@Composable
fun FeedbackDialog(inputValue: MutableState<TextFieldValue>, showDialog: MutableState<Boolean>, onDismiss: () -> Unit, onConfirmationClick: (String) -> Unit) {
    if (showDialog.value) {
        Column {
            Dialog(
                onDismissRequest = onDismiss,
                content = {
                    Column(modifier = Modifier
                        .wrapContentSize()
                        .border(1.dp, Primary500, shape = RoundedCornerShape(16.dp))
                        .background(color = Color.White, shape = RoundedCornerShape(16.dp)),
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(text = "Усли у вас есть предложения или замечания вы можете их описать ниже", modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp), textAlign = TextAlign.Center,)
                        FeedbackTextField(inputValue)
                        PrimaryButton(text = "Отправить", isEnabled = inputValue.value.text.isNotEmpty(), modifier = Modifier) {
                            onConfirmationClick.invoke(inputValue.value.text)
                        }
                    }
                }
            )
        }
    }
}

@Composable
fun FeedbackTextField(
    inputValue: MutableState<TextFieldValue>
) {
    OutlinedTextField(
        value = inputValue.value,
        onValueChange = { inputValue.value = it },
        placeholder = { Text(text = "Сообщение") },
        colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.White),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        maxLines = 6,
        minLines = 6
    )
}