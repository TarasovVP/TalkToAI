package com.vnstudio.talktoai.presentation.chat

import android.util.Log
import androidx.compose.foundation.*
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
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
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
import com.vnstudio.talktoai.presentation.base.AddChatItem
import com.vnstudio.talktoai.presentation.components.ChatTextField
import com.vnstudio.talktoai.presentation.components.DataEditDialog
import com.vnstudio.talktoai.ui.theme.*
import java.util.*

@Composable
fun ChatScreen(showCreateDataDialog: MutableState<Boolean>) {

    val viewModel: ChatViewModel = hiltViewModel()
    val inputValue = remember { mutableStateOf(TextFieldValue()) }
    val currentChat = viewModel.currentChatLiveData.observeAsState()
    val messages = viewModel.messagesLiveData.observeAsState()
    //val loading = viewModel.isProgressProcessLiveData.observeAsState()

    LaunchedEffect(viewModel){
        viewModel.getCurrentChat()
    }

    Log.e(
        "apiTAG",
        "ChatComposable fun ChatContent messages.size ${messages.value?.size} "
    )
    Log.e("messagesTAG", "ChatComposable fun ChatContent messages ${messages.value?.map { it.message }}")

    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Top
    ) {
        if (messages.value.isNullOrEmpty()) {
            IntroMessage(currentChat.value == null,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(45.dp)
                    .weight(1f),
            )
        } else {
            MessageList(messages.value.orEmpty(), modifier = Modifier
                .weight(1f)
                .padding(horizontal = 16.dp))
        }
        if (currentChat.value == null) {
            AddChatItem(Modifier.padding(start = 45.dp, end = 45.dp, bottom = 45.dp)) {
                showCreateDataDialog.value = true
                Log.e("apiTAG", "ChatContent AddChatItem click")
            }
        } else {
            ChatTextField(inputValue = inputValue) { messageText ->
                if (messageText.isEmpty()) {
                    Log.e("apiTAG", "ChatContent ChatTextField inputValue.value.text.isEmpty()")
                } else {
                    viewModel.insertMessage(Message(chatId = currentChat.value?.chatId ?: 0, author = "me", message = messageText, createdAt = Date().time))
                    viewModel.insertMessage(Message(chatId = currentChat.value?.chatId ?: 0, author = "gpt-3.5-turbo", message = String.EMPTY, createdAt = 0))
                    viewModel.sendRequest(ApiRequest(model = "gpt-3.5-turbo", temperature = 0.7f, messages = listOf(
                        MessageApi(role = "user", content = messageText)
                    )))
                }
            }
        }
    }
    DataEditDialog("Создать новый чат?", "Имя чата", inputValue, showCreateDataDialog, onDismiss = {
        showCreateDataDialog.value = false
    }) { newChatName ->
        viewModel.insertChat(Chat(name = newChatName, updated = Date().time))
        inputValue.value = TextFieldValue(String.EMPTY)
        showCreateDataDialog.value = false
    }
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
                    AIMessage(text = message.message) {

                    }
                }
            }
        }
    }
    LaunchedEffect(messages.size) {
        scrollState.scrollToItem(messages.size - 1)
    }
}

@Composable
fun IntroMessage(isChatsEmpty: Boolean, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = if (isChatsEmpty) "Что бы начать работу с ИИ создайте чат" else "Введите свой вопрос или воспользуйтесь микрофоном....",
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
            .padding(vertical = 8.dp)
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

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AIMessage(text: String, onLongClick: () -> Unit) {
    Row(
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.Bottom,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .combinedClickable(
                onClick = { },
                onLongClick = onLongClick
            )
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
        .padding(16.dp)) {
        LottieAnimation(composition,
            iterations = LottieConstants.IterateForever,
            modifier = Modifier
                .width(52.dp)
                .height(12.dp))
    }
}