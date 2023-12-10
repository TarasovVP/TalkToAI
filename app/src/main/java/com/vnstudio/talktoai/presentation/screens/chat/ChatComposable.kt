package com.vnstudio.talktoai.presentation.screens.chat

import android.content.Context
import android.content.Intent
import android.speech.tts.TextToSpeech
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
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
import com.vnstudio.talktoai.*
import com.vnstudio.talktoai.CommonExtensions.EMPTY
import com.vnstudio.talktoai.CommonExtensions.isNotNull
import com.vnstudio.talktoai.CommonExtensions.isTrue
import com.vnstudio.talktoai.R
import com.vnstudio.talktoai.data.database.db_entities.Chat
import com.vnstudio.talktoai.domain.ApiRequest
import com.vnstudio.talktoai.domain.enums.MessageStatus
import com.vnstudio.talktoai.domain.models.InfoMessage
import com.vnstudio.talktoai.domain.models.MessageApi
import com.vnstudio.talktoai.infrastructure.Constants.APP_LANG_RU
import com.vnstudio.talktoai.infrastructure.Constants.DEFAULT_CHAT_ID
import com.vnstudio.talktoai.presentation.components.*
import com.vnstudio.talktoai.presentation.components.draggable.UpdateViewConfiguration
import com.vnstudio.talktoai.presentation.theme.*
import com.vnstudio.talktoai.presentation.ui_models.MessageUIModel
import java.util.*

