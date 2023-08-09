package com.vn.talktoai.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vn.talktoai.R
import com.vn.talktoai.domain.models.Choice
import com.vn.talktoai.presentation.chat.AIMessage
import com.vn.talktoai.presentation.chat.UserMessage
import com.vn.talktoai.ui.theme.DarkGreen
import com.vn.talktoai.ui.theme.Green
import kotlinx.coroutines.launch

@Composable
fun MainScreen(chats: List<String>, content: @Composable (PaddingValues) -> Unit, onChatClicked: (String) -> Unit) {
    val scope = rememberCoroutineScope()

    val scaffoldState = rememberScaffoldState()

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                title = { Text(text = "Talk to AI") },
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
                        Icon(imageVector = Icons.Default.Menu, contentDescription = "Toolbar")
                    }
                }
            )
        },
        drawerContent = {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(top = 24.dp, start = 8.dp, end = 8.dp)
            ) {
                items(chats) { chat ->
                    ChatItem(text = chat, onChatClicked = onChatClicked)
                }
            }
        }, content = content
    )
}

@Composable
fun ChatItem(text: String, onChatClicked: (String) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp)
            .clickable {
                onChatClicked.invoke(text)
            }.border(1.dp, Green, shape = RoundedCornerShape(8.dp))
    ) {
        Icon(
            imageVector = Icons.Default.Info,
            contentDescription = "Send message button",
            tint = DarkGreen,
            modifier = Modifier
                .padding(vertical = 16.dp, horizontal = 8.dp)
        )

        Text(
            text = text,
            fontSize = 16.sp,
            modifier = Modifier
                .padding(vertical = 16.dp)
        )
    }
}