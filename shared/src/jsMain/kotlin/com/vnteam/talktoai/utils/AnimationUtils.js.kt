package com.vnteam.talktoai.utils

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.vnteam.talktoai.presentation.ui.theme.Primary500

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual class AnimationUtils {
    @Composable
    actual fun MessageTypingAnimation(animationResource: String) {
        Row(
            modifier = Modifier.padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            CircularProgressIndicator(
                modifier = Modifier.size(20.dp),
                color = Primary500,
                strokeWidth = 2.dp
            )
        }
    }

    @Composable
    actual fun MainProgressAnimation(animationResource: String) {
    }
}