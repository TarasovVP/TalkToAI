package presentation.screens.chat

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
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import clearCheckToAction
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.vnteam.talktoai.CommonExtensions.EMPTY
import com.vnteam.talktoai.CommonExtensions.isNotNull
import com.vnteam.talktoai.CommonExtensions.isTrue
import com.vnteam.talktoai.Constants
import com.vnteam.talktoai.Constants.DEFAULT_CHAT_ID
import com.vnteam.talktoai.Res
import com.vnteam.talktoai.avatar_ai
import com.vnteam.talktoai.data.network.request.ApiRequest
import com.vnteam.talktoai.data.network.request.MessageApi
import com.vnteam.talktoai.domain.enums.MessageStatus
import com.vnteam.talktoai.domain.models.InfoMessage
import com.vnteam.talktoai.domain.sealed_classes.MessageAction
import com.vnteam.talktoai.ic_chat_add
import com.vnteam.talktoai.ic_checked_check_box
import com.vnteam.talktoai.ic_copy
import com.vnteam.talktoai.ic_delete
import com.vnteam.talktoai.ic_empty_check_box
import com.vnteam.talktoai.ic_share
import com.vnteam.talktoai.presentation.ui.components.ConfirmationDialog
import com.vnteam.talktoai.presentation.ui.components.EmptyState
import com.vnteam.talktoai.presentation.ui.components.ExceptionMessageHandler
import com.vnteam.talktoai.presentation.ui.components.ProgressVisibilityHandler
import com.vnteam.talktoai.presentation.ui.components.TextFieldWithButton
import com.vnteam.talktoai.presentation.ui.components.TextIconButton
import com.vnteam.talktoai.presentation.ui.components.TruncatableText
import com.vnteam.talktoai.presentation.ui.components.draggable.UpdateViewConfiguration
import com.vnteam.talktoai.presentation.ui.components.textLinesCount
import com.vnteam.talktoai.presentation.ui.resources.LocalDefaultTextSize
import com.vnteam.talktoai.presentation.ui.resources.LocalLargePadding
import com.vnteam.talktoai.presentation.ui.resources.LocalMediumAvatarSize
import com.vnteam.talktoai.presentation.ui.resources.LocalSmallPadding
import com.vnteam.talktoai.presentation.ui.resources.LocalStringResources
import com.vnteam.talktoai.presentation.ui.theme.Neutral50
import com.vnteam.talktoai.presentation.ui.theme.Primary500
import com.vnteam.talktoai.presentation.ui.theme.Primary600
import com.vnteam.talktoai.presentation.ui.theme.Primary900
import com.vnteam.talktoai.presentation.uimodels.MessageUI
import com.vnteam.talktoai.presentation.uimodels.screen.ScreenState
import com.vnteam.talktoai.presentation.viewmodels.ChatViewModel
import com.vnteam.talktoai.utils.screenWidth
import dateToMilliseconds
import isDefineSecondsLater
import kotlinx.datetime.Clock
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel
import textToAction

