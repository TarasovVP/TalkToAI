package com.vnteam.talktoai.presentation.screens.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vnteam.talktoai.Constants
import com.vnteam.talktoai.Res
import com.vnteam.talktoai.avatar_ai
import com.vnteam.talktoai.presentation.ui.NavigationScreen
import com.vnteam.talktoai.presentation.ui.resources.LocalStringResources
import com.vnteam.talktoai.presentation.ui.theme.Neutral50
import com.vnteam.talktoai.presentation.ui.theme.Primary700
import com.vnteam.talktoai.presentation.uimodels.screen.ScreenState
import org.jetbrains.compose.resources.painterResource
import com.vnteam.talktoai.presentation.screens.chat_list.ChatListScreen

@Composable
fun DrawerContent(screenState: ScreenState, onScreenStateUpdate: (ScreenState) -> Unit) {
    DrawerHeader()
    ChatListScreen(
        currentChatId = screenState.currentChat?.id,
        onChatClick = { chat ->
            onScreenStateUpdate(
                screenState.copy(
                    currentChat = chat,
                    currentScreenRoute = NavigationScreen.ChatScreen.route.replace(
                        "/{chatId}",
                        "/${chat?.id}"
                    )
                )
            )
        }
    )
}

@Composable
fun DrawerHeader() {
    Row(
        modifier = Modifier.fillMaxWidth().background(color = Primary700),
        verticalAlignment = Alignment.Top
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Image(
                painter = painterResource(Res.drawable.avatar_ai),
                contentDescription = LocalStringResources.current.APP_NAME,
                modifier = Modifier.padding(start = 16.dp, top = 16.dp).size(60.dp)
            )
            Text(
                text = LocalStringResources.current.APP_NAME,
                fontSize = 16.sp,
                color = Neutral50,
                modifier = Modifier.padding(start = 16.dp, bottom = 16.dp, top = 8.dp)
            )
        }
    }
}