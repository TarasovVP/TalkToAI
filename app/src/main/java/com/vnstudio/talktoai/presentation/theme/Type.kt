<<<<<<<< HEAD:app/src/main/java/com/vnstudio/talktoai/presentation/theme/Type.kt
package com.vnstudio.talktoai.presentation.theme
========
package com.vnstudio.talktoai.ui.theme
>>>>>>>> 467a644 (Add firebase to project):app/src/main/java/com/vnstudio/talktoai/ui/theme/Type.kt

import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.vnstudio.talktoai.R
<<<<<<<< HEAD:app/src/main/java/com/vnstudio/talktoai/presentation/theme/Type.kt

========
>>>>>>>> 467a644 (Add firebase to project):app/src/main/java/com/vnstudio/talktoai/ui/theme/Type.kt

// Set of Material typography styles to start with
val Typography = Typography(
    body1 = TextStyle(
        fontFamily = FontFamily(
            listOf(
                Font(R.font.robotomono_regular),
                Font(R.font.robotomono_italic)
            )
        ),
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    )
    /* Other default text styles to override
    button = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.W500,
        fontSize = 14.sp
    ),
    caption = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp
    )
    */
)