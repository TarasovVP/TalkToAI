package com.vnstudio.talktoai.presentation.screens.chat

import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.google.accompanist.insets.navigationBarsWithImePadding
import com.vnstudio.talktoai.CommonExtensions.EMPTY
import com.vnstudio.talktoai.CommonExtensions.isNotNull
import com.vnstudio.talktoai.CommonExtensions.isTrue
import com.vnstudio.talktoai.R
import com.vnstudio.talktoai.clearCheckToAction
import com.vnstudio.talktoai.data.database.db_entities.Chat
import com.vnstudio.talktoai.data.network.models.ApiRequest
import com.vnstudio.talktoai.dateToMilliseconds
import com.vnstudio.talktoai.domain.enums.MessageStatus
import com.vnstudio.talktoai.domain.models.InfoMessage
import com.vnstudio.talktoai.domain.models.MessageApi
import com.vnstudio.talktoai.domain.sealed_classes.MessageAction
import com.vnstudio.talktoai.infrastructure.Constants.DEFAULT_CHAT_ID
import com.vnstudio.talktoai.isDefineSecondsLater
import com.vnstudio.talktoai.presentation.components.ConfirmationDialog
import com.vnstudio.talktoai.presentation.components.DataEditDialog
import com.vnstudio.talktoai.presentation.components.EmptyState
import com.vnstudio.talktoai.presentation.components.ExceptionMessageHandler
import com.vnstudio.talktoai.presentation.components.ProgressVisibilityHandler
import com.vnstudio.talktoai.presentation.components.TextFieldWithButton
import com.vnstudio.talktoai.presentation.components.TextIconButton
import com.vnstudio.talktoai.presentation.components.TruncatableText
import com.vnstudio.talktoai.presentation.components.draggable.UpdateViewConfiguration
import com.vnstudio.talktoai.presentation.components.getDimensionResource
import com.vnstudio.talktoai.presentation.components.painterRes
import com.vnstudio.talktoai.presentation.components.stringRes
import com.vnstudio.talktoai.presentation.components.textLinesCount
import com.vnstudio.talktoai.presentation.theme.Neutral50
import com.vnstudio.talktoai.presentation.theme.Primary500
import com.vnstudio.talktoai.presentation.theme.Primary600
import com.vnstudio.talktoai.presentation.theme.Primary900
import com.vnstudio.talktoai.presentation.ui_models.MessageUIModel
import com.vnstudio.talktoai.resources.LocalAvatarSize
import com.vnstudio.talktoai.resources.LocalDefaultTextSize
import com.vnstudio.talktoai.resources.LocalLargePadding
import com.vnstudio.talktoai.resources.LocalSmallPadding
import com.vnstudio.talktoai.textToAction
import kotlinx.datetime.Clock
import org.koin.androidx.compose.koinViewModel

