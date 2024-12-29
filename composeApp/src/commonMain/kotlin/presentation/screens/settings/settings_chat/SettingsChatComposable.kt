package presentation.screens.settings.settings_chat

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import com.vnteam.talktoai.presentation.ui.components.PrimaryButton
import com.vnteam.talktoai.presentation.ui.resources.LocalStringResources
import com.vnteam.talktoai.presentation.uimodels.screen.AppMessage
import com.vnteam.talktoai.presentation.viewmodels.SettingsChatViewModel
import org.koin.compose.viewmodel.koinViewModel
import presentation.updateScreenState

@Composable
fun SettingsChatContent() {

    val viewModel: SettingsChatViewModel = koinViewModel()

    updateScreenState(viewModel.progressVisibilityState.collectAsState().value, AppMessage(isMessageError = true, message = viewModel.exceptionMessage.collectAsState().value))

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = LocalStringResources.current.SETTINGS_CHAT)
        PrimaryButton(
            text = LocalStringResources.current.BUTTON_OK,
            modifier = Modifier,
            onClick = {
                viewModel.testProgressVisibilityChange()
            })
    }
}