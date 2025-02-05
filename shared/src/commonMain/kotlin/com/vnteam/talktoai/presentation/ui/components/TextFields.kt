package com.vnteam.talktoai.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vnteam.talktoai.CommonExtensions.EMPTY
import com.vnteam.talktoai.Res
import com.vnteam.talktoai.ic_message_send
import com.vnteam.talktoai.ic_toggle_password_disabled
import com.vnteam.talktoai.ic_toggle_password_enabled
import com.vnteam.talktoai.presentation.ui.resources.LocalDefaultTextSize
import com.vnteam.talktoai.presentation.ui.resources.LocalStringResources
import com.vnteam.talktoai.presentation.ui.theme.Neutral600
import com.vnteam.talktoai.presentation.ui.theme.Primary500
import com.vnteam.talktoai.presentation.ui.theme.Primary900
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrimaryTextField(
    placeHolder: String,
    inputValue: MutableState<TextFieldValue>,
) {
    TextField(value = inputValue.value,
        onValueChange = { newValue ->
            inputValue.value = newValue
        }, placeholder = { Text(text = placeHolder) },
        colors = TextFieldDefaults.colors(
            focusedTextColor = FilledTextFieldTokens.FocusInputColor.value,
            unfocusedTextColor = FilledTextFieldTokens.InputColor.value,
            disabledTextColor = FilledTextFieldTokens.DisabledInputColor.value
                .copy(alpha = FilledTextFieldTokens.DisabledInputOpacity),
            errorTextColor = FilledTextFieldTokens.ErrorInputColor.value,
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White,
            disabledContainerColor = Color.White,
            errorContainerColor = FilledTextFieldTokens.ContainerColor.value,
            cursorColor = FilledTextFieldTokens.CaretColor.value,
            errorCursorColor = FilledTextFieldTokens.ErrorFocusCaretColor.value,
            selectionColors = LocalTextSelectionColors.current,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
            errorIndicatorColor = FilledTextFieldTokens.ErrorActiveIndicatorColor.value,
            focusedLeadingIconColor = FilledTextFieldTokens.FocusLeadingIconColor.value,
            unfocusedLeadingIconColor = FilledTextFieldTokens.LeadingIconColor.value,
            disabledLeadingIconColor = FilledTextFieldTokens.DisabledLeadingIconColor.value
                .copy(alpha = FilledTextFieldTokens.DisabledLeadingIconOpacity),
            errorLeadingIconColor = FilledTextFieldTokens.ErrorLeadingIconColor.value,
            focusedTrailingIconColor = FilledTextFieldTokens.FocusTrailingIconColor.value,
            unfocusedTrailingIconColor = FilledTextFieldTokens.TrailingIconColor.value,
            disabledTrailingIconColor = FilledTextFieldTokens.DisabledTrailingIconColor.value
                .copy(alpha = FilledTextFieldTokens.DisabledTrailingIconOpacity),
            errorTrailingIconColor = FilledTextFieldTokens.ErrorTrailingIconColor.value,
            focusedLabelColor = FilledTextFieldTokens.FocusLabelColor.value,
            unfocusedLabelColor = FilledTextFieldTokens.LabelColor.value,
            disabledLabelColor = FilledTextFieldTokens.DisabledLabelColor.value
                .copy(alpha = FilledTextFieldTokens.DisabledLabelOpacity),
            errorLabelColor = FilledTextFieldTokens.ErrorLabelColor.value,
            focusedPlaceholderColor = FilledTextFieldTokens.InputPlaceholderColor.value,
            unfocusedPlaceholderColor = FilledTextFieldTokens.InputPlaceholderColor.value,
            disabledPlaceholderColor = FilledTextFieldTokens.DisabledInputColor.value
                .copy(alpha = FilledTextFieldTokens.DisabledInputOpacity),
            errorPlaceholderColor = FilledTextFieldTokens.InputPlaceholderColor.value,
            focusedSupportingTextColor = FilledTextFieldTokens.FocusSupportingColor.value,
            unfocusedSupportingTextColor = FilledTextFieldTokens.SupportingColor.value,
            disabledSupportingTextColor = FilledTextFieldTokens.DisabledSupportingColor.value
                .copy(alpha = FilledTextFieldTokens.DisabledSupportingOpacity),
            errorSupportingTextColor = FilledTextFieldTokens.ErrorSupportingColor.value,
            focusedPrefixColor = FilledTextFieldTokens.InputPrefixColor.value,
            unfocusedPrefixColor = FilledTextFieldTokens.InputPrefixColor.value,
            disabledPrefixColor = FilledTextFieldTokens.InputPrefixColor.value
                .copy(alpha = FilledTextFieldTokens.DisabledInputOpacity),
            errorPrefixColor = FilledTextFieldTokens.InputPrefixColor.value,
            focusedSuffixColor = FilledTextFieldTokens.InputSuffixColor.value,
            unfocusedSuffixColor = FilledTextFieldTokens.InputSuffixColor.value,
            disabledSuffixColor = FilledTextFieldTokens.InputSuffixColor.value
                .copy(alpha = FilledTextFieldTokens.DisabledInputOpacity),
            errorSuffixColor = FilledTextFieldTokens.InputSuffixColor.value,
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SecondaryTextField(
    inputValue: MutableState<TextFieldValue>,
    placeHolder: String,
) {
    TextField(
        value = inputValue.value,
        onValueChange = { inputValue.value = it },
        placeholder = { Text(text = placeHolder) },
        colors = TextFieldDefaults.colors(
            focusedTextColor = FilledTextFieldTokens.FocusInputColor.value,
            unfocusedTextColor = FilledTextFieldTokens.InputColor.value,
            disabledTextColor = FilledTextFieldTokens.DisabledInputColor.value
                .copy(alpha = FilledTextFieldTokens.DisabledInputOpacity),
            errorTextColor = FilledTextFieldTokens.ErrorInputColor.value,
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White,
            disabledContainerColor = Color.White,
            errorContainerColor = FilledTextFieldTokens.ContainerColor.value,
            cursorColor = FilledTextFieldTokens.CaretColor.value,
            errorCursorColor = FilledTextFieldTokens.ErrorFocusCaretColor.value,
            selectionColors = LocalTextSelectionColors.current,
            focusedIndicatorColor = FilledTextFieldTokens.FocusActiveIndicatorColor.value,
            unfocusedIndicatorColor = FilledTextFieldTokens.ActiveIndicatorColor.value,
            disabledIndicatorColor = FilledTextFieldTokens.DisabledActiveIndicatorColor.value
                .copy(alpha = FilledTextFieldTokens.DisabledActiveIndicatorOpacity),
            errorIndicatorColor = FilledTextFieldTokens.ErrorActiveIndicatorColor.value,
            focusedLeadingIconColor = FilledTextFieldTokens.FocusLeadingIconColor.value,
            unfocusedLeadingIconColor = FilledTextFieldTokens.LeadingIconColor.value,
            disabledLeadingIconColor = FilledTextFieldTokens.DisabledLeadingIconColor.value
                .copy(alpha = FilledTextFieldTokens.DisabledLeadingIconOpacity),
            errorLeadingIconColor = FilledTextFieldTokens.ErrorLeadingIconColor.value,
            focusedTrailingIconColor = FilledTextFieldTokens.FocusTrailingIconColor.value,
            unfocusedTrailingIconColor = FilledTextFieldTokens.TrailingIconColor.value,
            disabledTrailingIconColor = FilledTextFieldTokens.DisabledTrailingIconColor.value
                .copy(alpha = FilledTextFieldTokens.DisabledTrailingIconOpacity),
            errorTrailingIconColor = FilledTextFieldTokens.ErrorTrailingIconColor.value,
            focusedLabelColor = FilledTextFieldTokens.FocusLabelColor.value,
            unfocusedLabelColor = FilledTextFieldTokens.LabelColor.value,
            disabledLabelColor = FilledTextFieldTokens.DisabledLabelColor.value
                .copy(alpha = FilledTextFieldTokens.DisabledLabelOpacity),
            errorLabelColor = FilledTextFieldTokens.ErrorLabelColor.value,
            focusedPlaceholderColor = FilledTextFieldTokens.InputPlaceholderColor.value,
            unfocusedPlaceholderColor = FilledTextFieldTokens.InputPlaceholderColor.value,
            disabledPlaceholderColor = FilledTextFieldTokens.DisabledInputColor.value
                .copy(alpha = FilledTextFieldTokens.DisabledInputOpacity),
            errorPlaceholderColor = FilledTextFieldTokens.InputPlaceholderColor.value,
            focusedSupportingTextColor = FilledTextFieldTokens.FocusSupportingColor.value,
            unfocusedSupportingTextColor = FilledTextFieldTokens.SupportingColor.value,
            disabledSupportingTextColor = FilledTextFieldTokens.DisabledSupportingColor.value
                .copy(alpha = FilledTextFieldTokens.DisabledSupportingOpacity),
            errorSupportingTextColor = FilledTextFieldTokens.ErrorSupportingColor.value,
            focusedPrefixColor = FilledTextFieldTokens.InputPrefixColor.value,
            unfocusedPrefixColor = FilledTextFieldTokens.InputPrefixColor.value,
            disabledPrefixColor = FilledTextFieldTokens.InputPrefixColor.value
                .copy(alpha = FilledTextFieldTokens.DisabledInputOpacity),
            errorPrefixColor = FilledTextFieldTokens.InputPrefixColor.value,
            focusedSuffixColor = FilledTextFieldTokens.InputSuffixColor.value,
            unfocusedSuffixColor = FilledTextFieldTokens.InputSuffixColor.value,
            disabledSuffixColor = FilledTextFieldTokens.InputSuffixColor.value
                .copy(alpha = FilledTextFieldTokens.DisabledInputOpacity),
            errorSuffixColor = FilledTextFieldTokens.InputSuffixColor.value,
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasswordTextField(inputValue: MutableState<TextFieldValue>, placeHolder: String) {
    val passwordVisible = remember { mutableStateOf(false) }

    TextField(
        value = inputValue.value,
        onValueChange = { newValue ->
            inputValue.value = newValue
        },
        placeholder = { Text(text = placeHolder) },
        colors = TextFieldDefaults.colors(
            focusedTextColor = FilledTextFieldTokens.FocusInputColor.value,
            unfocusedTextColor = FilledTextFieldTokens.InputColor.value,
            disabledTextColor = FilledTextFieldTokens.DisabledInputColor.value
                .copy(alpha = FilledTextFieldTokens.DisabledInputOpacity),
            errorTextColor = FilledTextFieldTokens.ErrorInputColor.value,
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White,
            disabledContainerColor = Color.White,
            errorContainerColor = FilledTextFieldTokens.ContainerColor.value,
            cursorColor = FilledTextFieldTokens.CaretColor.value,
            errorCursorColor = FilledTextFieldTokens.ErrorFocusCaretColor.value,
            selectionColors = LocalTextSelectionColors.current,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
            errorIndicatorColor = FilledTextFieldTokens.ErrorActiveIndicatorColor.value,
            focusedLeadingIconColor = FilledTextFieldTokens.FocusLeadingIconColor.value,
            unfocusedLeadingIconColor = FilledTextFieldTokens.LeadingIconColor.value,
            disabledLeadingIconColor = FilledTextFieldTokens.DisabledLeadingIconColor.value
                .copy(alpha = FilledTextFieldTokens.DisabledLeadingIconOpacity),
            errorLeadingIconColor = FilledTextFieldTokens.ErrorLeadingIconColor.value,
            focusedTrailingIconColor = FilledTextFieldTokens.FocusTrailingIconColor.value,
            unfocusedTrailingIconColor = FilledTextFieldTokens.TrailingIconColor.value,
            disabledTrailingIconColor = FilledTextFieldTokens.DisabledTrailingIconColor.value
                .copy(alpha = FilledTextFieldTokens.DisabledTrailingIconOpacity),
            errorTrailingIconColor = FilledTextFieldTokens.ErrorTrailingIconColor.value,
            focusedLabelColor = FilledTextFieldTokens.FocusLabelColor.value,
            unfocusedLabelColor = FilledTextFieldTokens.LabelColor.value,
            disabledLabelColor = FilledTextFieldTokens.DisabledLabelColor.value
                .copy(alpha = FilledTextFieldTokens.DisabledLabelOpacity),
            errorLabelColor = FilledTextFieldTokens.ErrorLabelColor.value,
            focusedPlaceholderColor = FilledTextFieldTokens.InputPlaceholderColor.value,
            unfocusedPlaceholderColor = FilledTextFieldTokens.InputPlaceholderColor.value,
            disabledPlaceholderColor = FilledTextFieldTokens.DisabledInputColor.value
                .copy(alpha = FilledTextFieldTokens.DisabledInputOpacity),
            errorPlaceholderColor = FilledTextFieldTokens.InputPlaceholderColor.value,
            focusedSupportingTextColor = FilledTextFieldTokens.FocusSupportingColor.value,
            unfocusedSupportingTextColor = FilledTextFieldTokens.SupportingColor.value,
            disabledSupportingTextColor = FilledTextFieldTokens.DisabledSupportingColor.value
                .copy(alpha = FilledTextFieldTokens.DisabledSupportingOpacity),
            errorSupportingTextColor = FilledTextFieldTokens.ErrorSupportingColor.value,
            focusedPrefixColor = FilledTextFieldTokens.InputPrefixColor.value,
            unfocusedPrefixColor = FilledTextFieldTokens.InputPrefixColor.value,
            disabledPrefixColor = FilledTextFieldTokens.InputPrefixColor.value
                .copy(alpha = FilledTextFieldTokens.DisabledInputOpacity),
            errorPrefixColor = FilledTextFieldTokens.InputPrefixColor.value,
            focusedSuffixColor = FilledTextFieldTokens.InputSuffixColor.value,
            unfocusedSuffixColor = FilledTextFieldTokens.InputSuffixColor.value,
            disabledSuffixColor = FilledTextFieldTokens.InputSuffixColor.value
                .copy(alpha = FilledTextFieldTokens.DisabledInputOpacity),
            errorSuffixColor = FilledTextFieldTokens.InputSuffixColor.value,
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
                    painter = painterResource(if (passwordVisible.value) Res.drawable.ic_toggle_password_enabled else Res.drawable.ic_toggle_password_disabled),
                    contentDescription = LocalStringResources.current.AUTHORIZATION_PASSWORD
                )
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
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
    }

    TextField(value = inputValue.value,
        onValueChange = { newValue ->
            inputValue.value = newValue
        },
        placeholder = {
            Text(
                text = LocalStringResources.current.MESSAGE_ENTER_REQUEST,
                style = TextStyle(color = Color.Gray)
            )
        },
        textStyle = TextStyle(color = Color.Black),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White,
            disabledContainerColor = Color.White,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
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
            }) {
                Icon(
                    painter = painterResource(Res.drawable.ic_message_send),
                    contentDescription = LocalStringResources.current.MESSAGE_SEND_BUTTON,
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
    linesCount: Int,
) {
    Column(
        modifier = Modifier
            .padding(10.dp)
            .wrapContentSize()
    ) {
        Text(
            text = message,
            fontSize = LocalDefaultTextSize.current.textSize,
            color = Color.White,
            maxLines = if (isTruncated.value) 1 else Int.MAX_VALUE,
            overflow = TextOverflow.Ellipsis
        )
        if (linesCount > 2) {
            Text(
                text = if (isTruncated.value) LocalStringResources.current.MESSAGE_MORE else LocalStringResources.current.MESSAGE_HIDE,
                color = Color.LightGray,
                fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                fontSize = 12.sp,
                modifier = Modifier
                    .padding(top = 5.dp)
                    .clickable(onClick = {
                        isTruncated.value = isTruncated.value.not()
                    })
            )
        }
    }
}