@Composable
fun ChatContent(
    currentChatId: Long,
    isMessageActionModeState: MutableState<Boolean?>,
    infoMessageState: MutableState<InfoMessage?>,
    progressVisibilityState: MutableState<Boolean>,
) {
    val viewModel: ChatViewModel = koinViewModel()
    val showCreateChatDialog: MutableState<Boolean> = remember { mutableStateOf(false) }
    val currentChatState = viewModel.currentChatLiveData.collectAsState()
    val messagesState = viewModel.messagesLiveData.collectAsState()
    val messageActionState: MutableState<String> =
        rememberSaveable { mutableStateOf(MessageAction.Cancel().value) }
    val showMessageActionDialog: MutableState<Boolean> = rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.getCurrentChat(currentChatId)
    }

    LaunchedEffect(currentChatState.value) {
        currentChatState.value?.let { chat ->
            viewModel.getMessagesFromChat(chat.id)
        }
    }

    LaunchedEffect(isMessageActionModeState.value.isTrue() && messagesState.value?.none { it.isCheckedToDelete.value }
        .isTrue()) {
        isMessageActionModeState.value = false
    }

    val clipboardManager = LocalClipboardManager.current
    val shareIntentLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { _ ->
            infoMessageState.value = InfoMessage("Отправлено")
        }
    LaunchedEffect(messageActionState.value) {
        when (messageActionState.value) {
            MessageAction.Delete().value -> {
                showMessageActionDialog.value = true
            }

            MessageAction.Copy().value -> {
                clipboardManager.setText(AnnotatedString(messagesState.value.textToAction()))
                resetMessageActionState(
                    messagesState,
                    messageActionState,
                    isMessageActionModeState,
                    showMessageActionDialog
                )
                infoMessageState.value = InfoMessage("Скопировано")
            }

            MessageAction.Share().value -> {
                Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    putExtra(Intent.EXTRA_TEXT, messagesState.value.textToAction())

                    val chooser = Intent.createChooser(this, "Share Text")
                    shareIntentLauncher.launch(chooser)
                }
                resetMessageActionState(
                    messagesState,
                    messageActionState,
                    isMessageActionModeState,
                    showMessageActionDialog
                )
            }

            MessageAction.Transfer().value -> showMessageActionDialog.value = true
            else -> {
                resetMessageActionState(
                    messagesState,
                    messageActionState,
                    isMessageActionModeState,
                    showMessageActionDialog
                )
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
                    TextFieldWithButton(
                        (currentChatState.value?.id ?: DEFAULT_CHAT_ID) != DEFAULT_CHAT_ID
                    ) { messageText ->

                        viewModel.insertMessage(
                            MessageUIModel(
                                id = Clock.System.now().toEpochMilliseconds(),
                                chatId = currentChatState.value?.id ?: 0,
                                author = "me",
                                message = messageText,
                                updatedAt = Clock.System.now().dateToMilliseconds(),
                                status = MessageStatus.SUCCESS
                            )
                        )
                        val temporaryMessage = MessageUIModel(
                            id = Clock.System.now().toEpochMilliseconds() + 1,
                            chatId = currentChatState.value?.id ?: 0,
                            author = "gpt-3.5-turbo",
                            message = String.EMPTY,
                            updatedAt = Clock.System.now().dateToMilliseconds() + 1,
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
                id = Clock.System.now().dateToMilliseconds(),
                name = newChatName,
                updated = Clock.System.now().dateToMilliseconds()
            )
        )
        showCreateChatDialog.value = false
    }
    ConfirmationDialog(
        title = when (messageActionState.value) {
            MessageAction.Delete().value -> "Are you sure to delete?"
            MessageAction.Delete().value -> "Are you sure to transfer?"
            else -> String.EMPTY
        },
        showDialog = showMessageActionDialog,
        onDismiss = { showMessageActionDialog.value = false },
        onConfirmationClick = {
            when (messageActionState.value) {
                MessageAction.Delete().value -> {
                    viewModel.deleteMessages(messagesState.value?.filter { it.isCheckedToDelete.value }
                        ?.map { it.id } ?: listOf())
                    resetMessageActionState(
                        messagesState,
                        messageActionState,
                        isMessageActionModeState,
                        showMessageActionDialog
                    )
                    infoMessageState.value = InfoMessage("Удалено")
                }

                MessageAction.Transfer().value -> {
                    //TODO
                    resetMessageActionState(
                        messagesState,
                        messageActionState,
                        isMessageActionModeState,
                        showMessageActionDialog
                    )
                    infoMessageState.value = InfoMessage("Перенесено")
                }

                else -> {
                    resetMessageActionState(
                        messagesState,
                        messageActionState,
                        isMessageActionModeState,
                        showMessageActionDialog
                    )
                }
            }
        })

    ExceptionMessageHandler(infoMessageState, viewModel.exceptionLiveData)
    ProgressVisibilityHandler(progressVisibilityState, viewModel.progressVisibilityLiveData)
}

