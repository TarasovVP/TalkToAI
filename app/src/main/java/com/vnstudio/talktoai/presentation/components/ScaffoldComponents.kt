package com.vnstudio.talktoai.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vnstudio.talktoai.R
import com.vnstudio.talktoai.data.database.db_entities.Chat
import com.vnstudio.talktoai.domain.models.InfoMessage
import com.vnstudio.talktoai.presentation.screens.base.AddChatItem
import com.vnstudio.talktoai.presentation.screens.chat.ChatItem
import com.vnstudio.talktoai.presentation.screens.settings.settings_list.SettingsList
import com.vnstudio.talktoai.presentation.theme.Neutral50
import com.vnstudio.talktoai.presentation.theme.Primary100
import com.vnstudio.talktoai.presentation.theme.Primary700
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
    isChatDrawerMode: MutableState<Boolean>,
    currentRouteState: String?,
    chats: MutableState<List<Chat>>,
    onCreateChatClick: () -> Unit,
    onChatClick: (Chat) -> Unit,
    onDeleteChatClick: (Chat) -> Unit,
    infoMessageState: MutableState<InfoMessage?>,
    onNextScreen: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Primary900),
        Arrangement.Top
    ) {
        DrawerHeader(isChatDrawerMode)
        if (isChatDrawerMode.value) {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp, vertical = 24.dp)
            ) {
                items(chats.value) { chat ->
                    ChatItem(chat = chat, chats.value.indexOf(chat) == 0, onChatClick = {
                        onChatClick.invoke(chat)
                    }, onDeleteIconClick = onDeleteChatClick)
                }
            }
            AddChatItem(Modifier.padding(bottom = 40.dp, start = 16.dp, end = 16.dp), onCreateChatClick)
        } else {
            SettingsList(currentRouteState,
                Modifier
                    .weight(1f), infoMessageState, onNextScreen)
        }
    }

}

@Composable
fun DrawerHeader(isChatMode: MutableState<Boolean>) {
    Row(modifier = Modifier
        .fillMaxWidth()
        .background(color = Primary700), verticalAlignment = Alignment.Top) {
        Column(modifier = Modifier.weight(1f)) {
            Image(
                imageVector = ImageVector.vectorResource(id = if (isChatMode.value) R.drawable.avatar_ai else R.drawable.ic_settings),
                contentDescription = "Settings item icon",
                modifier = Modifier
                    .padding(start = 16.dp, top = 16.dp).size(60.dp)
            )
            Text(
                text = if (isChatMode.value) "Искуственный интеллект" else "Настройки",
                fontSize = 16.sp,
                color = Neutral50,
                modifier = Modifier
                    .padding(start = 16.dp, bottom = 16.dp, top = 8.dp)
            )
        }
        IconButton(onClick = {
            isChatMode.value = isChatMode.value.not()
        }) {
            Image(
                imageVector = ImageVector.vectorResource(id = if (isChatMode.value) R.drawable.ic_settings else R.drawable.ic_chat),
                contentDescription = "Settings item icon",
                modifier = Modifier
                    .padding(end = 16.dp, top = 16.dp).size(24.dp)
            )
        }
    }
}