package com.vnstudio.talktoai.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.MutableLiveData
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.vnstudio.talktoai.R
import com.vnstudio.talktoai.domain.models.InfoMessage
import com.vnstudio.talktoai.infrastructure.Constants
import com.vnstudio.talktoai.presentation.theme.Primary300
import com.vnstudio.talktoai.presentation.theme.Primary700

@Composable
fun ExceptionMessageHandler(
    messageState: MutableState<InfoMessage?>,
    exceptionLiveData: MutableLiveData<String>,
) {
    val exceptionState = exceptionLiveData.observeAsState()
    LaunchedEffect(exceptionState.value) {
        exceptionState.value.takeIf { exceptionState.value.isNullOrEmpty().not() }?.let {
            messageState.value = InfoMessage(
                exceptionState.value.orEmpty(),
                Constants.ERROR_MESSAGE
            )
            exceptionLiveData.value = null
        }
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
            text = "Или",
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
fun ShapeableImage(modifier: Modifier, drawableResId: Int, contentDescription: String) {
    ContextCompat.getDrawable(LocalContext.current, drawableResId)?.toBitmap()?.asImageBitmap()
        ?.let { painterResource(id = drawableResId) }?.let {
        Image(
            painter = it,
            contentDescription = contentDescription,
            modifier = modifier
                .fillMaxSize()
                .aspectRatio(1f)
                .clip(CircleShape)
                .background(Primary700),
            contentScale = ContentScale.Inside
        )
    }
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
            painter = painterResource(id = R.drawable.empty_state),
            contentDescription = "Empty state",
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(top = 16.dp)
        )
    }
}

@Composable
fun MainProgress(isMainProgressVisible: MutableState<Boolean>) {
    if (isMainProgressVisible.value) {
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