package com.vnstudio.talktoai.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.vnstudio.talktoai.ui.theme.Primary500

@Composable
fun ConfirmationDialog(title: String, showDialog: Boolean, onDismiss: () -> Unit, onConfirmationClick: () -> Unit) {
    Column {
        if (showDialog) {
            Dialog(
                onDismissRequest = onDismiss,
                content = {
                    Column(modifier = Modifier
                        .wrapContentSize()
                        .border(1.dp, Primary500, shape = RoundedCornerShape(16.dp))
                        .background(color = Color.White, shape = RoundedCornerShape(16.dp)),
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(text = title, modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp), textAlign = TextAlign.Center,)
                        SubmitButtons(onDismiss, onConfirmationClick)
                    }
                }
            )
        }
    }
}

@Composable
fun DataEditDialog(title: String, placeHolder: String, inputValue: MutableState<TextFieldValue>, showDialog: MutableState<Boolean>, onDismiss: () -> Unit, onConfirmationClick: (String) -> Unit) {
    Column {
        if (showDialog.value) {
            Dialog(
                onDismissRequest = onDismiss,
                content = {
                    Column(modifier = Modifier
                        .wrapContentSize()
                        .border(1.dp, Primary500, shape = RoundedCornerShape(16.dp))
                        .background(color = Color.White, shape = RoundedCornerShape(16.dp)),
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(text = title, modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp), textAlign = TextAlign.Center,)
                        SecondaryTextField(inputValue, placeHolder)
                        SubmitButtons(onDismiss) {
                            onConfirmationClick.invoke(inputValue.value.text)
                        }
                    }
                }
            )
        }
    }
}