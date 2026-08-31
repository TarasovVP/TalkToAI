package com.vnteam.talktoai.presentation.screens.chat

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vnteam.talktoai.domain.enums.AiProviderType
import com.vnteam.talktoai.domain.enums.ModelTier
import com.vnteam.talktoai.domain.models.AiModels
import com.vnteam.talktoai.domain.models.Chat
import com.vnteam.talktoai.presentation.ui.components.ChatSheetWrapper
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

    val chatName = remember(chat.id) { mutableStateOf(chat.name.orEmpty()) }
    val chatContext = remember(chat.id) { mutableStateOf(chat.context.orEmpty()) }
    val initialProvider = chat.aiProvider
        ?.let { runCatching { AiProviderType.valueOf(it) }.getOrNull() }
        ?: AiProviderType.OPENAI
    val chatProvider = remember(chat.id) { mutableStateOf(initialProvider) }
    val validatedChatModel = chat.aiModel
        ?.takeIf { id -> AiModels.forProvider(initialProvider).any { it.id == id } }
        ?: chat.aiModel?.let { AiModels.balancedFor(initialProvider).id }
    val chatModel = remember(chat.id) { mutableStateOf(validatedChatModel) }
    val providerDropdownExpanded = remember { mutableStateOf(false) }
    val dropdownExpanded = remember { mutableStateOf(false) }

    val effectiveModel = chatModel.value ?: globalAiModel.value
    val hasOverride = chatModel.value != null && chatModel.value != globalAiModel.value

    val hasChanges = chatName.value != chat.name.orEmpty() ||
            chatContext.value != chat.context.orEmpty() ||
            chatModel.value != chat.aiModel ||
            chatProvider.value != initialProvider

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
                    value = chatProvider.value.name,
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
                                if (chatProvider.value != provider) {
                                    chatProvider.value = provider
                                    chatModel.value = AiModels.balancedFor(provider).id
                                }
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
                val providerModels = AiModels.forProvider(chatProvider.value)
                val effectiveModelName = providerModels.find { it.id == effectiveModel }?.displayName ?: effectiveModel
                val globalModelName = providerModels.find { it.id == globalAiModel.value }?.displayName ?: globalAiModel.value
                val displayModel = if (chatModel.value == null) {
                    "$effectiveModelName (${stringRes.CHAT_SETTINGS_GLOBAL_LABEL})"
                } else {
                    effectiveModelName
                }
                val fieldContainerColor = MaterialTheme.colorScheme.tertiaryContainer
                val fieldContentColor = MaterialTheme.colorScheme.onTertiaryContainer
                OutlinedTextField(
                    value = displayModel,
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
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = "$globalModelName (${stringRes.CHAT_SETTINGS_GLOBAL_LABEL})",
                                color = fieldContentColor
                            )
                        },
                        onClick = {
                            chatModel.value = null
                            dropdownExpanded.value = false
                        }
                    )
                    providerModels.forEach { model ->
                        DropdownMenuItem(
                            text = { Text(text = model.displayName, color = fieldContentColor) },
                            onClick = {
                                chatModel.value = model.id
                                dropdownExpanded.value = false
                            }
                        )
                    }
                }
            }

            if (hasOverride) {
                TextButton(
                    onClick = {
                        chatModel.value = null
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
                isEnabled = hasChanges,
                modifier = Modifier.padding(top = 8.dp)
            ) {
                val updatedChat = chat.copy(
                    name = chatName.value.takeIf { it.isNotBlank() } ?: chat.name,
                    context = chatContext.value.takeIf { it.isNotBlank() },
                    aiModel = chatModel.value,
                    aiProvider = chatProvider.value.name,
                )
                viewModel.saveChat(updatedChat)
                onChatUpdated(updatedChat)
            }
        }
    }
}
