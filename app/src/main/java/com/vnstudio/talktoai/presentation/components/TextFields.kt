package com.vnstudio.talktoai.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.vnstudio.talktoai.CommonExtensions.EMPTY
import com.vnstudio.talktoai.R
import com.vnstudio.talktoai.presentation.theme.Neutral400
import com.vnstudio.talktoai.presentation.theme.Neutral600
import com.vnstudio.talktoai.presentation.theme.Primary500
import com.vnstudio.talktoai.presentation.theme.Primary900

@Composable
fun PrimaryTextField(
    placeHolder: String,
    inputValue: MutableState<TextFieldValue>,
) {
    TextField(value = inputValue.value,
        onValueChange = { newValue ->
            inputValue.value = newValue
        }, placeholder = { Text(text = placeHolder) },
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = Color.White,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent
        ),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .border(1.dp, Primary500, shape = RoundedCornerShape(16.dp))
            .background(
                color = Color.White,
                shape = RoundedCornerShape(16.dp)
            ),
        maxLines = 1
    )
}

@Composable
fun SecondaryTextField(
    inputValue: MutableState<TextFieldValue>,
    placeHolder: String,
) {
    TextField(
        value = inputValue.value,
        onValueChange = { inputValue.value = it },
        placeholder = { Text(text = placeHolder) },
        colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.White),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    )
}

@Composable
fun PasswordTextField(inputValue: MutableState<TextFieldValue>, placeHolder: String) {
    val passwordVisible = remember { mutableStateOf(false) }

    TextField(
        value = inputValue.value,
        onValueChange = { newValue ->
            inputValue.value = newValue
        },
        placeholder = { Text(text = placeHolder) },
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = Color.White,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent
        ),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .border(1.dp, Primary500, shape = RoundedCornerShape(16.dp))
            .background(
                color = Color.White,
                shape = RoundedCornerShape(16.dp)
            ),
        maxLines = 1,
        visualTransformation = if (passwordVisible.value) VisualTransformation.None else PasswordVisualTransformation(),
        trailingIcon = {
            IconButton(
                onClick = { passwordVisible.value = passwordVisible.value.not() }
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = if (passwordVisible.value) R.drawable.ic_toggle_password_enabled else R.drawable.ic_toggle_password_disabled),
                    contentDescription = if (passwordVisible.value) "Hide password" else "Show password"
                )
            }
        }
    )
}

@Composable
fun ChatTextField(
    isEnabled: Boolean,
    modifier: Modifier,
    inputValue: MutableState<TextFieldValue>,
    onSendClick: (String) -> Unit,
) {
    val focusManager = LocalFocusManager.current
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(color = if (isEnabled) Primary900 else Neutral400)
            .height(IntrinsicSize.Min)
    ) {
        TextField(value = inputValue.value,
            onValueChange = { newValue ->
                inputValue.value = newValue
            }, placeholder = { Text(text = "Enter request") },
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color.White,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            ),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .border(1.dp, if (isEnabled) Primary500 else Neutral600, shape = RoundedCornerShape(16.dp))
                .background(
                    color = Color.White,
                    shape = RoundedCornerShape(16.dp)
                )
                .clickable(enabled = isEnabled) {},
            maxLines = 6,
            trailingIcon = {
                IconButton(enabled = isEnabled, onClick = {
                    onSendClick.invoke(inputValue.value.text)
                    inputValue.value = TextFieldValue(String.EMPTY)
                    focusManager.clearFocus()
                }) {
                    Icon(
                        imageVector = ImageVector.vectorResource(if (inputValue.value.text.isEmpty()) R.drawable.ic_voice_record else R.drawable.ic_message_send),
                        contentDescription = "Send message button",
                        tint = if (isEnabled) Primary900 else Neutral600
                    )
                }
            }
        )
    }
}