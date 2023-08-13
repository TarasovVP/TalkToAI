<<<<<<<< HEAD:app/src/main/java/com/vnstudio/talktoai/presentation/theme/Theme.kt
package com.vnstudio.talktoai.presentation.theme
========
package com.vnstudio.talktoai.ui.theme
>>>>>>>> 467a644 (Add firebase to project):app/src/main/java/com/vnstudio/talktoai/ui/theme/Theme.kt

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable

private val DarkColorPalette = darkColors(
    primary = Primary200,
    primaryVariant = Primary700,
    secondary = Neutral200
)

private val LightColorPalette = lightColors(
    primary = Primary500,
    primaryVariant = Primary700,
    secondary = Neutral200

    /* Other default colors to override
    background = Color.White,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
    */
)

@Composable
fun TalkToAITheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}