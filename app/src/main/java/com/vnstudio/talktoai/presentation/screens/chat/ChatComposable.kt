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
import com.vnstudio.talktoai.presentation.components.*
import com.vnstudio.talktoai.presentation.theme.*
import java.util.*

@Composable
fun ChatScreen(
    chatsState: MutableState<List<Chat>>,
    openedChatState: MutableState<Chat?>,
    showCreateDataDialog: MutableState<Boolean>,
    showEditDataDialog: MutableState<Boolean>,
    deleteChatState: MutableState<Chat?>,
    infoMessageState: MutableState<InfoMessage?>
) {

    val viewModel: ChatViewModel = hiltViewModel()
    val chats = viewModel.chatsLiveData.observeAsState(listOf())
    val showConfirmationDialog = remember { mutableStateOf(deleteChatState.value.isNotNull()) }
    val inputValue = remember { mutableStateOf(TextFieldValue(String.EMPTY)) }

    LaunchedEffect(viewModel){
        viewModel.getChats()
    }
    LaunchedEffect(chats.value){
        chats.value?.let { chats ->
            chatsState.value = chats
        }
    }
    LaunchedEffect(openedChatState.value){
        openedChatState.value?.let { chat ->
            viewModel.updateChats(viewModel.chatsLiveData.value.orEmpty().onEach { if (it.chatId == chat.chatId) it.updated = Date().time })
        }
    }
    LaunchedEffect(deleteChatState.value){
        showConfirmationDialog.value = deleteChatState.value.isNotNull()
    }

    Log.e(
        "apiTAG",
        "MainScreen chats ${chats.value}"
    )

    MessagesScreen(viewModel = viewModel, showCreateDataDialog = showCreateDataDialog)

    inputValue.value = TextFieldValue( if (showCreateDataDialog.value) "Без названия" else chats.value.firstOrNull()?.name.orEmpty())

    DataEditDialog("Создать новый чат?", "Имя чата", inputValue, showCreateDataDialog, onDismiss = {
        showCreateDataDialog.value = false
    }) { newChatName ->
        viewModel.insertChat(Chat(name = newChatName, updated = Date().time))
        showCreateDataDialog.value = false
        /*scope.launch {
            scaffoldState.drawerState.close()
        }*/
    }
    DataEditDialog("Edit chat name", "Chat name", inputValue, showEditDataDialog, onDismiss = {
        showEditDataDialog.value = false
    }) { newChatName ->
        viewModel.updateChat(chats.value.orEmpty().first().apply {
            name = newChatName
        })
        showEditDataDialog.value = false
    }

    ConfirmationDialog("Delete chat?", showConfirmationDialog, onDismiss = {
        showConfirmationDialog.value = false
    }) {
        deleteChatState.value?.let { viewModel.deleteChat(it) }
        showConfirmationDialog.value = false
        deleteChatState.value = null
    }

    ExceptionMessageHandler(infoMessageState, viewModel.exceptionLiveData)
}

@Composable
fun MessagesScreen(viewModel: ChatViewModel, showCreateDataDialog: MutableState<Boolean>) {

    val inputValue = remember { mutableStateOf(TextFieldValue()) }
    val currentChatState = viewModel.currentChatLiveData.observeAsState()
    val messagesState = viewModel.messagesLiveData.observeAsState()

    LaunchedEffect(Unit) {
        viewModel.getCurrentChat()
    }

    Log.e(
        "apiTAG",
        "ChatComposable fun ChatContent messages.size ${messagesState.value?.size} "
    )
    Log.e("messagesTAG", "ChatComposable fun ChatContent messages ${messagesState.value?.map { it.message }}")

    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Top
    ) {
        if (messagesState.value.isNullOrEmpty()) {
            IntroMessage(currentChatState.value == null,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(45.dp)
                    .weight(1f),
            )
        } else {
            MessageList(messagesState.value.orEmpty(), modifier = Modifier
                .weight(1f)
                .padding(horizontal = 16.dp))
        }
        if (currentChatState.value == null) {
            TextIconButton("Новый чат", R.drawable.ic_chat_add, Modifier.padding(start = 45.dp, end = 45.dp, bottom = 45.dp)) {
                showCreateDataDialog.value = true
                Log.e("apiTAG", "ChatContent AddChatItem click")
            }
        } else {
            ChatTextField(inputValue = inputValue) { messageText ->
                if (messageText.isEmpty()) {
                    Log.e("apiTAG", "ChatContent ChatTextField inputValue.value.text.isEmpty()")
                } else {
                    viewModel.insertMessage(Message(chatId = currentChatState.value?.chatId ?: 0, author = "me", message = messageText, createdAt = Date().time))
                    viewModel.insertMessage(Message(chatId = currentChatState.value?.chatId ?: 0, author = "gpt-3.5-turbo", message = String.EMPTY, createdAt = 0))
                    viewModel.sendRequest(ApiRequest(model = "gpt-3.5-turbo", temperature = 0.7f, messages = listOf(
                        MessageApi(role = "user", content = messageText)
                    )))
                }
            }
        }
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