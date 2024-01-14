package com.vnstudio.talktoai.presentation.screens.chat

import android.content.Intent
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
import com.google.accompanist.insets.navigationBarsWithImePadding
import com.vnstudio.talktoai.*
import com.vnstudio.talktoai.CommonExtensions.EMPTY
import com.vnstudio.talktoai.CommonExtensions.isNotNull
import com.vnstudio.talktoai.CommonExtensions.isNotTrue
import com.vnstudio.talktoai.CommonExtensions.isTrue
import com.vnstudio.talktoai.R
import com.vnstudio.talktoai.data.database.db_entities.Chat
import com.vnstudio.talktoai.domain.ApiRequest
import com.vnstudio.talktoai.domain.enums.MessageStatus
import com.vnstudio.talktoai.domain.models.InfoMessage
import com.vnstudio.talktoai.domain.models.MessageApi
import com.vnstudio.talktoai.domain.sealed_classes.MessageAction
import com.vnstudio.talktoai.infrastructure.Constants.DEFAULT_CHAT_ID
import com.vnstudio.talktoai.presentation.components.*
import com.vnstudio.talktoai.presentation.components.draggable.UpdateViewConfiguration
import com.vnstudio.talktoai.presentation.theme.*
import com.vnstudio.talktoai.presentation.ui_models.MessageUIModel
import java.util.*

