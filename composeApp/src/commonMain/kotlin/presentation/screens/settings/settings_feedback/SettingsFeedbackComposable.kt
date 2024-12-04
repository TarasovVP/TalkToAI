package presentation.screens.settings.settings_feedback

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.vnteam.talktoai.CommonExtensions.EMPTY
import com.vnteam.talktoai.domain.models.Feedback
import com.vnteam.talktoai.domain.models.InfoMessage
import com.vnteam.talktoai.presentation.ui.components.ExceptionMessageHandler
import com.vnteam.talktoai.presentation.ui.components.PrimaryButton
import com.vnteam.talktoai.presentation.ui.resources.LocalStringResources
import com.vnteam.talktoai.presentation.uimodels.screen.ScreenState
import com.vnteam.talktoai.presentation.viewmodels.SettingsFeedbackViewModel
import kotlinx.datetime.Clock
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SettingsFeedbackContent(
    screenState: ScreenState
) {

    val viewModel: SettingsFeedbackViewModel = koinViewModel()
    val inputValue = remember { mutableStateOf(TextFieldValue(String.EMPTY)) }

    val successFeedbackState = viewModel.successFeedbackLiveData.collectAsState()
    val feedbackSendSuccess = LocalStringResources.current.SETTINGS_FEEDBACK_SEND_SUCCESS
    LaunchedEffect(successFeedbackState.value) {
        if (successFeedbackState.value) {
            screenState.infoMessageState.value = InfoMessage(message = feedbackSendSuccess)
        }
    }
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = LocalStringResources.current.SETTINGS_FEEDBACK_TITLE,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            textAlign = TextAlign.Center,
        )
        OutlinedTextField(
            value = inputValue.value,
            onValueChange = { inputValue.value = it },
            placeholder = { Text(text = LocalStringResources.current.SETTINGS_FEEDBACK_HINT) },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 16.dp),
            maxLines = 6,
            minLines = 6
        )
        PrimaryButton(
            text = LocalStringResources.current.SETTINGS_FEEDBACK_SEND_BUTTON,
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
    ExceptionMessageHandler(screenState.infoMessageState, viewModel.exceptionLiveData)
}