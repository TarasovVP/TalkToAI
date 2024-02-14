package com.vnstudio.talktoai.presentation.screens.settings.settings_feedback

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.koinViewModel
import com.vnstudio.talktoai.CommonExtensions.EMPTY
import com.vnstudio.talktoai.R
import com.vnstudio.talktoai.domain.models.Feedback
import com.vnstudio.talktoai.domain.models.InfoMessage
import com.vnstudio.talktoai.presentation.components.ExceptionMessageHandler
import com.vnstudio.talktoai.presentation.components.PrimaryButton
import java.util.*

@Composable
fun SettingsFeedbackScreen(
    infoMessageState: MutableState<InfoMessage?>,
    progressVisibilityState: MutableState<Boolean>
) {

    val viewModel: SettingsFeedbackViewModel = koinViewModel()
    val inputValue = remember { mutableStateOf(TextFieldValue(String.EMPTY)) }

    val successFeedbackState = viewModel.successFeedbackLiveData.observeAsState()
    val feedbackSendSuccess = stringResource(id = R.string.settings_feedback_send_success)
    LaunchedEffect(successFeedbackState.value) {
        successFeedbackState.value?.let {
            infoMessageState.value = InfoMessage(message = feedbackSendSuccess)
        }
    }
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = stringResource(id = R.string.settings_feedback_title),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            textAlign = TextAlign.Center,
        )
        OutlinedTextField(
            value = inputValue.value,
            onValueChange = { inputValue.value = it },
            placeholder = { Text(text = stringResource(id = R.string.settings_feedback_hint)) },
            colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.White),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 16.dp),
            maxLines = 6,
            minLines = 6
        )
        PrimaryButton(
            text = stringResource(id = R.string.settings_feedback_send_button),
            isEnabled = inputValue.value.text.isNotEmpty(),
            modifier = Modifier
        ) {
            viewModel.insertFeedback(
                Feedback(
                    viewModel.currentUserEmail(),
                    inputValue.value.text,
                    Date().time
                )
            )
            inputValue.value = TextFieldValue(String.EMPTY)
        }
    }
    ExceptionMessageHandler(infoMessageState, viewModel.exceptionLiveData)
}