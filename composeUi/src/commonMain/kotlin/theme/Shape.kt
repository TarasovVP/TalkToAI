package theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import com.vnteam.talktoai.presentation.resources.default_padding
import com.vnteam.talktoai.presentation.resources.medium_padding
import com.vnteam.talktoai.presentation.resources.small_padding

val Shapes = Shapes(
    small = RoundedCornerShape(small_padding.size.value),
    medium = RoundedCornerShape(medium_padding.size.value),
    large = RoundedCornerShape(default_padding.size.value)
)