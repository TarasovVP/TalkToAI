package com.vnstudio.talktoai.presentation.main

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.vnstudio.talktoai.CommonExtensions.EMPTY
import com.vnstudio.talktoai.R
import com.vnstudio.talktoai.data.database.db_entities.Chat
import com.vnstudio.talktoai.presentation.base.AddChatItem
import com.vnstudio.talktoai.presentation.base.ConfirmationDialog
import com.vnstudio.talktoai.presentation.base.DataEditDialog
import com.vnstudio.talktoai.presentation.chat.ChatViewModel
import com.vnstudio.talktoai.ui.theme.*
import kotlinx.coroutines.launch
import java.util.*

@Composable
fun MainScreen(
    showCreateDataDialog: MutableState<Boolean>,
    onChatClicked: () -> Unit,
    onSettingsClicked: () -> Unit,
    content: @Composable (PaddingValues) -> Unit
) {
    val viewModel: ChatViewModel = hiltViewModel()
    val scope = rememberCoroutineScope()
    val scaffoldState = rememberScaffoldState()
    val chats = viewModel.chatsLiveData.observeAsState(listOf())
    val showEditDataDialog = mutableStateOf(false)
    var showConfirmationDialog by remember { mutableStateOf(false) }
    val inputValue = remember { mutableStateOf(TextFieldValue(String.EMPTY)) }
    val deletedChat = remember { mutableStateOf(Chat()) }

    LaunchedEffect(viewModel){
        viewModel.getChats()
    }

    Log.e(
        "apiTAG",
        "MainScreen chats ${chats.value}"
    )


    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                title = { Text(text = chats.value.firstOrNull()?.name ?: "Talk to AI") },
                backgroundColor = Primary900,
                contentColor = Neutral50,
                navigationIcon = {
                    IconButton(onClick = {
                        scope.launch {
                            if (scaffoldState.drawerState.isClosed) {
                                scaffoldState.drawerState.open()
                            } else {
                                scaffoldState.drawerState.close()
                            }
                        }
                    }) {
                        Icon(imageVector = ImageVector.vectorResource(id = R.drawable.ic_navigation), contentDescription = "Navigation icon", tint = Primary100)
                    }
                },
                actions = {
                    if (chats.value.isNotEmpty()) {
                        IconButton(
                            onClick = {
                                showEditDataDialog.value = true
                            }
                        ) {
                            Icon(imageVector = ImageVector.vectorResource(id = R.drawable.ic_edit), contentDescription = "Edit title", tint = Primary100)
                        }
                    }
                }
            )
        },

        drawerContent = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = Primary900)

            ) {
                AddChatItem(Modifier.padding(top = 40.dp, bottom = 24.dp, start = 16.dp, end = 16.dp)) {
                    showCreateDataDialog.value = true
                }
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    items(chats.value.orEmpty()) { chat ->
                        ChatItem(chat = chat, chats.value.indexOf(chat) == 0, onChatClick = {
                            onChatClicked.invoke()
                            viewModel.updateChats(chats.value.onEach { if (it.chatId == chat.chatId) it.updated = Date().time })
                            scope.launch {
                                scaffoldState.drawerState.close()
                            }
                        }, onDeleteIconClick = {
                            deletedChat.value = it
                            showConfirmationDialog = true
                        })
                    }
                }
                SettingsItem{
                    onSettingsClicked.invoke()
                    scope.launch {
                        scaffoldState.drawerState.close()
                    }
                }
            }
        }, content = content
    )
    inputValue.value = TextFieldValue( if (showCreateDataDialog.value) "Без названия" else chats.value.firstOrNull()?.name.orEmpty())

    DataEditDialog("Создать новый чат?", "Имя чата", inputValue, showCreateDataDialog, onDismiss = {
        showCreateDataDialog.value = false
    }) { newChatName ->
        viewModel.insertChat(Chat(name = newChatName, updated = Date().time))
        showCreateDataDialog.value = false
        scope.launch {
            scaffoldState.drawerState.close()
        }
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
        showConfirmationDialog = false
    }, onConfirmationClick = {
        viewModel.deleteChat(deletedChat.value)
        showConfirmationDialog = false
        deletedChat.value = Chat()
    })
}

@Composable
fun ChatItem(chat: Chat, isCurrent: Boolean, onChatClick: () -> Unit, onDeleteIconClick: (Chat) -> Unit) {
    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(bottom = 16.dp, start = 16.dp, end = 16.dp)
        .let { modifier ->
            if (isCurrent) {
            modifier.background(color = Primary800, shape = RoundedCornerShape(16.dp))
        } else {
            modifier.clickable {
                onChatClick.invoke()
            }
        }
    }) {
        Image(
            imageVector = ImageVector.vectorResource(id = R.drawable.ic_chat),
            contentDescription = "Chat item icon",
            modifier = Modifier
                .padding(8.dp)
        )
        Text(
            text = chat.name,
            fontSize = 16.sp,
            color = Neutral50,
            modifier = Modifier.weight(1f).padding(vertical = 8.dp)
        )
        Image(
            imageVector = ImageVector.vectorResource(id = R.drawable.ic_delete),
            contentDescription = "Delete chat button",
            modifier = Modifier
                .padding(8.dp)
                .clickable {
                    onDeleteIconClick.invoke(chat)
                }
        )
    }
}

@Composable
fun SettingsItem(onSettingsClicked: () -> Unit) {
    Row(modifier = Modifier
        .fillMaxWidth()
        .background(color = Primary700)
        .clickable {
            onSettingsClicked.invoke()
        }) {
        Image(
            imageVector = ImageVector.vectorResource(id = R.drawable.ic_settings),
            contentDescription = "Settings item icon",
            modifier = Modifier
                .padding(top = 16.dp, bottom = 40.dp, start = 32.dp, end = 8.dp)
        )
        Text(
            text = "Settings",
            fontSize = 16.sp,
            color = Neutral50,
            modifier = Modifier
                .weight(1f)
                .padding(top = 16.dp, bottom = 40.dp, end = 32.dp)
        )
    }
}