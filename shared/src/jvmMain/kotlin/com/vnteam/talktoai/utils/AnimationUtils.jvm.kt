package com.vnteam.talktoai.utils

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual class AnimationUtils {

    @Composable
    actual fun MessageTypingAnimation(animationResource: String) {
        val transition = rememberInfiniteTransition()
        val delays = listOf(0, 150, 300)
        val alphas = delays.map { delay ->
            val alpha by transition.animateFloat(
                initialValue = 0.3f,
                targetValue = 1f,
                animationSpec = infiniteRepeatable(
                    animation = tween(600, delayMillis = delay, easing = LinearEasing),
                    repeatMode = RepeatMode.Reverse
                )
            )
            alpha
        }
        Box(modifier = Modifier.padding(16.dp)) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                alphas.forEach { alpha ->
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .alpha(alpha)
                            .background(Color.White, CircleShape)
                    )
                }
            }
        }
    }

    @Composable
    actual fun MainProgressAnimation(animationResource: String) {
        val transition = rememberInfiniteTransition()
        val delays = listOf(0, 200, 400)
        val alphas = delays.map { delay ->
            val alpha by transition.animateFloat(
                initialValue = 0.2f,
                targetValue = 1f,
                animationSpec = infiniteRepeatable(
                    animation = tween(800, delayMillis = delay, easing = LinearEasing),
                    repeatMode = RepeatMode.Reverse
                )
            )
            alpha
        }
        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                alphas.forEach { alpha ->
                    Box(
                        modifier = Modifier
                            .size(16.dp)
                            .alpha(alpha)
                            .background(Color.Gray, CircleShape)
                    )
                }
            }
        }
    }
}
