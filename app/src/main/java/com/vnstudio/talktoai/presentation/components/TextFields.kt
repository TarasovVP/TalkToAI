package com.vnstudio.talktoai.presentation.components

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vnstudio.talktoai.CommonExtensions.EMPTY
import com.vnstudio.talktoai.CommonExtensions.isNotNull
import com.vnstudio.talktoai.R
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
fun TextFieldWithButton(
    isEnabled: Boolean,
    onSendClick: (String) -> Unit,
) {

    val inputValue: MutableState<TextFieldValue> = remember {
        mutableStateOf(TextFieldValue())
    }

    val focusManager = LocalFocusManager.current
    LaunchedEffect(inputValue.value) {
        if (inputValue.value.text.isEmpty()) focusManager.clearFocus()
        Log.e("inputValueTAG", "TextFields TextFieldWithButton LaunchedEffect inputValue ${inputValue.value.text}")
    }

    Log.e("inputValueTAG", "TextFields TextFieldWithButton inputValue ${inputValue.value.text}")

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
            .border(
                1.dp,
                if (isEnabled) Primary500 else Neutral600,
                shape = RoundedCornerShape(16.dp)
            )
            .background(
                color = Color.White,
                shape = RoundedCornerShape(16.dp)
            )
            .clickable(enabled = isEnabled) {},
        maxLines = 6,
        trailingIcon = {
            IconButton(enabled = isEnabled && inputValue.value.text.isNotBlank(), onClick = {
                onSendClick.invoke(inputValue.value.text.trim())
                inputValue.value = TextFieldValue(String.EMPTY)
                Log.e("inputValueTAG", "TextFields TextFieldWithButton onClick inputValue ${inputValue.value.text}")
            }) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_message_send),
                    contentDescription = "Send message button",
                    tint = if (isEnabled) Primary900 else Neutral600
                )
            }
        }
    )
}

@Composable
fun TruncatableText(
    message: String,
    isTruncated: MutableState<Boolean>,
    linesCount: Int
) {
    Log.e("truncateTAG", "TruncatableText init message ${message.takeIf { it.length > 6 }?.substring(0, 6) } isTruncated ${isTruncated.value}" )
    if ( linesCount > 2) {
        val annotatedString = buildAnnotatedString {
            val textSpanStyle = MaterialTheme.typography.body1.toSpanStyle().copy(color = Color.White)
            withStyle(style = textSpanStyle) {
                append(message)
            }
            val truncationButton = if (isTruncated.value) " More " else " Hide "
            withStyle(style = textSpanStyle.copy(color = Color.Blue, fontStyle = androidx.compose.ui.text.font.FontStyle.Italic, fontSize = 12.sp)) {
                append( truncationButton )
                addStringAnnotation(
                    tag = "CLICKABLE",
                    annotation = "icon",
                    start = length - truncationButton.length,
                    end = length
                )
            }
        }

        ClickableText(
            text = annotatedString,
            modifier = Modifier
                .padding(10.dp)
                .wrapContentSize()
                .pointerInput(Unit) {
                    Log.e("clickTAG", "pointerInput: ")
                    detectTapGestures(onTap = {
                        Log.e("clickTAG", "pointerInput: ")
                    })
                    detectTapGestures(onLongPress = {
                        Log.e("clickTAG", "pointerInput: ")
                    })
                }
                .let { if (isTruncated.value) it.heightIn(max = 68.dp) else it },
            onClick = { offset ->
                Log.e("truncateTAG", "TruncatableText onClick message ${message.takeIf { it.length > 6 }?.substring(0, 6) } isTruncated ${isTruncated.value}" )
                annotatedString.getStringAnnotations(tag = "CLICKABLE", start = offset, end = offset).firstOrNull()?.let {
                    isTruncated.value = isTruncated.value.not()
                }
            }
        )
    } else {
        Text(
            text = message,
            fontSize = getDimensionResource(resId = R.dimen.default_text_size).value.sp,
            color = Color.White,
            modifier = Modifier
                .padding(10.dp)
                .wrapContentSize()
        )
    }
}