@Composable
fun ChatContent(
    chatId: Long,
    showCreateChatDialog: MutableState<Boolean>,
    isMessageActionModeState: MutableState<Boolean?> = mutableStateOf(false),
    screenState: ScreenState
) {
    val viewModel = koinViewModel<ChatViewModel>()
    val currentChatState = viewModel.currentChatLiveData.collectAsState()
    val messagesState = viewModel.messagesLiveData.collectAsState()
    val messageActionState: MutableState<String> =
        rememberSaveable { mutableStateOf(MessageAction.Cancel().value) }
    val showMessageActionDialog: MutableState<Boolean> = rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.getAnimationResource()
    }

    LaunchedEffect(screenState.currentScreenState.value) {
        viewModel.getCurrentChat(chatId)
    }

    LaunchedEffect(currentChatState.value) {
        currentChatState.value?.let { chat ->
            viewModel.getMessagesFromChat(chat.id)
        }
    }

    LaunchedEffect(isMessageActionModeState.value.isTrue() && messagesState.value.none { it.isCheckedToDelete.value }) {
        isMessageActionModeState.value = false
    }

    val clipboardManager = LocalClipboardManager.current
    val messageSent = LocalStringResources.current.MESSAGE_ACTION_SEND
    val messageCopy = LocalStringResources.current.MESSAGE_ACTION_COPY
    val messageShare = LocalStringResources.current.MESSAGE_ACTION_SHARE
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
                screenState.infoMessageState.value = InfoMessage(messageCopy)
            }

            MessageAction.Share().value -> {
                viewModel.shareLink(messagesState.value.textToAction())
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
        modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Top
    ) {
        Box(modifier = Modifier.weight(1f)) {
            messagesState.value.takeIf { it.isNotNull() }?.let { messages ->
                MessagesList(
                    messages,
                    isMessageActionModeState,
                    modifier = Modifier.padding(horizontal = 16.dp)
                ) { message ->
                    viewModel.updateMessage(message)
                }
            }
        }
        Box(
            modifier = Modifier.fillMaxWidth().background(Primary900)
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
                            MessageUI(
                                id = Clock.System.now().toEpochMilliseconds(),
                                chatId = currentChatState.value?.id ?: 0,
                                author = Constants.MESSAGE_ROLE_ME,
                                message = messageText,
                                updatedAt = Clock.System.now().dateToMilliseconds(),
                                status = MessageStatus.SUCCESS
                            )
                        )
                        val temporaryMessage = MessageUI(
                            id = Clock.System.now().toEpochMilliseconds() + 1,
                            chatId = currentChatState.value?.id ?: 0,
                            author = Constants.MESSAGE_ROLE_CHAT_GPT,
                            message = String.EMPTY,
                            updatedAt = Clock.System.now().dateToMilliseconds() + 1,
                            status = MessageStatus.REQUESTING
                        )
                        viewModel.insertMessage(temporaryMessage)
                        viewModel.sendRequest(
                            temporaryMessage, ApiRequest(
                                model = Constants.MESSAGE_ROLE_CHAT_GPT,
                                temperature = 0.7f,
                                messages = listOf(
                                    MessageApi(
                                        role = Constants.MESSAGE_ROLE_USER,
                                        content = messageText
                                    )
                                )
                            )
                        )
                    }
                }
            }
        }
    }

    val messageDelete = LocalStringResources.current.MESSAGE_ACTION_DELETE
    val messageTransfer = LocalStringResources.current.MESSAGE_ACTION_TRANSFER
    ConfirmationDialog(title = when (messageActionState.value) {
        MessageAction.Delete().value -> LocalStringResources.current.MESSAGE_DELETE_CONFIRMATION
        MessageAction.Transfer().value -> LocalStringResources.current.MESSAGE_TRANSFER_CONFIRMATION
        else -> String.EMPTY
    },
        showDialog = showMessageActionDialog,
        onDismiss = { showMessageActionDialog.value = false }) {
        when (messageActionState.value) {
            MessageAction.Delete().value -> {
                viewModel.deleteMessages(messagesState.value.filter { it.isCheckedToDelete.value }
                    .map { it.id })
                resetMessageActionState(
                    messagesState,
                    messageActionState,
                    isMessageActionModeState,
                    showMessageActionDialog
                )
                screenState.infoMessageState.value = InfoMessage(messageDelete)
            }

            MessageAction.Transfer().value -> {
                //TODO
                resetMessageActionState(
                    messagesState,
                    messageActionState,
                    isMessageActionModeState,
                    showMessageActionDialog
                )
                screenState.infoMessageState.value = InfoMessage(messageTransfer)
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
    }

    ExceptionMessageHandler(screenState.infoMessageState, viewModel.exceptionLiveData)
    ProgressVisibilityHandler(
        mutableStateOf(screenState.isProgressVisible), viewModel.progressVisibilityLiveData
    )
}

private fun resetMessageActionState(
    messagesState: State<List<MessageUI>?>,
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
    messages: List<MessageUI>,
    isMessageActionModeState: MutableState<Boolean?>,
    modifier: Modifier = Modifier,
    onMessageChange: (MessageUI) -> Unit = {},
) {
    if (messages.isEmpty()) {
        EmptyState(
            text = LocalStringResources.current.MESSAGE_EMPTY_STATE,
            modifier = Modifier.fillMaxSize().padding(45.dp)
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
                    modifier = Modifier.fillMaxSize(), state = scrollState
                ) {
                    items(messages) { message ->
                        Message(
                            isUserAuthor = message.author == Constants.MESSAGE_ROLE_ME,
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
    message: MessageUI,
    isMessageDeleteModeState: MutableState<Boolean?>,
    onMessageChange: (MessageUI) -> Unit = {},
) {
    val isTruncatedState = rememberSaveable { mutableStateOf(message.isTruncated) }
    LaunchedEffect(isTruncatedState.value) {
        if (isTruncatedState.value != message.isTruncated) {
            onMessageChange(message.copy(isTruncated = isTruncatedState.value))
        }
    }

    val paddings =
        LocalLargePadding.current.size.value + (if (isUserAuthor) LocalLargePadding.current.size else LocalSmallPadding.current.size).value + (LocalDefaultTextSize.current.textSize * 2).value + (LocalDefaultTextSize.current.textSize * 2).value + if (isUserAuthor) 0f else (LocalMediumAvatarSize.current.size * 2).value
    val linesCount = textLinesCount(
        message.message, paddings, LocalDefaultTextSize.current.textSize.value
    )
    Row(verticalAlignment = Alignment.Top,
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
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
            }) {

        Column(
            modifier = Modifier.fillMaxHeight().width(32.dp).padding(top = 6.dp),
            verticalArrangement = Arrangement.Top
        ) {
            if (isMessageDeleteModeState.value.isTrue()) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalPlatformContext.current)
                        .data(if (message.isCheckedToDelete.value) Res.drawable.ic_checked_check_box else Res.drawable.ic_empty_check_box)
                        .crossfade(true)
                        .build(),
                    contentDescription = LocalStringResources.current.MESSAGE_DELETE_CONFIRMATION,
                    contentScale = ContentScale.Inside,
                    modifier = Modifier
                        .padding(2.dp)
                )
            } else if (isUserAuthor.not()) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalPlatformContext.current)
                        .data(Res.drawable.avatar_ai)
                        .crossfade(true)
                        .build(),
                    contentDescription = LocalStringResources.current.AI_AVATAR,
                    contentScale = ContentScale.Crop
                )
            }
        }
        Row(
            horizontalArrangement = if (isUserAuthor) Arrangement.End else Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Spacer(modifier = Modifier.width(4.dp))
            Box(
                modifier = Modifier.wrapContentSize().fillMaxWidth(0.8f)
                    .widthIn(40.dp, (screenWidth().value * 0.8).dp)
                    .background(
                        color = if (isUserAuthor) Primary500 else Primary600,
                        shape = RoundedCornerShape(
                            topStart = 16.dp,
                            topEnd = LocalLargePadding.current.size,
                            bottomStart = if (isUserAuthor) LocalLargePadding.current.size else LocalSmallPadding.current.size,
                            bottomEnd = if (isUserAuthor) 2.dp else 16.dp
                        )
                    )
            ) {
                when {
                    message.status == MessageStatus.ERROR -> Text(
                        text = message.errorMessage,
                        fontSize = 16.sp,
                        color = Color.Red,
                        modifier = Modifier.padding(8.dp).wrapContentSize()
                    )

                    message.status == MessageStatus.REQUESTING && Clock.System.now()
                        .isDefineSecondsLater(
                            20, message.updatedAt
                        ) -> Text(
                        text = LocalStringResources.current.UNKNOWN_ERROR,
                        fontSize = 16.sp,
                        color = Color.Red,
                        modifier = Modifier.padding(8.dp).wrapContentSize()
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
fun MessageTypingAnimation() {
    val viewModel = koinViewModel<ChatViewModel>()
    viewModel.animationResource.collectAsState().value.let {
        viewModel.animationUtils.MessageTypingAnimation(
            it
        )
    }
}

@Composable
fun CreateChatScreen(onClick: () -> Unit) {
    Row(
        Modifier.padding(16.dp).height(TextFieldDefaults.MinHeight)
    ) {
        TextIconButton(
            LocalStringResources.current.NEW_CHAT, Res.drawable.ic_chat_add, Modifier, onClick
        )
    }
}

@Composable
fun MessageActionField(
    messageActionState: MutableState<String>,
) {
    Row(
        Modifier.padding(16.dp).height(TextFieldDefaults.MinHeight)
    ) {
        TextButton(onClick = { messageActionState.value = MessageAction.Cancel().value }) {
            Text(text = LocalStringResources.current.BUTTON_CANCEL, color = Neutral50)
        }
        Spacer(modifier = Modifier.weight(1f))
        IconButton(onClick = { messageActionState.value = MessageAction.Copy().value }) {
            Image(
                painter = painterResource(Res.drawable.ic_copy),
                contentDescription = LocalStringResources.current.MESSAGE_COPY_BUTTON
            )
        }
        IconButton(onClick = { messageActionState.value = MessageAction.Delete().value }) {
            Image(
                painter = painterResource(Res.drawable.ic_delete),
                contentDescription = LocalStringResources.current.MESSAGE_DELETE_BUTTON
            )
        }
        IconButton(onClick = { messageActionState.value = MessageAction.Share().value }) {
            Image(
                painter = painterResource(Res.drawable.ic_share),
                contentDescription = LocalStringResources.current.MESSAGE_SHARE_BUTTON
            )
        }
    }
}