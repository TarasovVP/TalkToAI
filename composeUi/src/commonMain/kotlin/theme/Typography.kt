package theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import com.vnteam.talktoai.Res
import com.vnteam.talktoai.presentation.resources.default_text_size
import com.vnteam.talktoai.robotomono_italic
import com.vnteam.talktoai.robotomono_regular


@Composable
fun Typography() = Typography(
    bodySmall = TextStyle(
        fontWeight = FontWeight.Light,
        fontSize = default_text_size.textSize,
        fontFamily = FontFamily(
            org.jetbrains.compose.resources.Font(Res.font.robotomono_italic, FontWeight.Normal)
        )
    ),
    bodyMedium = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = default_text_size.textSize,
        fontFamily = FontFamily(
            org.jetbrains.compose.resources.Font(Res.font.robotomono_regular, FontWeight.Normal)
        )
    ),
    bodyLarge = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = default_text_size.textSize,
        fontFamily = FontFamily(
            org.jetbrains.compose.resources.Font(Res.font.robotomono_regular, FontWeight.Bold)
        )
    )
)