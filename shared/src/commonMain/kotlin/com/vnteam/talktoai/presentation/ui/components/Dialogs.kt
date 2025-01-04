package com.vnteam.talktoai.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.vnteam.talktoai.CommonExtensions.EMPTY
import com.vnteam.talktoai.presentation.ui.resources.LocalStringResources
import com.vnteam.talktoai.presentation.ui.theme.Primary500

@Composable
fun ConfirmationDialog(
    title: String,
    showDialog: MutableState<Boolean>,
    onConfirmationClick: () -> Unit,
) {
    if (showDialog.value) {
        Column {
            Dialog(
                onDismissRequest = {
                    showDialog.value = false
                },
                content = {
                    Column(
                        modifier = Modifier
                            .wrapContentSize()
                            .border(1.dp, Primary500, shape = RoundedCornerShape(16.dp))
                            .background(color = Color.White, shape = RoundedCornerShape(16.dp)),
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = title,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            textAlign = TextAlign.Center,
                        )
                        SubmitButtons(true, {
                            showDialog.value = false
                        }, {
                            showDialog.value = false
                            onConfirmationClick.invoke()
                        }
                        )
                    }
                }
            )
        }
    }
}

@Composable
fun DataEditDialog(
    title: String,
    placeHolder: String,
    inputValue: MutableState<TextFieldValue>,
    showDialog: MutableState<Boolean>,
    onConfirmationClick: (String) -> Unit,
) {
    if (showDialog.value) {
        Column {
            Dialog(
                onDismissRequest = {
                    showDialog.value = false
                },
                content = {
                    Column(
                        modifier = Modifier
                            .wrapContentSize()
                            .border(1.dp, Primary500, shape = RoundedCornerShape(16.dp))
                            .background(color = Color.White, shape = RoundedCornerShape(16.dp)),
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = title,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            textAlign = TextAlign.Center,
                        )
                        SecondaryTextField(inputValue, placeHolder)
                        SubmitButtons(inputValue.value.text.isNotEmpty(), {
                            showDialog.value = false
                        }) {
                            onConfirmationClick.invoke(inputValue.value.text)
                        }
                    }
                }
            )
        }
    }
}

@Composable
fun CreateChatDialog(
    currentChatName: String = String.EMPTY,
    showDialog: MutableState<Boolean>,
    onConfirmationClick: (String) -> Unit,
) {
    DataEditDialog(
        if (currentChatName.isEmpty()) LocalStringResources.current.CHAT_CREATE_TITLE else LocalStringResources.current.CHAT_RENAME_TITLE,
        LocalStringResources.current.CHAT_NAME,
        remember {
            mutableStateOf(TextFieldValue(currentChatName))
        },
        showDialog) { newChatName ->
        showDialog.value = false
        onConfirmationClick.invoke(newChatName)
    }
}