package com.vnteam.talktoai.presentation.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import com.vnteam.talktoai.presentation.ui.resources.default_padding
import com.vnteam.talktoai.presentation.ui.resources.medium_padding
import com.vnteam.talktoai.presentation.ui.resources.small_padding

val Shapes = Shapes(
    small = RoundedCornerShape(small_padding.size.value),
    medium = RoundedCornerShape(medium_padding.size.value),
    large = RoundedCornerShape(default_padding.size.value)
)