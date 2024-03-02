package com.vnstudio.talktoai.presentation.screens.settings.settings_feedback

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.vnstudio.talktoai.CommonExtensions.EMPTY
import com.vnstudio.talktoai.domain.models.Feedback
import com.vnstudio.talktoai.domain.models.InfoMessage
import com.vnstudio.talktoai.presentation.components.ExceptionMessageHandler
import com.vnstudio.talktoai.presentation.components.PrimaryButton
import com.vnstudio.talktoai.presentation.components.stringRes
import kotlinx.datetime.Clock
import org.koin.androidx.compose.koinViewModel

@Composable
fun SettingsFeedbackContent(
    infoMessageState: MutableState<InfoMessage?>,
    progressVisibilityState: MutableState<Boolean>
) {

    val viewModel: SettingsFeedbackViewModel = koinViewModel()
    val inputValue = remember { mutableStateOf(TextFieldValue(String.EMPTY)) }

    val successFeedbackState = viewModel.successFeedbackLiveData.collectAsState()
    val feedbackSendSuccess = stringRes().SETTINGS_FEEDBACK_SEND_SUCCESS
    LaunchedEffect(successFeedbackState.value) {
        if (successFeedbackState.value) {
            infoMessageState.value = InfoMessage(message = feedbackSendSuccess)
        }
    }
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = stringRes().SETTINGS_FEEDBACK_TITLE,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            textAlign = TextAlign.Center,
        )
        OutlinedTextField(
            value = inputValue.value,
            onValueChange = { inputValue.value = it },
            placeholder = { Text(text = stringRes().SETTINGS_FEEDBACK_HINT) },
            colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.White),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 16.dp),
            maxLines = 6,
            minLines = 6
        )
        PrimaryButton(
            text = stringRes().SETTINGS_FEEDBACK_SEND_BUTTON,
            isEnabled = inputValue.value.text.isNotEmpty(),
            modifier = Modifier
        ) {
            viewModel.insertFeedback(
                Feedback(
                    viewModel.currentUserEmail(),
                    inputValue.value.text,
                    Clock.System.now().toEpochMilliseconds()
                )
            )
            inputValue.value = TextFieldValue(String.EMPTY)
        }
    }
    ExceptionMessageHandler(infoMessageState, viewModel.exceptionLiveData)
}