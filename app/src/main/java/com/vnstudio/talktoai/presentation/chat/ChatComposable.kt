package com.vnstudio.talktoai.presentation.chat

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.*
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.airbnb.lottie.LottieDrawable
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.vnstudio.talktoai.CommonExtensions.EMPTY
import com.vnstudio.talktoai.R
import com.vnstudio.talktoai.data.database.db_entities.Chat
import com.vnstudio.talktoai.data.database.db_entities.Message
import com.vnstudio.talktoai.domain.ApiRequest
import com.vnstudio.talktoai.domain.models.MessageApi
import com.vnstudio.talktoai.ui.theme.*
import java.util.*

@Composable
fun ChatContent() {

    val viewModel: ChatViewModel = viewModel()
    val inputValue = remember { mutableStateOf(TextFieldValue()) }
    val currentChat = viewModel.currentChatLiveData.observeAsState(Chat())
    val messages = viewModel.messagesLiveData.observeAsState()
    val loading = viewModel.isProgressProcessLiveData.observeAsState()

    Log.e(
        "apiTAG",
        "ChatComposable fun ChatContent messages.size ${messages.value?.size} loading $loading"
    )

    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Top
    ) {
        if (messages.value.isNullOrEmpty()) {
            IntroMessage(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(45.dp)
                    .weight(1f)
            )
        } else {
            MessageList(messages.value.orEmpty(), modifier = Modifier
                .weight(1f)
                .padding(horizontal = 16.dp))
        }
        ChatTextField(inputValue = inputValue) {
            if (inputValue.value.text.isEmpty()) {
                Log.e("apiTAG", "ChatContent ChatTextField inputValue.value.text.isEmpty()")
            } else {
                viewModel.sendRequest(currentChat.value.chatId, ApiRequest(model = "gpt-3.5-turbo", temperature = 0.7f, messages = listOf(
                    MessageApi(role = "user", content = inputValue.value.text)
                ))
                )
                viewModel.insertMessage(Message(chatId = currentChat.value.chatId, author = "me", message = inputValue.value.text, createdAt = Date().time))
                viewModel.insertMessage(Message(chatId = currentChat.value.chatId, author = "gpt-3.5-turbo", message = String.EMPTY, createdAt = 0))
                inputValue.value = TextFieldValue(String.EMPTY)
            }
        }
    }

    //if (loading.value == true) MessageTypingAnimation()
}

@Composable
fun MessageList(messages: List<Message>, modifier: Modifier = Modifier) {
    val scrollState = rememberLazyListState()
    Box(modifier = modifier) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            state = scrollState
        ) {
            items(messages) { message ->
                if (message.author == "me") {
                    UserMessage(text = message.message)
                } else {
                    AIMessage(text = message.message)
                }
            }
        }
    }
    LaunchedEffect(messages.size) {
        scrollState.scrollToItem(messages.size - 1)
    }
}

@Composable
fun IntroMessage(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = "You haven't got messages. Start a conversation with AI.",
            fontSize = 16.sp,
            modifier = Modifier
                .wrapContentSize()
                .border(1.dp, Primary300, shape = RoundedCornerShape(18.dp))
                .padding(16.dp)
        )
    }
}

@Composable
fun UserMessage(text: String) {
    Row(
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp)
    ) {
        Box(
            modifier = Modifier
                .wrapContentSize()
                .widthIn(0.dp, (LocalConfiguration.current.screenWidthDp * 0.8).dp)
                .background(
                    color = Primary500,
                    shape = RoundedCornerShape(
                        topStart = 16.dp,
                        topEnd = 16.dp,
                        bottomStart = 16.dp,
                        bottomEnd = 2.dp
                    )
                )
        ) {
            Text(
                text = text,
                fontSize = 16.sp,
                color = Color.White,
                modifier = Modifier
                    .padding(10.dp)
                    .wrapContentSize()

            )
        }
    }
}

@Composable
fun AIMessage(text: String) {
    Row(
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.Bottom,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp)
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(R.drawable.avatar_ai)
                .crossfade(true)
                .build(),
            contentDescription = "AI avatar",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .clip(CircleShape)
                .size(32.dp)
        )
        Box(
            modifier = Modifier
                .wrapContentSize()
                .padding(start = 8.dp)
                .widthIn(0.dp, (LocalConfiguration.current.screenWidthDp * 0.8).dp)
                .background(
                    color = Primary600,
                    shape = RoundedCornerShape(
                        topStart = 16.dp,
                        topEnd = 16.dp,
                        bottomStart = 2.dp,
                        bottomEnd = 16.dp
                    )
                )
        ) {
            if (text.isEmpty()) {
                MessageTypingAnimation()
            } else {
                Text(
                    text = text,
                    fontSize = 16.sp,
                    color = Color.White,
                    modifier = Modifier
                        .padding(16.dp)
                        .wrapContentSize()

                )
            }
        }
    }
}

@Composable
fun ChatTextField(
    inputValue: MutableState<TextFieldValue>,
    onSendClick: (MutableState<TextFieldValue>) -> Unit
) {
    val focusManager = LocalFocusManager.current
    Box(modifier = Modifier
        .fillMaxWidth()
        .background(color = Primary900)
        .height(IntrinsicSize.Min)) {
        TextField(value = inputValue.value,
            onValueChange = { newValue ->
            inputValue.value = newValue
        }, placeholder = { Text(text = "Enter request") },
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color.White,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            ),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .border(1.dp, Primary500, shape = RoundedCornerShape(16.dp))
                .background(
                    color = Color.White,
                    shape = RoundedCornerShape(16.dp)
                ),
            maxLines = 6,
            trailingIcon = {
            Box(
                modifier = Modifier
                    .clickable {
                        onSendClick.invoke(inputValue)
                        focusManager.clearFocus()

                    }
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(if (inputValue.value.text.isEmpty()) R.drawable.ic_voice_record else R.drawable.ic_message_send),
                    contentDescription = "Send message button",
                    tint = Primary900
                )
            }
        }
        )
    }
}

@Composable
fun CircularProgressBar() {
    Box(contentAlignment = Alignment.Center) {
        CircularProgressIndicator(
            modifier = Modifier
                .fillMaxSize(0.25f),
            color = Color.Green
        )
    }
}

@Composable
fun MessageTypingAnimation() {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.message_typing))
    Box(Modifier
        .padding(vertical = 8.dp)) {
        LottieAnimation(composition,
            iterations = LottieConstants.IterateForever,
            modifier = Modifier
                .width(100.dp)
                .height(30.dp))
    }
}