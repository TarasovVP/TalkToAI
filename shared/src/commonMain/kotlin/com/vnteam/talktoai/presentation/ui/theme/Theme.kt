package com.vnteam.talktoai.presentation.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle

private val DarkColorPalette = darkColorScheme(
    primary = Primary200,
    primaryContainer = Primary800,
    secondary = Neutral500,
    secondaryContainer = White.copy(alpha = 0.12f),
    onSecondaryContainer = Neutral50,
    tertiaryContainer = Neutral800,
    onTertiaryContainer = Neutral50,
    onBackground = White
)

private val LightColorPalette = lightColorScheme(
    primary = Primary500,
    primaryContainer = Primary400,
    secondary = Neutral200,
    secondaryContainer = Primary500,
    onSecondaryContainer = Neutral50,
    tertiaryContainer = White,
    onTertiaryContainer = Neutral900,
    onBackground = Neutral800
)

@Composable
fun AppTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val appColors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colorScheme = appColors,
        shapes = Shapes,
        typography = appTypography(),
        content = {
            ProvideTextStyle(
                value = TextStyle(color = if (darkTheme) White else Primary800),
                content = content
            )
        }
    )
}