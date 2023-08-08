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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vn.talktoai.R
import com.vn.talktoai.domain.models.Choice
import com.vn.talktoai.presentation.ChatUiState
import com.vn.talktoai.ui.theme.DarkGreen
import com.vn.talktoai.ui.theme.Green

@Composable
fun ChatContent(
    chatUiState: ChatUiState,
    onSendClick: (MutableState<TextFieldValue>) -> Unit,
) {

    val inputValue = remember { mutableStateOf(TextFieldValue()) }

    Log.e(
        "apiTAG",
        "ChatComposable fun ChatContent choices ${chatUiState.choices.toList()} isLoadingState.value ${chatUiState.isLoadingState.value} "
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
        ,
        verticalArrangement = Arrangement.Top
    ) {
        if (chatUiState.choices.isEmpty()) {
            IntroMessage(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .weight(1f)
            )
        } else {
            MessageList(chatUiState.choices, modifier = Modifier.weight(1f).padding(16.dp))
        }
        ChatOutlinedTextField(inputValue = inputValue, onSendClick)
    }
    if (chatUiState.isLoadingState.value) CircularProgressBar()
}

@Composable
fun MessageList(messages: List<Choice>, modifier: Modifier = Modifier) {
    Box(modifier = modifier) {
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            items(messages) { choice ->
                if (choice.message?.role == "me") {
                    UserMessage(text = choice.message.content.orEmpty())
                } else {
                    AIMessage(text = choice.message?.content.orEmpty())
                }
            }
        }
    }
}

@Composable
fun IntroMessage(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "You haven't got messages. Start a conversation with AI.",
            fontSize = 16.sp
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
                    color = Green,
                    shape = RoundedCornerShape(
                        topStart = 8.dp,
                        topEnd = 8.dp,
                        bottomStart = 8.dp,
                        bottomEnd = 0.dp
                    )
                )
        ) {
            Text(
                text = text,
                fontSize = 16.sp,
                color = Color.White,
                modifier = Modifier
                    .padding(8.dp)
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
            painter = painterResource(id = R.drawable.avatar_ai),
            contentDescription = "AI avatar"

        )
        Box(
            modifier = Modifier
                .wrapContentSize()
                .padding(start = 8.dp)
                .widthIn(0.dp, (LocalConfiguration.current.screenWidthDp * 0.8).dp)
                .background(
                    color = DarkGreen,
                    shape = RoundedCornerShape(
                        topStart = 8.dp,
                        topEnd = 8.dp,
                        bottomStart = 0.dp,
                        bottomEnd = 8.dp
                    )
                )
        ) {
            Text(
                text = text,
                fontSize = 16.sp,
                color = Color.White,
                modifier = Modifier
                    .padding(8.dp)
                    .wrapContentSize()

            )
        }
    }
}

@Composable
fun ChatOutlinedTextField(inputValue: MutableState<TextFieldValue>, onSendClick: (MutableState<TextFieldValue>) -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = DarkGreen)
            .height(IntrinsicSize.Min)
    ) {
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
            shape = RoundedCornerShape(22.dp),
            modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .border(1.dp, Green, shape = RoundedCornerShape(22.dp))
            .background(
                color = Color.White,
                shape = RoundedCornerShape(22.dp)
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
                    painter = painterResource(if (inputValue.value.text.isEmpty()) R.drawable.ic_voice_record else R.drawable.ic_message_send),
                    contentDescription = "Send message button",
                    tint = DarkGreen
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