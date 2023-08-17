package com.vnstudio.talktoai.presentation.base

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.vnstudio.talktoai.R
import com.vnstudio.talktoai.ui.theme.*

@Composable
fun PrimaryButton(text: String, isEnabled: Boolean = true, modifier: Modifier, onClick: () -> Unit) {
    TextButton(enabled = isEnabled,
        modifier = modifier
        .padding(horizontal = 16.dp, vertical = 8.dp)
        .fillMaxWidth()
        .background(color = if (isEnabled) Primary500 else Neutral400, shape = RoundedCornerShape(16.dp)),
        onClick = {
            onClick.invoke()
        }
    ) {
        Text(text = text, color = Color.White)
    }
}

@Composable
fun SecondaryButton(text: String, modifier: Modifier, onClick: () -> Unit) {
    TextButton(modifier = modifier
        .padding(horizontal = 16.dp, vertical = 8.dp)
        .fillMaxWidth()
        .border(1.dp, Neutral700, shape = RoundedCornerShape(16.dp))
        .background(color = Color.Transparent, shape = RoundedCornerShape(16.dp)),
        onClick = {
            onClick.invoke()
        }
    ) {
        Text(text = text, color = Neutral700)
    }
}

@Composable
fun AddChatItem(modifier: Modifier, onAddChatClicked: () -> Unit) {
    Row(modifier = modifier
        .fillMaxWidth()
        .background(color = Primary700, shape = RoundedCornerShape(16.dp))
        .clickable {
            onAddChatClicked.invoke()
        }) {
        Image(
            imageVector = ImageVector.vectorResource(id = R.drawable.ic_chat_add),
            contentDescription = "Add chat button",
            modifier = Modifier
                .padding(8.dp)
        )
        Text(
            text = "New chat",
            fontSize = 16.sp,
            color = Neutral50,
            modifier = Modifier.weight(1f).padding(vertical = 8.dp)
        )
    }
}

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
                        Row(modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            SecondaryButton(text = "Cancel", Modifier.weight(1f), onClick = onDismiss)
                            PrimaryButton(text = "OK", true, Modifier.weight(1f)) {
                                onConfirmationClick.invoke()
                            }
                        }
                    }
                }
            )
        }
    }
}

@Composable
fun DataEditDialog(title: String, placeHolder: String, inputValue: MutableState<TextFieldValue>, showDialog: Boolean, onDismiss: () -> Unit, onConfirmationClick: (String) -> Unit) {
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
                        TextField(
                            value = inputValue.value,
                            onValueChange = { inputValue.value = it },
                            placeholder = { Text(text = placeHolder) },
                            colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.White),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp))
                        Row(modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            SecondaryButton(text = "Cancel", Modifier.weight(1f), onClick = onDismiss)
                            PrimaryButton(text = "OK", inputValue.value.text.isNotEmpty(), Modifier.weight(1f)) {
                                onConfirmationClick.invoke(inputValue.value.text)
                            }
                        }
                    }
                }
            )
        }
    }
}