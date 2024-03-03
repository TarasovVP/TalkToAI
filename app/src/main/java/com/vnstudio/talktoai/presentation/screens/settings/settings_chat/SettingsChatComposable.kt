package com.vnstudio.talktoai.presentation.screens.settings.settings_chat

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.vnstudio.talktoai.domain.models.ScreenState
import com.vnstudio.talktoai.presentation.components.ExceptionMessageHandler
import com.vnstudio.talktoai.presentation.components.PrimaryButton
import org.koin.androidx.compose.koinViewModel

@Composable
fun SettingsChatContent(
    screenState: ScreenState,
) {

    val viewModel: SettingsChatViewModel = koinViewModel()

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "SettingsChatScreen")
        PrimaryButton(text = "Click", modifier = Modifier, onClick = {})
    }
    ExceptionMessageHandler(screenState.infoMessageState, viewModel.exceptionLiveData)
}