@Composable
fun ChatScreen(
    currentChatId: Long,
    isMessageActionModeState: MutableState<Boolean?>,
    infoMessageState: MutableState<InfoMessage?>,
    progressVisibilityState: MutableState<Boolean>,
) {
    val viewModel: ChatViewModel = hiltViewModel()
    val showCreateChatDialog: MutableState<Boolean> = remember { mutableStateOf(false) }
    val currentChatState = viewModel.currentChatLiveData.observeAsState()
    val messagesState = viewModel.messagesLiveData.observeAsState()
    val messageActionState: MutableState<MessageAction> = remember { mutableStateOf(MessageAction.Cancel) }

    LaunchedEffect(Unit) {
        Log.e("apiTAG", "ChatScreen getCurrentChat currentChatId $currentChatId")
        viewModel.getCurrentChat(currentChatId)
    }

    LaunchedEffect(currentChatState.value) {
        currentChatState.value?.let { chat ->
            viewModel.getMessagesFromChat(chat.id)
        }
    }

    LaunchedEffect(isMessageActionModeState.value.isTrue() && messagesState.value?.none { it.isCheckedToDelete.value }.isTrue()) {
        isMessageActionModeState.value = false
    }

    val clipboardManager = LocalClipboardManager.current
    val shareIntentLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { _ ->
            Log.e(
                "apiTAG",
                "ChatScreen MessageDeleteField onShareClick shareIntentLauncher onComplete"
            )
        }
    LaunchedEffect(messageActionState.value) {
        when(messageActionState.value) {
            is MessageAction.Delete -> {
                viewModel.deleteMessages(messagesState.value?.filter { it.isCheckedToDelete.value }?.map { it.id } ?: listOf() )
                messagesState.value.clearCheckToAction()
                isMessageActionModeState.value = false
            }
            is MessageAction.Copy -> {
                clipboardManager.setText(AnnotatedString(messagesState.value.textToAction()))
                messagesState.value.clearCheckToAction()
                isMessageActionModeState.value = false
            }
            is MessageAction.Share -> {
                Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    putExtra(Intent.EXTRA_TEXT, messagesState.value.textToAction())

                    val chooser = Intent.createChooser(this, "Share Text")
                    shareIntentLauncher.launch(chooser)
                }

                messagesState.value.clearCheckToAction()
                isMessageActionModeState.value = false
            }
            is MessageAction.Transfer -> {
                //TODO
            }

            else -> {
                messagesState.value?.clearCheckToAction()
                isMessageActionModeState.value = false
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .navigationBarsWithImePadding(),
        verticalArrangement = Arrangement.Top
    ) {
        Box(modifier = Modifier.weight(1f)) {
            messagesState.value.takeIf { it.isNotNull() }?.let { messages ->
                MessagesList(
                    messages, isMessageActionModeState, modifier = Modifier
                        .padding(horizontal = 16.dp)
                ) { message ->
                    viewModel.updateMessage(message)
                }
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

                isMessageActionModeState.value.isTrue() -> {
                    MessageActionField(messageActionState)
                }

                currentChatState.value.isNotNull() && currentChatState.value?.id != DEFAULT_CHAT_ID -> {
                    val inputValue = remember {
                        mutableStateOf(TextFieldValue())
                    }
                    TextFieldWithButton(
                        (currentChatState.value?.id ?: DEFAULT_CHAT_ID) != DEFAULT_CHAT_ID,
                        inputValue = inputValue
                    ) { messageText ->

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
    isMessageActionModeState: MutableState<Boolean?>,
    modifier: Modifier = Modifier,
    onMessageChange: (MessageUIModel) -> Unit = {}
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

        LaunchedEffect(messages.size) {
            scrollState.animateScrollToItem(index = messages.size - 1)
        }

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
                            isMessageDeleteModeState = isMessageActionModeState,
                            onMessageChange
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
    onMessageChange: (MessageUIModel) -> Unit = {},
) {
    val isTruncatedState = remember { mutableStateOf(message.isTruncated) }

    LaunchedEffect(isTruncatedState.value) {
        onMessageChange(message.copy(isTruncated = isTruncatedState.value))
    }

    val paddings = getDimensionResource(resId = R.dimen.large_padding).value + getDimensionResource(resId = if (isUserAuthor) R.dimen.large_padding else R.dimen.small_padding).value + getDimensionResource(resId = R.dimen.default_text_size).value * 2 + getDimensionResource(resId = R.dimen.default_text_size).value * 2 + if (isUserAuthor) 0f else getDimensionResource(resId = R.dimen.avatar_size).value
    val linesCount = textLinesCount(message.message, paddings, getDimensionResource(resId = R.dimen.default_text_size).value)
    val changedMessage = if (linesCount > 2 && isTruncatedState.value) message.message.substring(0, charsInLine(paddings, getDimensionResource(resId = R.dimen.default_text_size).value).toInt()) + "..." else message.message
    Log.e("charWidthTAG", "ChatComposable: message.message ${message.message.takeIf { it.length > 6 }?.substring(0, 6) } message.length ${message.message.length }")
    Row(
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
                        message.isCheckedToDelete.value = message.isCheckedToDelete.value.not()
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
        }
        Row(
            horizontalArrangement = if (isUserAuthor) Arrangement.End else Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            if (isUserAuthor.not() && isMessageDeleteModeState.value.isNotTrue()) {
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
            Spacer(modifier = Modifier.width(4.dp))
            Box(
                modifier = Modifier
                    .wrapContentSize()
                    .widthIn(0.dp, (LocalConfiguration.current.screenWidthDp * 0.8).dp)
                    .background(
                        color = if (isUserAuthor) Primary500 else Primary600,
                        shape = RoundedCornerShape(
                            topStart = 16.dp,
                            topEnd = getDimensionResource(resId = R.dimen.large_padding),
                            bottomStart = getDimensionResource(resId = if (isUserAuthor) R.dimen.large_padding else R.dimen.small_padding),
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
                        message = changedMessage,
                        isTruncated = isTruncatedState,
                        linesCount = linesCount
                    )
                }
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
    fun MessageActionField(
        messageActionState: MutableState<MessageAction>
    ) {
        Row(
            Modifier
                .padding(16.dp)
                .height(TextFieldDefaults.MinHeight)
        ) {
            TextButton(onClick = { messageActionState.value = MessageAction.Cancel }) {
                Text(text = stringResource(id = R.string.button_cancel), color = Neutral50)
            }
            Spacer(modifier = Modifier.weight(1f))
            IconButton(onClick = { messageActionState.value = MessageAction.Copy }) {
                Image(
                    painter = painterResource(id = R.drawable.ic_copy),
                    contentDescription = "Message copy button"
                )
            }
            IconButton(onClick = { messageActionState.value = MessageAction.Delete }) {
                Image(
                    painter = painterResource(id = R.drawable.ic_delete),
                    contentDescription = "Message delete button"
                )
            }
            IconButton(onClick = { messageActionState.value = MessageAction.Share }) {
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