package com.vn.talktoai.presentation.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.vn.talktoai.CommonExtensions.EMPTY
import com.vn.talktoai.R
import com.vn.talktoai.data.database.db_entities.Chat
import com.vn.talktoai.presentation.base.PrimaryButton
import com.vn.talktoai.presentation.base.SecondaryButton
import com.vn.talktoai.ui.theme.*
import kotlinx.coroutines.launch

@Composable
fun MainScreen(chats: List<Chat>, onAddChatClicked: () -> Unit, onDeleteChatClicked: (Chat) -> Unit, onEditChatClicked: (Chat) -> Unit, onChatClicked: (Chat) -> Unit, onSettingsClicked: () -> Unit, content: @Composable (PaddingValues) -> Unit) {
    val scope = rememberCoroutineScope()
    var showDialog by remember { mutableStateOf(false) }
    val scaffoldState = rememberScaffoldState()

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
                            showDialog = true
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
                        ChatItem(chat = chat, onChatClicked = onChatClicked, onDeleteChatClicked = onDeleteChatClicked)
                    }
                }
                SettingsItem(onSettingsClicked)
            }
        }, content = content
    )

    EditChatNameDialog(chats.firstOrNull()?.name.orEmpty(), showDialog, onDismiss = {
        showDialog = false
    }, onChatNameChange = { newChatName ->
        onEditChatClicked.invoke(chats.first().apply {
            name = newChatName
        })
        showDialog = false
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

@Composable
fun EditChatNameDialog(chatName: String, showDialog: Boolean, onDismiss: () -> Unit, onChatNameChange: (String) -> Unit) {
    var inputText by remember { mutableStateOf(String.EMPTY) }
    inputText = chatName
    Column {
        if (showDialog) {
            Dialog(
                onDismissRequest = onDismiss,
                content = {
                    Column(modifier = Modifier
                        .wrapContentSize()
                        .border(1.dp, Primary500, shape = RoundedCornerShape(16.dp))
                        .background(color = Color.White, shape = RoundedCornerShape(16.dp)),
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(text = "Edit chat name", modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp), textAlign = TextAlign.Center,)
                        TextField(
                            value = inputText,
                            onValueChange = { inputText = it },
                            placeholder = { Text(text = "Chat name") },
                            colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.White),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp))
                        Row(modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            SecondaryButton(text = "Cancel", Modifier.weight(1f), onClick = onDismiss)
                            PrimaryButton(text = "OK", Modifier.weight(1f)) {
                                onChatNameChange.invoke(inputText)
                            }
                        }
                    }
                }
            )
        }
    }
}