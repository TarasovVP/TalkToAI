package com.vnstudio.talktoai.presentation.screens.settings.settings_chat

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import org.koin.androidx.compose.koinViewModel
import com.vnstudio.talktoai.domain.models.InfoMessage
import com.vnstudio.talktoai.presentation.components.ExceptionMessageHandler
import com.vnstudio.talktoai.presentation.components.PrimaryButton

@Composable
fun SettingsChatScreen(
    infoMessageState: MutableState<InfoMessage?>,
    progressVisibilityState: MutableState<Boolean>,
    onClick: () -> Unit
) {

    val viewModel: SettingsChatViewModel = koinViewModel()

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "SettingsChatScreen")
        PrimaryButton(text = "Click", modifier = Modifier, onClick = onClick)
    }
    ExceptionMessageHandler(infoMessageState, viewModel.exceptionLiveData)
}