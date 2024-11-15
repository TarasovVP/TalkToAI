package presentation.screens.settings.settings_chat

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.vnteam.talktoai.presentation.uimodels.screen.ScreenState
import com.vnteam.talktoai.presentation.viewmodels.SettingsChatViewModel
import components.ExceptionMessageHandler
import components.PrimaryButton
import org.koin.compose.viewmodel.koinViewModel
import resources.LocalStringResources

@Composable
fun SettingsChatContent(
    screenState: ScreenState,
) {

    val viewModel: SettingsChatViewModel = koinViewModel()

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = LocalStringResources.current.SETTINGS_CHAT)
        PrimaryButton(text = LocalStringResources.current.BUTTON_OK, modifier = Modifier, onClick = {})
    }
    ExceptionMessageHandler(screenState.infoMessageState, viewModel.exceptionLiveData)
}