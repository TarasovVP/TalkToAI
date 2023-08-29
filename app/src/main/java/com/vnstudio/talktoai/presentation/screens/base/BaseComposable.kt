package com.vnstudio.talktoai.presentation.screens.base

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.MutableLiveData
import com.vnstudio.talktoai.R
import com.vnstudio.talktoai.domain.models.InfoMessage
import com.vnstudio.talktoai.infrastructure.Constants
import com.vnstudio.talktoai.presentation.theme.Neutral50
import com.vnstudio.talktoai.presentation.theme.Primary300
import com.vnstudio.talktoai.presentation.theme.Primary500
import com.vnstudio.talktoai.presentation.theme.Primary700


@Composable
fun ExceptionMessageHandler(messageState: MutableState<InfoMessage?>, exceptionLiveData: MutableLiveData<String>) {
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
fun AddChatItem(modifier: Modifier, onAddChatClicked: () -> Unit) {
    Row(modifier = modifier
        .fillMaxWidth()
        .background(color = Primary700, shape = RoundedCornerShape(16.dp))
        .clickable {
            onAddChatClicked.invoke()
        }) {
        Image(
            imageVector = ImageVector.vectorResource(id = R.drawable.ic_chat_add),
            contentDescription = "Add chat button",
            modifier = Modifier
                .padding(8.dp)
        )
        Text(
            text = "New chat",
            fontSize = 16.sp,
            color = Neutral50,
            modifier = Modifier.weight(1f).padding(vertical = 8.dp)
        )
    }
}

@Composable
fun OrDivider(modifier: Modifier) {
    Row(modifier = modifier
        .fillMaxWidth()) {
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
            modifier = Modifier.weight(1f).padding(vertical = 8.dp).wrapContentSize()
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
    ContextCompat.getDrawable(LocalContext.current, drawableResId)?.toBitmap()?.asImageBitmap()?.let { painterResource(id = drawableResId) }?.let {
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
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 32.dp)
                .background(color = Primary500, shape = RoundedCornerShape(16.dp))
        ) {
            Text(text = text, textAlign = TextAlign.Center, color = Neutral50, modifier = Modifier
                .fillMaxWidth().padding(16.dp))
        }
        Image(painter = painterResource(id = R.drawable.empty_state), contentDescription = "Empty state", modifier = Modifier
            .fillMaxWidth()
            .weight(1f)
            .padding(top = 16.dp))
    }
}