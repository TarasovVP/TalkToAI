package com.vnstudio.talktoai.presentation.components

import android.annotation.SuppressLint
import android.text.TextPaint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.navigator.Navigator
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.vnstudio.talktoai.R
import com.vnstudio.talktoai.domain.models.InfoMessage
import com.vnstudio.talktoai.infrastructure.Constants
import com.vnstudio.talktoai.infrastructure.Constants.APP_NETWORK_UNAVAILABLE_REPEAT
import com.vnstudio.talktoai.presentation.theme.Primary300
import com.vnstudio.talktoai.presentation.theme.Primary700
import com.vnstudio.talktoai.resources.DrawableResources
import com.vnstudio.talktoai.resources.StringResources
import com.vnstudio.talktoai.resources.getStringResourcesByLocale
import kotlinx.coroutines.flow.MutableStateFlow
import kotlin.math.ceil

@Composable
fun ExceptionMessageHandler(
    messageState: MutableState<InfoMessage?>,
    exceptionStateFlow: MutableStateFlow<String?>,
) {
    val exceptionState = exceptionStateFlow.collectAsState()
    val stringRes = LocalStringResources.current
    LaunchedEffect(exceptionState.value) {
        exceptionState.value.takeIf { exceptionState.value.isNullOrEmpty().not() }?.let {
            messageState.value = InfoMessage(
                if (exceptionState.value == APP_NETWORK_UNAVAILABLE_REPEAT) stringRes.APP_NETWORK_UNAVAILABLE_REPEAT else exceptionState.value.orEmpty(),
                Constants.ERROR_MESSAGE
            )
            exceptionStateFlow.value = null
        }
    }
}

@Composable
fun ProgressVisibilityHandler(
    progressVisibilityState: MutableState<Boolean>,
    progressVisibilityStateFlow: MutableStateFlow<Boolean>,
) {
    val progressProcessState = progressVisibilityStateFlow.collectAsState()
    LaunchedEffect(progressProcessState.value) {
        progressVisibilityState.value = progressProcessState.value
    }
}

@Composable
fun OrDivider(modifier: Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .weight(1f)
                .background(Primary300)
                .align(Alignment.CenterVertically)
        )
        Text(
            text = LocalStringResources.current.AUTHORIZATION_OR,
            fontSize = 16.sp,
            modifier = Modifier
                .weight(1f)
                .padding(vertical = 8.dp)
                .wrapContentSize()
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .weight(1f)
                .background(Primary300)
                .align(Alignment.CenterVertically)
        )
    }
}

@Composable
fun ShapeableImage(modifier: Modifier, drawableResId: String, contentDescription: String) {
    Image(
        painter = painterRes(drawableResId),
        contentDescription = contentDescription,
        modifier = modifier
            .fillMaxSize()
            .aspectRatio(1f)
            .clip(CircleShape)
            .background(Primary700),
        contentScale = ContentScale.Inside
    )
}

@Composable
fun EmptyState(text: String, modifier: Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = text, textAlign = TextAlign.Center, modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, Primary300, shape = RoundedCornerShape(18.dp))
                .padding(16.dp)
        )
        Image(
            painter = painterRes(DrawableResources.EMPTY_STATE),
            contentDescription = text,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(top = 16.dp)
        )
    }
}

@Composable
fun MainProgress(progressVisibilityState: MutableState<Boolean>) {

    if (progressVisibilityState.value) {
        Box(modifier = Modifier.fillMaxSize(), Alignment.Center) {
           /* CircularProgressIndicator(
                modifier = Modifier.size(100.dp),
                color = Primary700,
                strokeWidth = 5.dp
            )*/
        }
        val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.main_progress))
        Box(contentAlignment = Alignment.Center, modifier = Modifier
            .fillMaxSize()) {
            LottieAnimation(
                composition,
                iterations = LottieConstants.IterateForever,
                modifier = Modifier
                    .fillMaxSize(0.6f)
            )
        }
    }
}

@Composable
fun textLinesCount(text: String, paddings: Float, textSize: Float) : Int {
    val charsInLine = charsInLine(paddings, textSize)
    return charsInLine.takeIf { it > 0 }?.let { ceil((text.length / it).toDouble()).toInt() } ?: 1
}

@Composable
fun charsInLine(paddings: Float, textSize: Float) : Float {
    val screenWidth = measureScreenWidth() - paddings
    val charWidth = measureCharWidth(textSize)
    return charWidth.takeIf { it > 0 }?.let { screenWidth / it } ?: 0f
}

@Composable
fun measureScreenWidth(): Float {
    val screenWidthDp = LocalConfiguration.current.screenWidthDp.dp
    return screenWidthDp.value
}

@Composable
fun measureCharWidth(textSize: Float): Float {
    val density = LocalDensity.current.density
    val textPaint = TextPaint().apply {
        this.textSize = textSize * density
    }
    return textPaint.measureText(" ")
}

@Composable
fun getDimensionResource(resId: Int): Dp {
    val resources = LocalContext.current.resources
    val density = LocalDensity.current.density
    val sizeInPixels = resources.getDimension(resId)
    return (sizeInPixels / density).dp
}

@SuppressLint("DiscouragedApi")
@Composable
fun painterRes(resId: String): Painter {
    val context = LocalContext.current
    val resourceId = context.resources.getIdentifier(resId, "drawable", context.packageName)
    return painterResource(resourceId)
}

@Composable
fun LocalStringResources.current: StringResources {
    return getStringResourcesByLocale(Locale.current.language)
}

val LocalAppNavigator: ProvidableCompositionLocal<Navigator?> = staticCompositionLocalOf { null }

@Composable
fun ProvideAppNavigator(navigator: Navigator, content: @Composable () -> Unit) {
    CompositionLocalProvider(LocalAppNavigator provides navigator) {
        content()
    }
}