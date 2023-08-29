package com.vnstudio.talktoai.presentation.settings.settings_chat

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.vnstudio.talktoai.domain.models.InfoMessage
import com.vnstudio.talktoai.presentation.base.ExceptionMessageHandler
import com.vnstudio.talktoai.presentation.components.PrimaryButton

@Composable
fun SettingsChatScreen(infoMessageState: MutableState<InfoMessage?>, onClick: () -> Unit, ) {

    val viewModel: SettingsChatViewModel = hiltViewModel()

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "SettingsChatScreen")
        PrimaryButton(text = "Click", modifier = Modifier, onClick = onClick)
    }
    ExceptionMessageHandler(infoMessageState, viewModel.exceptionLiveData)
}