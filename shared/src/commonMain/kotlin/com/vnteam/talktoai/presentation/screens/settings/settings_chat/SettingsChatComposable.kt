package com.vnteam.talktoai.presentation.screens.settings.settings_chat

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vnteam.talktoai.Res
import com.vnteam.talktoai.SettingsConstants
import com.vnteam.talktoai.ic_delete
import com.vnteam.talktoai.presentation.ui.components.PasswordTextField
import com.vnteam.talktoai.presentation.ui.components.PrimaryButton
import com.vnteam.talktoai.presentation.ui.resources.LocalStringResources
import com.vnteam.talktoai.presentation.LocalScreenState
import com.vnteam.talktoai.presentation.ui.theme.Neutral500
import com.vnteam.talktoai.presentation.uimodels.screen.AppMessage
import com.vnteam.talktoai.presentation.updateScreenState
import com.vnteam.talktoai.presentation.viewmodels.settings.SettingsChatViewModel
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsChatContent() {

    val viewModel: SettingsChatViewModel = koinViewModel()
    updateScreenState(viewModel.progressVisibilityState.collectAsState().value)

    val stringRes = LocalStringResources.current
    val aiModel = viewModel.aiModel.collectAsState()
    val availableModels = viewModel.availableModels.collectAsState()
    val temperature = viewModel.temperature.collectAsState()
    val hasChanges = viewModel.hasChanges.collectAsState()
    val savedApiKey = viewModel.savedApiKey.collectAsState()

    val localScreenState = LocalScreenState.current
    LaunchedEffect(Unit) {
        viewModel.settingsSaved.collect {
            localScreenState.value = localScreenState.value.copy(
                appMessage = AppMessage(message = stringRes.SETTINGS_CHAT_SAVED)
            )
        }
    }

    val dropdownExpanded = remember { mutableStateOf(false) }
    val apiKeyState = remember { mutableStateOf(TextFieldValue("")) }

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

        PasswordTextField(
            inputValue = apiKeyState,
            placeHolder = stringRes.SETTINGS_CHAT_API_KEY_HINT
        )

        if (savedApiKey.value.isNotEmpty()) {
            SavedApiKeyRow(savedApiKey.value) { viewModel.clearApiKey() }
        }

        PrimaryButton(
            text = stringRes.SETTINGS_CHAT_SAVE,
            isEnabled = apiKeyState.value.text.isNotEmpty() || hasChanges.value,
            modifier = Modifier.padding(top = 16.dp)
        ) {
            if (apiKeyState.value.text.isNotEmpty()) {
                viewModel.onApiKeyChanged(apiKeyState.value.text)
            }
            viewModel.saveSettings()
            apiKeyState.value = TextFieldValue("")
        }
    }
}

@Composable
private fun SavedApiKeyRow(apiKey: String, onDelete: () -> Unit) {
    val masked = if (apiKey.length <= 4) "*".repeat(apiKey.length)
    else "****" + apiKey.takeLast(4)

    Row(
        modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = masked,
            fontSize = 14.sp,
            color = Neutral500,
            modifier = Modifier.weight(1f)
        )
        IconButton(onClick = onDelete, modifier = Modifier.size(36.dp)) {
            Icon(
                painter = painterResource(Res.drawable.ic_delete),
                contentDescription = null,
                tint = Color.Red,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}
