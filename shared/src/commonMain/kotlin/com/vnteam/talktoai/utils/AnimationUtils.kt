package com.vnteam.talktoai.utils

import androidx.compose.runtime.Composable

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
expect class AnimationUtils {
    @Composable
    fun MessageTypingAnimation(animationResource: String)
    @Composable
    fun MainProgressAnimation(animationResource: String)
}