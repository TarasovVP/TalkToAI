package com.vn.talktoai.presentation.chat

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vn.talktoai.R
import com.vn.talktoai.data.database.db_entities.Message
import com.vn.talktoai.presentation.uistates.ChatUiState
import com.vn.talktoai.ui.theme.*

@Composable
fun ChatContent(
    chatUiState: ChatUiState,
    onSendClick: (MutableState<TextFieldValue>) -> Unit,
) {

    val inputValue = remember { mutableStateOf(TextFieldValue()) }

    Log.e(
        "apiTAG",
        "ChatComposable fun ChatContent messages ${chatUiState.messages.toList()} isLoadingState.value ${chatUiState.isLoadingState.value} "
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
        ,
        verticalArrangement = Arrangement.Top
    ) {
        if (chatUiState.messages.isEmpty()) {
            IntroMessage(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(45.dp)
                    .weight(1f)
            )
        } else {
            MessageList(chatUiState.messages, modifier = Modifier.weight(1f).padding(16.dp))
        }
        ChatTextField(inputValue = inputValue, onSendClick)
    }
    if (chatUiState.isLoadingState.value) CircularProgressBar()
}

@Composable
fun MessageList(messages: List<Message>, modifier: Modifier = Modifier) {
    Box(modifier = modifier) {
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            items(messages) { message ->
                if (message.author == "me") {
                    UserMessage(text = message.message)
                } else {
                    AIMessage(text = message.message)
                }
            }
        }
    }
}

@Composable
fun IntroMessage(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = "You haven't got messages. Start a conversation with AI.",
            fontSize = 16.sp,
            modifier = Modifier.wrapContentSize()
                .border(1.dp, Primary300, shape = RoundedCornerShape(18.dp))
                .padding(16.dp)
        )
    }
}

@Composable
fun UserMessage(text: String) {
    Row(
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp)
    ) {
        Box(
            modifier = Modifier
                .wrapContentSize()
                .widthIn(0.dp, (LocalConfiguration.current.screenWidthDp * 0.8).dp)
                .background(
                    color = Primary500,
                    shape = RoundedCornerShape(
                        topStart = 16.dp,
                        topEnd = 16.dp,
                        bottomStart = 16.dp,
                        bottomEnd = 2.dp
                    )
                )
        ) {
            Text(
                text = text,
                fontSize = 16.sp,
                color = Color.White,
                modifier = Modifier
                    .padding(10.dp)
                    .wrapContentSize()

            )
        }
    }
}

@Composable
fun AIMessage(text: String) {
    Row(
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.Bottom,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp)
    ) {
        Image(
            imageVector = ImageVector.vectorResource(id = R.drawable.avatar_ai),
            contentDescription = "AI avatar"

        )
        Box(
            modifier = Modifier
                .wrapContentSize()
                .padding(start = 8.dp)
                .widthIn(0.dp, (LocalConfiguration.current.screenWidthDp * 0.8).dp)
                .background(
                    color = Primary600,
                    shape = RoundedCornerShape(
                        topStart = 16.dp,
                        topEnd = 16.dp,
                        bottomStart = 2.dp,
                        bottomEnd = 16.dp
                    )
                )
        ) {
            Text(
                text = text,
                fontSize = 16.sp,
                color = Color.White,
                modifier = Modifier
                    .padding(10.dp)
                    .wrapContentSize()

            )
        }
    }
}

@Composable
fun ChatTextField(inputValue: MutableState<TextFieldValue>, onSendClick: (MutableState<TextFieldValue>) -> Unit) {
    Box(modifier = Modifier
            .fillMaxWidth()
            .background(color = Primary900)
            .height(IntrinsicSize.Min)) {
        TextField(value = inputValue.value,
            onValueChange = { newValue ->
            inputValue.value = newValue
        }, placeholder = { Text(text = "Enter request") },
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = {
                onSendClick.invoke(inputValue)
            }),
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color.White,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            ),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .border(1.dp, Primary500, shape = RoundedCornerShape(16.dp))
            .background(
                color = Color.White,
                shape = RoundedCornerShape(16.dp)
            ),
            maxLines = 6,
            trailingIcon = {
            Box(
                modifier = Modifier
                    .clickable {
                        onSendClick.invoke(inputValue)
                    }
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(if (inputValue.value.text.isEmpty()) R.drawable.ic_voice_record else R.drawable.ic_message_send),
                    contentDescription = "Send message button",
                    tint = Primary900
                )
            }
        }
        )
    }
}

@Composable
fun CircularProgressBar() {
    Box(contentAlignment = Alignment.Center) {
        CircularProgressIndicator(
            modifier = Modifier
                .fillMaxSize(0.25f),
            color = Color.Green
        )
    }
}