package com.vnteam.talktoai.presentation.screens.chat

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import com.vnteam.talktoai.presentation.ui.components.ChatSheetWrapper
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vnteam.talktoai.SettingsConstants
import com.vnteam.talktoai.domain.models.Chat
import com.vnteam.talktoai.presentation.ui.components.PrimaryButton
import com.vnteam.talktoai.presentation.ui.resources.LocalStringResources
import com.vnteam.talktoai.presentation.viewmodels.chats.ChatSettingsViewModel
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatSettingsBottomSheet(
    chat: Chat,
    onDismiss: () -> Unit,
    onChatUpdated: (Chat) -> Unit,
) {
    val viewModel: ChatSettingsViewModel = koinViewModel()
    val stringRes = LocalStringResources.current
    val globalAiModel = viewModel.globalAiModel.collectAsState()
    val globalTemperature = viewModel.globalTemperature.collectAsState()

    val chatName = remember(chat.id) { mutableStateOf(chat.name.orEmpty()) }
    val chatContext = remember(chat.id) { mutableStateOf(chat.context.orEmpty()) }
    val chatModel = remember(chat.id) { mutableStateOf(chat.aiModel) }
    val chatTemperature = remember(chat.id) { mutableStateOf(chat.temperature) }

    val dropdownExpanded = remember { mutableStateOf(false) }

    val effectiveModel = chatModel.value ?: globalAiModel.value
    val effectiveTemperature = chatTemperature.value ?: globalTemperature.value
    val hasOverride = chatModel.value != null || chatTemperature.value != null

    LaunchedEffect(Unit) {
        viewModel.chatSaved.collect {
            onDismiss()
        }
    }

    ChatSheetWrapper(onDismiss = onDismiss) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, bottom = 32.dp)
        ) {
            Text(
                text = stringRes.SETTINGS_CHAT,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            OutlinedTextField(
                value = chatName.value,
                onValueChange = { chatName.value = it },
                label = { Text(stringRes.CHAT_NAME) },
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                singleLine = true
            )

            OutlinedTextField(
                value = chatContext.value,
                onValueChange = { chatContext.value = it },
                label = { Text(stringRes.CHAT_CONTEXT) },
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                minLines = 3,
                maxLines = 6,
            )

            Text(
                text = stringRes.SETTINGS_CHAT_MODEL_TITLE,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            ExposedDropdownMenuBox(
                expanded = dropdownExpanded.value,
                onExpandedChange = { dropdownExpanded.value = it },
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
            ) {
                val displayModel = if (chatModel.value == null) {
                    "${effectiveModel} (${stringRes.CHAT_SETTINGS_GLOBAL_LABEL})"
                } else {
                    effectiveModel
                }
                OutlinedTextField(
                    value = displayModel,
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
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = "${globalAiModel.value} (${stringRes.CHAT_SETTINGS_GLOBAL_LABEL})",
                                color = Color.Black
                            )
                        },
                        onClick = {
                            chatModel.value = null
                            dropdownExpanded.value = false
                        }
                    )
                    SettingsConstants.AI_MODELS.forEach { model ->
                        DropdownMenuItem(
                            text = { Text(text = model, color = Color.Black) },
                            onClick = {
                                chatModel.value = model
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
                val tempLabel = if (chatTemperature.value == null) {
                    "${effectiveTemperature} (${stringRes.CHAT_SETTINGS_GLOBAL_LABEL})"
                } else {
                    effectiveTemperature.toString()
                }
                Text(text = tempLabel)
            }
            Slider(
                value = effectiveTemperature,
                onValueChange = { newVal ->
                    chatTemperature.value = (newVal * 10).toInt() / 10f
                },
                valueRange = SettingsConstants.AI_TEMPERATURE_MIN..SettingsConstants.AI_TEMPERATURE_MAX,
                steps = 19,
                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
            )

            if (hasOverride) {
                TextButton(
                    onClick = {
                        chatModel.value = null
                        chatTemperature.value = null
                    },
                    modifier = Modifier.padding(bottom = 8.dp)
                ) {
                    Text(text = stringRes.CHAT_SETTINGS_USE_GLOBAL)
                }
            } else {
                Spacer(modifier = Modifier.height(8.dp))
            }

            PrimaryButton(
                text = stringRes.SETTINGS_CHAT_SAVE,
                isEnabled = true,
                modifier = Modifier.padding(top = 8.dp)
            ) {
                val updatedChat = chat.copy(
                    name = chatName.value.takeIf { it.isNotBlank() } ?: chat.name,
                    context = chatContext.value.takeIf { it.isNotBlank() },
                    aiModel = chatModel.value,
                    temperature = chatTemperature.value,
                )
                viewModel.saveChat(updatedChat)
                onChatUpdated(updatedChat)
            }
        }
    }
}
