package com.vnstudio.talktoai.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vnstudio.talktoai.presentation.theme.Blue
import com.vnstudio.talktoai.presentation.theme.Neutral400
import com.vnstudio.talktoai.presentation.theme.Neutral50
import com.vnstudio.talktoai.presentation.theme.Neutral700
import com.vnstudio.talktoai.presentation.theme.Primary300
import com.vnstudio.talktoai.presentation.theme.Primary400
import com.vnstudio.talktoai.presentation.theme.Primary500
import com.vnstudio.talktoai.presentation.theme.Primary700

@Composable
fun PrimaryButton(
    text: String,
    isEnabled: Boolean = true,
    modifier: Modifier,
    onClick: () -> Unit,
) {
    TextButton(enabled = isEnabled,
        modifier = modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .fillMaxWidth()
            .background(
                color = if (isEnabled) Primary500 else Neutral400,
                shape = RoundedCornerShape(16.dp)
            )
            .testTag("sign_up_button"),
        onClick = {
            onClick.invoke()
        }
    ) {
        Text(text = text, color = Color.White)
    }
}

@Composable
fun SecondaryButton(text: String, isDestructive: Boolean, modifier: Modifier, onClick: () -> Unit) {
    TextButton(modifier = modifier
        .padding(horizontal = 16.dp, vertical = 8.dp)
        .fillMaxWidth()
        .border(
            1.dp,
            if (isDestructive) Color.Red else Primary400,
            shape = RoundedCornerShape(16.dp)
        )
        .background(color = Color.Transparent, shape = RoundedCornerShape(16.dp)),
        onClick = {
            onClick.invoke()
        }
    ) {
        Text(text = text, color = if (isDestructive) Color.Red else Neutral700)
    }
}

@Composable
fun SubmitButtons(
    isEnabled: Boolean = true,
    onDismiss: () -> Unit,
    onConfirmationClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        SecondaryButton(text = stringRes().BUTTON_CANCEL, false, Modifier.weight(1f), onClick = onDismiss)
        PrimaryButton(text = stringRes().BUTTON_OK, isEnabled, Modifier.weight(1f)) {
            onConfirmationClick.invoke()
        }
    }
}

@Composable
fun GoogleButton(title: String, modifier: Modifier, onClick: () -> Unit) {
    Row(modifier = modifier
        .wrapContentSize()
        .border(1.dp, color = Primary300, shape = RoundedCornerShape(16.dp))
        .background(color = Color.Transparent, shape = RoundedCornerShape(16.dp))
        .clickable {
            onClick.invoke()
        }) {
        Image(
            painter = painterRes("ic_logo_google"),
            contentDescription = stringRes().AUTHORIZATION_WITH_GOOGLE_ACCOUNT,
            modifier = Modifier
                .padding(top = 8.dp, start = 16.dp, bottom = 8.dp)
        )
        Text(
            text = title,
            fontSize = 16.sp,
            modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp)
        )
    }
}

@Composable
fun LinkButton(text: String, modifier: Modifier, textAlign: TextAlign? = null,  onClick: () -> Unit) {
    TextButton(
        onClick = onClick, modifier = modifier
    ) {
        Text(text = text, maxLines = 1, overflow = TextOverflow.Ellipsis, color = Blue, textAlign = textAlign, modifier = Modifier.wrapContentSize())
    }
}

@Composable
fun TextIconButton(text: String, icon: String, modifier: Modifier, onClick: () -> Unit) {
    IconButton(onClick = onClick) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .background(color = Primary700, shape = RoundedCornerShape(16.dp))
        ) {
            Image(
                painter = painterRes(icon),
                contentDescription = stringRes().CHAT_CREATE_BUTTON,
                modifier = Modifier
                    .padding(8.dp)
            )
            Text(
                text = text,
                fontSize = 16.sp,
                color = Neutral50,
                modifier = Modifier
                    .weight(1f)
                    .padding(vertical = 8.dp)
            )
        }
    }
}

