package com.vnstudio.talktoai.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.vnstudio.talktoai.R
import com.vnstudio.talktoai.data.database.db_entities.Chat
import com.vnstudio.talktoai.presentation.screens.base.AddChatItem
import com.vnstudio.talktoai.presentation.screens.chat.ChatItem
import com.vnstudio.talktoai.presentation.screens.chat.SettingsItem
import com.vnstudio.talktoai.presentation.theme.Neutral50
import com.vnstudio.talktoai.presentation.theme.Primary100
import com.vnstudio.talktoai.presentation.theme.Primary900


@Composable
fun PrimaryTopBar(title: String, onNavigationIconClick: () -> Unit, isActionVisible: Boolean, onActionIconClick: () -> Unit) {
    TopAppBar(
        title = { Text(title) },
        backgroundColor = Primary900,
        contentColor = Neutral50,
        navigationIcon = {
            IconButton(onClick = onNavigationIconClick) {
                Icon(imageVector = ImageVector.vectorResource(id = R.drawable.ic_navigation), contentDescription = "Navigation icon", tint = Primary100)
            }
        },
        actions = {
            if (isActionVisible) {
                IconButton(
                    onClick = onActionIconClick) {
                    Icon(imageVector = ImageVector.vectorResource(id = R.drawable.ic_edit), contentDescription = "Edit title", tint = Primary100)
                }
            }
        }
    )
}

@Composable
fun SecondaryTopBar(title: String, onNavigationIconClick: () -> Unit) {
    TopAppBar(
        title = { Text(title) },
        backgroundColor = Primary900,
        contentColor = Neutral50,
        navigationIcon = {
            IconButton(onClick = onNavigationIconClick) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.ic_arrow_back),
                    contentDescription = "Navigation icon"
                )
            }
        })
}

@Composable
fun AppDrawer(
    isChatScreen: Boolean,
    chats: MutableState<List<Chat>>,
    onCreateChatClick: () -> Unit,
    onChatClick: (Chat) -> Unit,
    onDeleteChatClick: (Chat) -> Unit,
    onSettingsClick: () -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Primary900)
    ) {
        AddChatItem(Modifier.padding(top = 40.dp, bottom = 24.dp, start = 16.dp, end = 16.dp), onCreateChatClick)
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            items(chats.value) { chat ->
                ChatItem(chat = chat, chats.value.indexOf(chat) == 0 && isChatScreen, onChatClick = {
                    onChatClick.invoke(chat)
                }, onDeleteIconClick = onDeleteChatClick)
            }
        }
        SettingsItem(onSettingsClick)
    }
}