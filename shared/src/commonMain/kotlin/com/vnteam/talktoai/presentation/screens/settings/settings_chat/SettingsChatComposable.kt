package com.vnteam.talktoai.presentation.screens.settings.settings_chat

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.vnteam.talktoai.SettingsConstants
import com.vnteam.talktoai.presentation.ui.components.PasswordTextField
import com.vnteam.talktoai.presentation.ui.components.PrimaryButton
import com.vnteam.talktoai.presentation.ui.resources.LocalStringResources
import com.vnteam.talktoai.presentation.uimodels.screen.AppMessage
import com.vnteam.talktoai.presentation.updateScreenState
import com.vnteam.talktoai.presentation.viewmodels.settings.SettingsChatViewModel
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsChatContent() {

    val viewModel: SettingsChatViewModel = koinViewModel()
    updateScreenState(viewModel.progressVisibilityState.collectAsState().value)

    val stringRes = LocalStringResources.current
    val aiModel = viewModel.aiModel.collectAsState()
    val apiKey = viewModel.apiKey.collectAsState()
    val availableModels = viewModel.availableModels.collectAsState()
    val temperature = viewModel.temperature.collectAsState()

    val settingsSaved = viewModel.settingsSaved.collectAsState()
    if (settingsSaved.value) {
        updateScreenState(appMessage = AppMessage(message = stringRes.SETTINGS_CHAT_SAVED))
    }

    val dropdownExpanded = remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = stringRes.SETTINGS_CHAT_MODEL_TITLE,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        ExposedDropdownMenuBox(
            expanded = dropdownExpanded.value,
            onExpandedChange = { dropdownExpanded.value = it },
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
        ) {
            OutlinedTextField(
                value = aiModel.value,
                onValueChange = {},
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = dropdownExpanded.value) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                ),
                textStyle = TextStyle(color = Color.Black),
                modifier = Modifier.fillMaxWidth().menuAnchor()
            )
            ExposedDropdownMenu(
                expanded = dropdownExpanded.value,
                onDismissRequest = { dropdownExpanded.value = false }
            ) {
                availableModels.value.forEach { model ->
                    DropdownMenuItem(
                        text = { Text(text = model, color = Color.Black) },
                        onClick = {
                            viewModel.onModelSelected(model)
                            dropdownExpanded.value = false
                        }
                    )
                }
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth().padding(bottom = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = stringRes.SETTINGS_CHAT_TEMPERATURE_TITLE)
            Text(text = temperature.value.toString())
        }
        Slider(
            value = temperature.value,
            onValueChange = { viewModel.onTemperatureChanged(it) },
            valueRange = SettingsConstants.AI_TEMPERATURE_MIN..SettingsConstants.AI_TEMPERATURE_MAX,
            steps = 19,
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
        )

        Text(
            text = stringRes.SETTINGS_CHAT_API_KEY_TITLE,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        val apiKeyState = remember(apiKey.value) { mutableStateOf(TextFieldValue(apiKey.value)) }
        PasswordTextField(
            inputValue = apiKeyState,
            placeHolder = stringRes.SETTINGS_CHAT_API_KEY_HINT
        )

        PrimaryButton(
            text = stringRes.SETTINGS_CHAT_SAVE,
            modifier = Modifier.padding(top = 16.dp)
        ) {
            viewModel.onApiKeyChanged(apiKeyState.value.text)
            viewModel.saveSettings()
        }
    }
}