private fun resetMessageActionState(
    messagesState: State<List<MessageUIModel>?>,
    messageActionState: MutableState<String>,
    isMessageActionModeState: MutableState<Boolean?>,
    showMessageActionDialog: MutableState<Boolean>,
) {
    messagesState.value.clearCheckToAction()
    messageActionState.value = String.EMPTY
    isMessageActionModeState.value = false
    showMessageActionDialog.value = false
}

@Composable
fun MessagesList(
    messages: List<MessageUIModel>,
    isMessageActionModeState: MutableState<Boolean?>,
    modifier: Modifier = Modifier,
    onMessageChange: (MessageUIModel) -> Unit = {},
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
            longPressTimeoutMillis = 300L
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
    val isTruncatedState = rememberSaveable { mutableStateOf(message.isTruncated) }
    LaunchedEffect(isTruncatedState.value) {
        if (isTruncatedState.value != message.isTruncated) {
            onMessageChange(message.copy(isTruncated = isTruncatedState.value))
        }
    }

    val paddings =
        LocalLargePadding.current.margin.value + (if (isUserAuthor) LocalLargePadding.current.margin else LocalSmallPadding.current.margin).value + (LocalDefaultTextSize.current.textSize * 2).value + (LocalDefaultTextSize.current.textSize * 2).value + if (isUserAuthor) 0f else (LocalAvatarSize.current.margin * 2).value
    val linesCount = textLinesCount(
        message.message,
        paddings,
        getDimensionResource(resId = R.dimen.default_text_size).value
    )
    Row(
        verticalAlignment = Alignment.Top,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
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

        Column(modifier = Modifier
            .fillMaxHeight()
            .width(32.dp)
            .padding(top = 6.dp),
            verticalArrangement = Arrangement.Top) {
            if (isMessageDeleteModeState.value.isTrue()) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(if (message.isCheckedToDelete.value) R.drawable.ic_checked_check_box else R.drawable.ic_empty_check_box)
                        .crossfade(true)
                        .build(),
                    contentDescription = "Deleting message checkbox",
                    contentScale = ContentScale.Inside,
                    modifier = Modifier
                        .padding(2.dp)
                )
            } else if (isUserAuthor.not()) {
                //TODO uncomment
                /*AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(painterRes(resId = "avatar_ai"))
                        .crossfade(true)
                        .build(),
                    contentDescription = "AI avatar",
                    contentScale = ContentScale.Crop
                )*/
            }
        }
        Row(
            horizontalArrangement = if (isUserAuthor) Arrangement.End else Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Spacer(modifier = Modifier.width(4.dp))
            Box(
                modifier = Modifier
                    .wrapContentSize()
                    .widthIn(40.dp, (LocalConfiguration.current.screenWidthDp * 0.8).dp)
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

                    message.status == MessageStatus.REQUESTING && Clock.System.now().isDefineSecondsLater(
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
            "ic_chat_add",
            Modifier,
            onClick
        )
    }
}

@Composable
fun MessageActionField(
    messageActionState: MutableState<String>,
) {
    Row(
        Modifier
            .padding(16.dp)
            .height(TextFieldDefaults.MinHeight)
    ) {
        TextButton(onClick = { messageActionState.value = MessageAction.Cancel().value }) {
            Text(text = stringRes().BUTTON_CANCEL, color = Neutral50)
        }
        Spacer(modifier = Modifier.weight(1f))
        IconButton(onClick = { messageActionState.value = MessageAction.Copy().value }) {
            Image(
                painter = painterRes("ic_copy"),
                contentDescription = "Message copy button"
            )
        }
        IconButton(onClick = { messageActionState.value = MessageAction.Delete().value }) {
            Image(
                painter = painterRes("ic_delete"),
                contentDescription = "Message delete button"
            )
        }
        IconButton(onClick = { messageActionState.value = MessageAction.Share().value }) {
            Image(
                painter = painterRes("ic_share"),
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