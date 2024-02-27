package com.vnstudio.talktoai.domain.resources

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Immutable
data class Dimens(val margin: Dp)

val smallDimens = Dimens(margin = 10.dp)

val mediumDimens = Dimens(margin = 12.dp)

val largeDimens = Dimens(margin = 14.dp)

val LocalDimens = staticCompositionLocalOf { smallDimens }