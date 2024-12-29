package presentation.screens.chat_list

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.vnteam.talktoai.CommonExtensions.isNull
import com.vnteam.talktoai.CommonExtensions.isTrue
import com.vnteam.talktoai.Constants.DEFAULT_CHAT_ID
import com.vnteam.talktoai.Res
import com.vnteam.talktoai.domain.enums.AuthState
import com.vnteam.talktoai.domain.models.Chat
import com.vnteam.talktoai.empty_state
import com.vnteam.talktoai.ic_chat
import com.vnteam.talktoai.ic_chat_add
import com.vnteam.talktoai.ic_delete
import com.vnteam.talktoai.ic_drag_handle
import com.vnteam.talktoai.presentation.ui.components.ConfirmationDialog
import com.vnteam.talktoai.presentation.ui.components.CreateChatDialog
import com.vnteam.talktoai.presentation.ui.components.TextIconButton
import com.vnteam.talktoai.presentation.ui.components.draggable.DragDropColumn
import com.vnteam.talktoai.presentation.ui.resources.LocalStringResources
import com.vnteam.talktoai.presentation.ui.theme.Neutral50
import com.vnteam.talktoai.presentation.ui.theme.Primary800
import com.vnteam.talktoai.presentation.ui.theme.Primary900
import com.vnteam.talktoai.presentation.viewmodels.ChatListViewModel
import dateToMilliseconds
import kotlinx.datetime.Clock
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ChatListComposable(
    onChatClick: (Long) -> Unit
) {
    val viewModel = koinViewModel<ChatListViewModel>()
    val authState = viewModel.authState.collectAsState()
    LaunchedEffect(Unit) {
        viewModel.addAuthStateListener()
    }
    LaunchedEffect(authState.value) {
        authState.value?.let { authStateValue ->
            when (authStateValue) {
                AuthState.UNAUTHORISED -> viewModel.removeRemoteUserListeners()
                AuthState.AUTHORISED_ANONYMOUSLY -> viewModel.getChats()
                else -> {
                    viewModel.getChats()
                    viewModel.addRemoteChatListener()
                    viewModel.addRemoteMessageListener()
                }
            }
        }
    }
    val chatsState = viewModel.chatsList.collectAsState()
    val showCreateChatDialog = remember { mutableStateOf(false) }
    val showDeleteChatDialog = remember { mutableStateOf(false) }

    val currentChatState = remember { mutableStateOf<Chat?>(null) }
    val deleteChatState = remember { mutableStateOf<Chat?>(null) }

    LaunchedEffect(chatsState.value) {
        chatsState.value?.let { chats ->
            when {
                currentChatState.value.isNull() -> currentChatState.value = chats.firstOrNull()
                chats.contains(currentChatState.value).not() -> {
                    currentChatState.value = chats.firstOrNull()
                    /*chatsState.value?.currentScreenState?.value =
                        "$DESTINATION_CHAT_SCREEN/${currentChatState.value?.id ?: DEFAULT_CHAT_ID}"*/
                }
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().background(Primary900), Arrangement.Top
    ) {
        if (chatsState.value.isNullOrEmpty()) {
            Image(
                painter = painterResource(Res.drawable.empty_state),
                contentDescription = LocalStringResources.current.CHAT_EMPTY_STATE,
                modifier = Modifier.fillMaxWidth().weight(1f).padding(top = 16.dp)
            )
        } else {
            DragDropColumn(
                modifier = Modifier.weight(1f).padding(horizontal = 16.dp, vertical = 24.dp),
                items = chatsState.value.orEmpty(),
                onSwap = { firstIndex, secondIndex ->
                    viewModel.swapChats(firstIndex, secondIndex)
                },
                onDragEnd = {
                    viewModel.updateChats(chatsState.value.orEmpty())
                }
            ) { chat, isDragging ->
                val elevation = animateDpAsState(if (isDragging) 4.dp else 1.dp)
                ChatItem(name = chat.name,
                    mainIcon = Res.drawable.ic_chat,
                    isCurrent = chat.id == currentChatState.value?.id,
                    secondaryIcon = if (isDragging) Res.drawable.ic_drag_handle else Res.drawable.ic_delete,
                    elevation = elevation.value,
                    isIconClick = true,
                    onIconClick = {
                        if (isDragging.not()) {
                            deleteChatState.value = chat
                            showDeleteChatDialog.value = true
                        }
                    },
                    onItemClick = { if (isDragging.not()) onChatClick.invoke(chat.id) })
            }
        }
        TextIconButton(
            LocalStringResources.current.NEW_CHAT,
            Res.drawable.ic_chat_add,
            Modifier.padding(bottom = 40.dp, start = 16.dp, end = 16.dp)
        ) {
            showCreateChatDialog.value = true
        }
    }
    CreateChatDialog(
        currentChatState.value?.name.orEmpty(),
        showCreateChatDialog
    ) {
        if (currentChatState.value?.name.isNullOrEmpty()) {
            viewModel.insertChat(
                Chat(
                    id = Clock.System.now().dateToMilliseconds(),
                    name = it,
                    updated = Clock.System.now().dateToMilliseconds(),
                    listOrder = (chatsState.value.orEmpty().size + 1).toLong()
                )
            )
            currentChatState.value = null
        } else {
            currentChatState.value?.apply {
                name = it
            }?.let { viewModel.updateChat(it) }
        }
        onChatClick.invoke(currentChatState.value?.id ?: DEFAULT_CHAT_ID)
    }

    ConfirmationDialog(
        LocalStringResources.current.CHAT_DELETE_TITLE,
        showDeleteChatDialog,
        onDismiss = {
            showDeleteChatDialog.value = false
        }) {
        deleteChatState.value?.let { viewModel.deleteChat(it) }
        showDeleteChatDialog.value = false
        deleteChatState.value = null
    }
}

@Composable
fun ChatItem(
    name: String,
    mainIcon: DrawableResource,
    isCurrent: Boolean,
    secondaryIcon: DrawableResource? = null,
    elevation: Dp? = null,
    isIconClick: Boolean? = false,
    onIconClick: () -> Unit = {},
    onItemClick: () -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp, bottom = 8.dp, start = 16.dp, end = 16.dp)
            .let { modifier ->
            if (isCurrent.not()) {
                modifier.clickable {
                    onItemClick.invoke()
                }
            } else {
                modifier
            }
        }, colors = CardDefaults.cardColors(
            containerColor = if (isCurrent) Primary800 else Primary900
        ), elevation = CardDefaults.cardElevation(
            defaultElevation = elevation ?: 1.dp
        )
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Image(
                painter = painterResource(mainIcon),
                contentDescription = name,
                modifier = Modifier.padding(8.dp)
            )
            Text(
                text = name,
                color = Neutral50,
                modifier = Modifier.weight(1f).padding(vertical = 8.dp)
            )
            secondaryIcon?.let { icon ->
                Image(painter = painterResource(icon),
                    contentDescription = name,
                    modifier = Modifier.padding(8.dp).let {
                        if (isIconClick.isTrue()) {
                            it.clickable {
                                onIconClick.invoke()
                            }
                        } else {
                            it
                        }
                    })
            }
        }
    }
}