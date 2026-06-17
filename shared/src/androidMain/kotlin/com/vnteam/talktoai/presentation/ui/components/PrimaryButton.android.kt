package com.vnteam.talktoai.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.vnteam.talktoai.presentation.ui.theme.Neutral400
import com.vnteam.talktoai.presentation.ui.theme.Primary500

@Composable
actual fun PrimaryButton(
    text: String,
    isEnabled: Boolean,
    modifier: Modifier,
    onClick: () -> Unit,
) {
    TextButton(
        enabled = isEnabled,
        modifier = modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .fillMaxWidth()
            .background(
                color = if (isEnabled) Primary500 else Neutral400,
                shape = RoundedCornerShape(16.dp)
            )
            .testTag("sign_up_button"),
        onClick = { onClick.invoke() }
    ) {
        Text(text = text, color = Color.White)
    }
}
