package com.vnstudio.talktoai.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vnstudio.talktoai.R
import com.vnstudio.talktoai.ui.theme.*


@Composable
fun PrimaryButton(text: String, isEnabled: Boolean = true, modifier: Modifier, onClick: () -> Unit) {
    TextButton(enabled = isEnabled,
        modifier = modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .fillMaxWidth()
            .background(
                color = if (isEnabled) Primary500 else Neutral400,
                shape = RoundedCornerShape(16.dp)
            ),
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
        .border(1.dp, if (isDestructive) Color.Red else Primary400, shape = RoundedCornerShape(16.dp))
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
    onConfirmationClick: () -> Unit
) {
    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(8.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        SecondaryButton(text = "Cancel", false, Modifier.weight(1f), onClick = onDismiss)
        PrimaryButton(text = "OK", isEnabled, Modifier.weight(1f)) {
            onConfirmationClick.invoke()
        }
    }
}

@Composable
fun GoogleButton(modifier: Modifier, onClick: () -> Unit) {
    Row(modifier = modifier
        .wrapContentSize()
        .border(1.dp, color = Primary300, shape = RoundedCornerShape(16.dp))
        .background(color = Color.Transparent, shape = RoundedCornerShape(16.dp))
        .clickable {
            onClick.invoke()
        }) {
        Image(
            imageVector = ImageVector.vectorResource(id = R.drawable.ic_logo_google),
            contentDescription = "Google button",
            modifier = Modifier
                .padding(top = 8.dp, start = 16.dp, bottom = 8.dp)
        )
        Text(
            text = "Войти",
            fontSize = 16.sp,
            modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp)
        )
    }
}

@Composable
fun LinkButton(text: String, modifier: Modifier, onClick: () -> Unit) {
    TextButton(
        onClick = onClick, modifier = modifier
    ) {
        Text(text = text, color = Color.Blue)
    }
}

