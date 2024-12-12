package com.vnteam.talktoai.utils

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual class AnimationUtils {
    @Composable
    actual fun MessageTypingAnimation(animationResource: String) {
        val composition by rememberLottieComposition(
            LottieCompositionSpec.JsonString(
                animationResource
            )
        )
        Box(
            Modifier
                .padding(16.dp)
        ) {
            LottieAnimation(
                composition,
                iterations = LottieConstants.IterateForever,
                modifier = Modifier
                    .width(52.dp)
                    .height(12.dp)
            )
        }
    }
}