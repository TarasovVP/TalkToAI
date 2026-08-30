package com.vnteam.talktoai.presentation.screens.settings.settings_chat

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.vnteam.talktoai.SettingsConstants
import com.vnteam.talktoai.domain.enums.AiProviderType
import com.vnteam.talktoai.presentation.LocalScreenState
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
    val aiProvider = viewModel.aiProvider.collectAsState()
    val aiModel = viewModel.aiModel.collectAsState()
    val availableModels = viewModel.availableModels.collectAsState()
    val temperature = viewModel.temperature.collectAsState()
    val hasChanges = viewModel.hasChanges.collectAsState()
    val globalContext = viewModel.globalContext.collectAsState()

    val localScreenState = LocalScreenState.current
    LaunchedEffect(Unit) {
        viewModel.settingsSaved.collect {
            localScreenState.value = localScreenState.value.copy(
                appMessage = AppMessage(message = stringRes.SETTINGS_CHAT_SAVED)
            )
        }
    }

    val providerDropdownExpanded = remember { mutableStateOf(false) }
    val dropdownExpanded = remember { mutableStateOf(false) }
    val globalContextState = remember(globalContext.value) { mutableStateOf(globalContext.value) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = stringRes.SETTINGS_CHAT_PROVIDER_TITLE,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        ExposedDropdownMenuBox(
            expanded = providerDropdownExpanded.value,
            onExpandedChange = { providerDropdownExpanded.value = it },
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
        ) {
            val fieldContainerColor = MaterialTheme.colorScheme.tertiaryContainer
            val fieldContentColor = MaterialTheme.colorScheme.onTertiaryContainer
            OutlinedTextField(
                value = aiProvider.value.name,
                onValueChange = {},
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = providerDropdownExpanded.value) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = fieldContainerColor,
                    unfocusedContainerColor = fieldContainerColor,
                ),
                textStyle = TextStyle(color = fieldContentColor),
                modifier = Modifier.fillMaxWidth().menuAnchor()
            )
            ExposedDropdownMenu(
                expanded = providerDropdownExpanded.value,
                onDismissRequest = { providerDropdownExpanded.value = false }
            ) {
                AiProviderType.entries.forEach { provider ->
                    DropdownMenuItem(
                        text = { Text(text = provider.name, color = fieldContentColor) },
                        onClick = {
                            viewModel.onProviderSelected(provider)
                            providerDropdownExpanded.value = false
                        }
                    )
                }
            }
        }

        Text(
            text = stringRes.SETTINGS_CHAT_MODEL_TITLE,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        ExposedDropdownMenuBox(
            expanded = dropdownExpanded.value,
            onExpandedChange = { dropdownExpanded.value = it },
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
        ) {
            val fieldContainerColor = MaterialTheme.colorScheme.tertiaryContainer
            val fieldContentColor = MaterialTheme.colorScheme.onTertiaryContainer
            val displayedModelName = availableModels.value.find { it.id == aiModel.value }?.displayName ?: aiModel.value
            OutlinedTextField(
                value = displayedModelName,
                onValueChange = {},
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = dropdownExpanded.value) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = fieldContainerColor,
                    unfocusedContainerColor = fieldContainerColor,
                ),
                textStyle = TextStyle(color = fieldContentColor),
                modifier = Modifier.fillMaxWidth().menuAnchor()
            )
            ExposedDropdownMenu(
                expanded = dropdownExpanded.value,
                onDismissRequest = { dropdownExpanded.value = false }
            ) {
                availableModels.value.forEach { model ->
                    DropdownMenuItem(
                        text = { Text(text = model.displayName, color = fieldContentColor) },
                        onClick = {
                            viewModel.onModelSelected(model.id)
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
            text = stringRes.SETTINGS_CHAT_GLOBAL_CONTEXT_TITLE,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        val fieldContainerColor = MaterialTheme.colorScheme.tertiaryContainer
        val fieldContentColor = MaterialTheme.colorScheme.onTertiaryContainer
        OutlinedTextField(
            value = globalContextState.value,
            onValueChange = {
                globalContextState.value = it
                viewModel.onGlobalContextChanged(it)
            },
            placeholder = {
                Text(
                    text = stringRes.SETTINGS_CHAT_GLOBAL_CONTEXT_HINT,
                    color = fieldContentColor.copy(alpha = 0.6f)
                )
            },
            textStyle = TextStyle(color = fieldContentColor),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = fieldContainerColor,
                unfocusedContainerColor = fieldContainerColor,
            ),
            minLines = 3,
            maxLines = 6,
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
        )

        PrimaryButton(
            text = stringRes.SETTINGS_CHAT_SAVE,
            isEnabled = hasChanges.value,
            modifier = Modifier.padding(top = 8.dp)
        ) {
            viewModel.saveSettings()
        }
    }
}