@Composable
fun ChatScreen(
    currentChatId: Long,
    isMessageDeleteModeState: MutableState<Boolean?>,
    infoMessageState: MutableState<InfoMessage?>,
    progressVisibilityState: MutableState<Boolean>,
) {
    val viewModel: ChatViewModel = hiltViewModel()
    val showCreateChatDialog: MutableState<Boolean> = remember { mutableStateOf(false) }
    val currentChatState = viewModel.currentChatLiveData.observeAsState()
    val messagesState = viewModel.messagesLiveData.observeAsState()

    LaunchedEffect(Unit) {
        Log.e("apiTAG", "ChatScreen getCurrentChat currentChatId $currentChatId")
        viewModel.getCurrentChat(currentChatId)
    }

    LaunchedEffect(currentChatState.value) {
        currentChatState.value?.let { chat ->
            viewModel.getMessagesFromChat(chat.id)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Top
    ) {
        Box(modifier = Modifier.weight(1f)) {
            messagesState.value.takeIf { it.isNotNull() }?.let { messages ->
                MessagesList(
                    messages, isMessageDeleteModeState, modifier = Modifier
                        .padding(horizontal = 16.dp)
                )
            }
            Log.e("apiTAG", "ChatScreen Column currentChatState.value ${currentChatState.value}")
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Primary900)
        ) {
            when {
                currentChatState.value?.id == DEFAULT_CHAT_ID -> CreateChatScreen {
                    showCreateChatDialog.value = true
                }

                isMessageDeleteModeState.value.isTrue() -> {
                    val clipboardManager = LocalClipboardManager.current
                    val shareIntentLauncher =
                        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { _ ->
                            Log.e(
                                "apiTAG",
                                "ChatScreen MessageDeleteField onShareClick shareIntentLauncher onComplete"
                            )
                        }
                    MessageDeleteField(
                        onCancelClick = {
                            messagesState.value.clearCheckToAction()
                            isMessageDeleteModeState.value = false
                        },
                        onCopyClick = {
                            clipboardManager.setText(AnnotatedString(messagesState.value.textToAction()))
                            messagesState.value.clearCheckToAction()
                            isMessageDeleteModeState.value = false
                        },
                        onDeleteClick = {
                            Log.e(
                                "apiTAG",
                                "ChatScreen MessageDeleteField onDeleteClick messagesState.value isCheckedToDelete ${messagesState.value?.filter { it.isCheckedToDelete.value }}"
                            )
                            messagesState.value.clearCheckToAction()
                            isMessageDeleteModeState.value = false
                        },
                        onShareClick = {

                            Intent(Intent.ACTION_SEND).apply {
                                type = "text/plain"
                                putExtra(Intent.EXTRA_TEXT, messagesState.value.textToAction())

                                val chooser = Intent.createChooser(this, "Share Text")
                                shareIntentLauncher.launch(chooser)
                            }

                            messagesState.value.clearCheckToAction()
                            isMessageDeleteModeState.value = false

                            Log.e(
                                "apiTAG",
                                "ChatScreen MessageDeleteField onShareClick messagesState.value isCheckedToDelete ${messagesState.value?.filter { it.isCheckedToDelete.value }}"
                            )

                            //TODO
                            /*iniTextToSpeech(context) {textToSpeech ->
                                val text =
                                    messagesState.value?.filter { it.isCheckedToDelete.value }?.joinToString { it.message }.orEmpty()
                                Log.e(
                                    "textToSpeechTAG",
                                    "ChatComposable onShareClick text $text"
                                )
                                textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
                            }*/
                        })
                }

                currentChatState.value.isNotNull() && currentChatState.value?.id != DEFAULT_CHAT_ID -> {
                    TextFieldWithButton(
                        (currentChatState.value?.id ?: DEFAULT_CHAT_ID) != DEFAULT_CHAT_ID,
                        inputValue = remember {
                            mutableStateOf(TextFieldValue())
                        }
                    ) { messageText ->
                        if (messageText.isEmpty()) {
                            Log.e(
                                "apiTAG",
                                "ChatContent ChatTextField inputValue.value.text.isEmpty()"
                            )
                        } else {
                            viewModel.insertMessage(
                                MessageUIModel(
                                    id = Date().time,
                                    chatId = currentChatState.value?.id ?: 0,
                                    author = "me",
                                    message = messageText,
                                    updatedAt = Date().dateToMilliseconds(),
                                    status = MessageStatus.SUCCESS
                                )
                            )
                            val temporaryMessage = MessageUIModel(
                                id = Date().time + 1,
                                chatId = currentChatState.value?.id ?: 0,
                                author = "gpt-3.5-turbo",
                                message = String.EMPTY,
                                updatedAt = Date().dateToMilliseconds() + 1,
                                status = MessageStatus.REQUESTING
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
            }
        }
    }

    DataEditDialog(
        "Создать новый чат?",
        "Название чата",
        remember {
            mutableStateOf(TextFieldValue())
        },
        showCreateChatDialog,
        onDismiss = {
            showCreateChatDialog.value = false
        }) { newChatName ->
        viewModel.insertChat(
            Chat(
                id = Date().dateToMilliseconds(),
                name = newChatName,
                updated = Date().dateToMilliseconds()
            )
        )
        showCreateChatDialog.value = false
    }

    ExceptionMessageHandler(infoMessageState, viewModel.exceptionLiveData)
    ProgressVisibilityHandler(progressVisibilityState, viewModel.progressVisibilityLiveData)
}

@Composable
fun MessagesList(
    messages: List<MessageUIModel>,
    isMessageDeleteModeState: MutableState<Boolean?>,
    modifier: Modifier = Modifier,
) {

    if (messages.isEmpty()) {
        EmptyState(
            text = "Введите свой вопрос или воспользуйтесь микрофоном....",
            modifier = Modifier
                .fillMaxSize()
                .padding(45.dp)
        )
    } else {
        val scrollState = rememberLazyListState(initialFirstVisibleItemIndex = messages.lastIndex)

        UpdateViewConfiguration(
            longPressTimeoutMillis = 200L
        ) {
            Box(modifier = modifier) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    state = scrollState
                ) {
                    items(messages) { message ->
                        Message(
                            isUserAuthor = message.author == "me",
                            message = message,
                            isMessageDeleteModeState = isMessageDeleteModeState
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun Message(
    isUserAuthor: Boolean,
    message: MessageUIModel,
    isMessageDeleteModeState: MutableState<Boolean?>,
) {
    val linesCount = remember { mutableIntStateOf(1) }
    val isTruncated = remember { mutableStateOf(false) }

    Row(
        horizontalArrangement = if (isUserAuthor) Arrangement.End else Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .animateContentSize(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioNoBouncy,
                    stiffness = Spring.StiffnessMedium
                )
            )
            .pointerInput(isMessageDeleteModeState.value) {
                if (isMessageDeleteModeState.value.isTrue()) {
                    detectTapGestures(onTap = {
                        message.isCheckedToDelete.value = message.isCheckedToDelete.value.not()
                    })
                } else {
                    detectTapGestures(onLongPress = {
                        isMessageDeleteModeState.value = true
                    })
                }
            }
    ) {
        if (isMessageDeleteModeState.value.isTrue()) {
            Box(modifier = Modifier.size(32.dp)) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(if (message.isCheckedToDelete.value) R.drawable.ic_checked_check_box else R.drawable.ic_empty_check_box)
                        .crossfade(true)
                        .build(),
                    contentDescription = "AI avatar",
                    contentScale = ContentScale.Inside,
                    modifier = Modifier.size(24.dp)
                )
            }
            Spacer(modifier = Modifier.weight(1f))
        } else if (isUserAuthor.not()) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(R.drawable.avatar_ai)
                    .crossfade(true)
                    .build(),
                contentDescription = "AI avatar",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(32.dp)
            )
        }
        Box(
            modifier = Modifier
                .wrapContentSize()
                .widthIn(0.dp, (LocalConfiguration.current.screenWidthDp * 0.8).dp)
                .background(
                    color = if (isUserAuthor) Primary500 else Primary600,
                    shape = RoundedCornerShape(
                        topStart = 16.dp,
                        topEnd = 16.dp,
                        bottomStart = if (isUserAuthor) 16.dp else 2.dp,
                        bottomEnd = if (isUserAuthor) 2.dp else 16.dp
                    )
                )
        ) {
            when {
                message.status == MessageStatus.ERROR -> Text(
                    text = message.errorMessage,
                    fontSize = 16.sp,
                    color = Color.Red,
                    modifier = Modifier
                        .padding(8.dp)
                        .wrapContentSize()
                )

                message.status == MessageStatus.REQUESTING && Date().isDefineSecondsLater(
                    20,
                    message.updatedAt
                ) -> Text(
                    text = "Неизвестная ошибка",
                    fontSize = 16.sp,
                    color = Color.Red,
                    modifier = Modifier
                        .padding(8.dp)
                        .wrapContentSize()
                )

                message.status == MessageStatus.REQUESTING -> MessageTypingAnimation()
                else -> TruncatableText(
                    message = message.message,
                    isTruncated = isTruncated,
                    linesCount = linesCount
                )
            }
        }
    }
}


    @Composable
    fun CreateChatScreen(onClick: () -> Unit) {
        Row(
            Modifier
                .padding(16.dp)
                .height(TextFieldDefaults.MinHeight)
        ) {
            TextIconButton(
                "Новый чат",
                R.drawable.ic_chat_add,
                Modifier,
                onClick
            )
        }
    }

    @Composable
    fun MessageDeleteField(
        onCancelClick: () -> Unit,
        onCopyClick: () -> Unit,
        onDeleteClick: () -> Unit,
        onShareClick: () -> Unit,
    ) {
        Row(
            Modifier
                .padding(16.dp)
                .height(TextFieldDefaults.MinHeight)
        ) {
            TextButton(onClick = onCancelClick) {
                Text(text = stringResource(id = R.string.button_cancel), color = Neutral50)
            }
            Spacer(modifier = Modifier.weight(1f))
            IconButton(onClick = onCopyClick) {
                Image(
                    painter = painterResource(id = R.drawable.ic_copy),
                    contentDescription = "Message copy button"
                )
            }
            IconButton(onClick = onDeleteClick) {
                Image(
                    painter = painterResource(id = R.drawable.ic_delete),
                    contentDescription = "Message delete button"
                )
            }
            IconButton(onClick = onShareClick) {
                Image(
                    painter = painterResource(id = R.drawable.ic_share),
                    contentDescription = "Message share button"
                )
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