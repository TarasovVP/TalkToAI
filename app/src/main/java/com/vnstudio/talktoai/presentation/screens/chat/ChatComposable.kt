package com.vnstudio.talktoai.presentation.screens.chat

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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.*
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
import com.vnstudio.talktoai.CommonExtensions.isNotNull
import com.vnstudio.talktoai.R
import com.vnstudio.talktoai.data.database.db_entities.Chat
import com.vnstudio.talktoai.data.database.db_entities.Message
import com.vnstudio.talktoai.domain.ApiRequest
import com.vnstudio.talktoai.domain.models.InfoMessage
import com.vnstudio.talktoai.domain.models.MessageApi
import com.vnstudio.talktoai.isDefineSecondsLater
import com.vnstudio.talktoai.presentation.components.*
import com.vnstudio.talktoai.presentation.theme.*
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun ChatScreen(
    currentChatId: Long?,
    infoMessageState: MutableState<InfoMessage?>,
    progressVisibilityState: MutableState<Boolean>,
) {
    val viewModel: ChatViewModel = hiltViewModel()
    val showCreateChatDialog: MutableState<Boolean> = remember { mutableStateOf(false) }
    val currentChatState = viewModel.currentChatLiveData.observeAsState()
    val messagesState = viewModel.messagesLiveData.observeAsState()

    LaunchedEffect(Unit) {
        currentChatId?.let { viewModel.getCurrentChat(it) }
    }

    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Top
    ) {
        messagesState.value.takeIf { it.isNotNull() }?.let { messages ->
            MessagesScreen(messages, modifier = Modifier
                .weight(1f)
                .padding(horizontal = 16.dp)
            ) { messageText ->
                if (messageText.isEmpty()) {
                    Log.e("apiTAG", "ChatContent ChatTextField inputValue.value.text.isEmpty()")
                } else {
                    viewModel.insertMessage(
                        Message(
                            id = Date().time / 1000,
                            chatId = currentChatState.value?.id ?: 0,
                            author = "me",
                            message = messageText,
                            updatedAt = Date().time / 1000
                        )
                    )
                    val temporaryMessage = Message(
                        id = Date().time / 1000 + 1,
                        chatId = currentChatState.value?.id ?: 0,
                        author = "gpt-3.5-turbo",
                        message = String.EMPTY,
                        updatedAt = Date().time / 1000 + 1
                    )
                    viewModel.insertMessage(temporaryMessage)
                    viewModel.sendRequest(
                        temporaryMessage,
                        ApiRequest(
                            model = "gpt-3.5-turbo", temperature = 0.7f, messages = listOf(
                                MessageApi(role = "user", content = messageText)
                            )
                        )
                    )
                }
            }
        }
        currentChatState.value?.let { chat ->
            if (chat.id > 0) {
                viewModel.getMessagesFromChat(chat.id)
            } else {
                CreateChatScreen {
                    showCreateChatDialog.value = true
                }
            }
        }
    }

    DataEditDialog(
        "Создать новый чат?",
        "Название чата",
        mutableStateOf(TextFieldValue()),
        showCreateChatDialog,
        onDismiss = {
            showCreateChatDialog.value = false
        }) { newChatName ->
        viewModel.insertChat(Chat(id = Date().time, name = newChatName, updated = Date().time))
        showCreateChatDialog.value = false
    }

    ExceptionMessageHandler(infoMessageState, viewModel.exceptionLiveData)
    ProgressVisibilityHandler(progressVisibilityState, viewModel.progressVisibilityLiveData)
}

@Composable
fun CreateChatScreen(onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Top
    ) {
        EmptyState(
            text = "Что бы начать работу с ИИ создайте чат" ,
            modifier = Modifier
                .fillMaxSize()
                .padding(45.dp)
                .weight(1f)
        )
        TextIconButton(
            "Новый чат",
            R.drawable.ic_chat_add,
            Modifier.padding(start = 45.dp, end = 45.dp, bottom = 45.dp),
            onClick
        )
    }
}

@Composable
fun MessagesScreen(
    messages: List<Message>,
    modifier: Modifier = Modifier,
    onSendClick: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Top
    ) {
        if (messages.isEmpty()) {
            EmptyState(
                text = "Введите свой вопрос или воспользуйтесь микрофоном....",
                modifier = Modifier
                    .fillMaxSize()
                    .padding(45.dp)
                    .weight(1f)
            )
        } else {
            val scrollState = rememberLazyListState(initialFirstVisibleItemIndex = messages.lastIndex)
            Log.e(
                "scrollTAG",
                "ChatComposable MessagesScreen message.size ${messages.size} scrollState.firstVisibleItemIndex ${scrollState.firstVisibleItemIndex} firstVisibleItemScrollOffset ${scrollState.firstVisibleItemScrollOffset}"
            )
            Box(modifier = modifier) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    state = scrollState
                ) {
                    items(messages) { message ->
                        if (message.author == "me") {
                            UserMessage(message = message)
                        } else {
                            AIMessage(message = message) {

                            }
                        }
                    }
                }
            }
        }
        ChatTextField(inputValue = mutableStateOf( TextFieldValue()), onSendClick)
    }
}

@Composable
fun UserMessage(message: Message) {
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
                text = message.message,
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
fun AIMessage(message: Message, onLongClick: () -> Unit) {
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
            if (message.message.isEmpty()) {
                val format = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
                Log.e(
                    "timeTAG",
                    "ChatComposable AIMessage message.message ${message.message} message.updatedAt ${format.format(Date(message.updatedAt * 1000))} Date().time ${format.format((Date().time) + 20000)}"
                )
            }


            when {
                message.message.isEmpty() && Date().isDefineSecondsLater(20, message.updatedAt) -> Text(
                    text = "Error",
                    fontSize = 16.sp,
                    color = Color.Red,
                    modifier = Modifier
                        .padding(16.dp)
                        .wrapContentSize()
                )
                message.message.isEmpty() -> MessageTypingAnimation()
                else -> Text(
                    text = message.message,
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
fun MessageTypingAnimation() {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.message_typing))
    Box(
        Modifier
            .padding(16.dp)
    ) {
        LottieAnimation(
            composition,
            iterations = LottieConstants.IterateForever,
            modifier = Modifier
                .width(52.dp)
                .height(12.dp)
        )
    }
}