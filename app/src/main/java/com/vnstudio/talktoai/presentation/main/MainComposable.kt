package com.vnstudio.talktoai.presentation.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vnstudio.talktoai.R
import com.vnstudio.talktoai.data.database.db_entities.Chat
import com.vnstudio.talktoai.presentation.base.ConfirmationDialog
import com.vnstudio.talktoai.presentation.base.DataEditDialog
import com.vnstudio.talktoai.ui.theme.*
import kotlinx.coroutines.launch

@Composable
fun MainScreen(chats: List<Chat>, onAddChatClicked: () -> Unit, onDeleteChatClicked: (Chat) -> Unit, onEditChatClicked: (Chat) -> Unit, onChatClicked: (Chat) -> Unit, onSettingsClicked: () -> Unit, content: @Composable (PaddingValues) -> Unit) {
    val scope = rememberCoroutineScope()
    val scaffoldState = rememberScaffoldState()
    var showEditDataDialog by remember { mutableStateOf(false) }
    var showConfirmationDialog by remember { mutableStateOf(false) }
    val inputValue = remember { mutableStateOf(TextFieldValue()) }

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                title = { Text(text = chats.firstOrNull()?.name.orEmpty()) },
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
                    IconButton(
                        onClick = {
                            showEditDataDialog = true
                        }
                    ) {
                        Icon(imageVector = ImageVector.vectorResource(id = R.drawable.ic_edit), contentDescription = "Edit title", tint = Primary100)
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
                AddChatItem(onAddChatClicked)
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    items(chats) { chat ->
                        ChatItem(chat = chat, onChatClicked = {
                            onChatClicked.invoke(it)
                            scope.launch {
                                scaffoldState.drawerState.close()
                            }
                        }, onDeleteChatClicked = {
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
    inputValue.value = TextFieldValue(chats.firstOrNull()?.name.orEmpty())
    DataEditDialog("Edit chat name", "Chat name", inputValue, showEditDataDialog, onDismiss = {
        showEditDataDialog = false
    }, onConfirmationClick = { newChatName ->
        onEditChatClicked.invoke(chats.first().apply {
            name = newChatName
        })
        showEditDataDialog = false
    })

    ConfirmationDialog("Delete chat?", showConfirmationDialog, onDismiss = {
        showConfirmationDialog = false
    }, onConfirmationClick = {
        //TODO improve implementation
        onDeleteChatClicked.invoke(chats[1])
        showConfirmationDialog = false
    })
}

@Composable
fun AddChatItem(onAddChatClicked: () -> Unit) {
    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(top = 40.dp, bottom = 32.dp, start = 32.dp, end = 32.dp)
        .clickable {
            onAddChatClicked.invoke()
        }) {
        Image(
            imageVector = ImageVector.vectorResource(id = R.drawable.ic_chat_add),
            contentDescription = "Add chat button",
            modifier = Modifier
                .padding(end = 8.dp)
        )
        Text(
            text = "New chat",
            fontSize = 16.sp,
            color = Neutral50
        )
    }
}

@Composable
fun ChatItem(chat: Chat, onChatClicked: (Chat) -> Unit, onDeleteChatClicked: (Chat) -> Unit) {
    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(bottom = 16.dp, start = 32.dp, end = 32.dp)
        .clickable {
            onChatClicked.invoke(chat)
        }) {
        Image(
            imageVector = ImageVector.vectorResource(id = R.drawable.ic_chat),
            contentDescription = "Chat item icon",
            modifier = Modifier
                .padding(end = 8.dp)
        )
        Text(
            text = chat.name,
            fontSize = 16.sp,
            color = Neutral50,
            modifier = Modifier.weight(1f)
        )
        Image(
            imageVector = ImageVector.vectorResource(id = R.drawable.ic_delete),
            contentDescription = "Delete chat button",
            modifier = Modifier
                .padding(end = 8.dp)
                .clickable {
                    onDeleteChatClicked.invoke(chat)
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