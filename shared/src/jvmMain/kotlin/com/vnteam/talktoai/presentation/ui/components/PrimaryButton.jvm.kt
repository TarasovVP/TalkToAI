package com.vnteam.talktoai.presentation.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
    FilledTonalButton(
        enabled = isEnabled,
        modifier = modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.filledTonalButtonColors(
            containerColor = Primary500,
            contentColor = Color.White,
            disabledContainerColor = Neutral400,
            disabledContentColor = Color.White,
        ),
        onClick = { onClick() }
    ) {
        Text(text = text)
    }
}
