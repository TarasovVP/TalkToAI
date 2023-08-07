package com.vn.talktoai.presentation.chat

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vn.talktoai.R
import com.vn.talktoai.domain.models.Choice
import com.vn.talktoai.presentation.ChatUiState

@Composable
fun ChatContent(
    chatUiState: ChatUiState,
    onSendClick: (MutableState<TextFieldValue>) -> Unit) {

    val inputValue = remember { mutableStateOf(TextFieldValue()) }

    Log.e("apiTAG", "ChatComposable fun ChatContent choices ${chatUiState.choices.toList()} isLoadingProgress ${chatUiState.isLoadingProgress}")

    Column(modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top
    ) {
        if (chatUiState.choices.isEmpty()) {
            IntroMessage(modifier = Modifier
                .fillMaxSize().weight(1f))
        } else {
            MessageList(chatUiState.choices, modifier = Modifier.weight(1f))
        }
        ChatInputField(inputValue = inputValue) {
            onSendClick.invoke(inputValue)
        }
    }
    if (chatUiState.isLoadingProgress) CircularProgressBar()
}

@Composable
fun MessageList(messages: List<Choice>,  modifier: Modifier = Modifier) {
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
    Box(modifier = Modifier
        .fillMaxWidth()
        .padding(bottom = 8.dp)
        .border(width = 1.dp, color = Color.LightGray)) {
        Row(
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        )  {
            Text(
                text = text,
                fontSize = 16.sp,
                modifier = Modifier
                    .padding(8.dp)
                    .wrapContentSize()
            )
            Image (
                painter = painterResource(id = R.drawable.user_avatar),
                contentDescription = "User avatar",
                modifier = Modifier
                    .padding(8.dp)
                    .width(24.dp)
                    .height(24.dp)
            )
        }
    }
}

@Composable
fun AIMessage(text: String) {
    Box(modifier = Modifier
        .fillMaxWidth()
        .padding(bottom = 8.dp)
        .border(width = 1.dp, color = Color.LightGray)) {
        Row(
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Image (
                painter = painterResource(id = R.drawable.ai_avatar),
                contentDescription = "AI avatar",
                modifier = Modifier
                    .padding(8.dp)
                    .width(24.dp)
                    .height(24.dp)
            )
            Text(
                text = text,
                fontSize = 16.sp,
                modifier = Modifier
                    .padding(bottom = 8.dp)
                    .fillMaxWidth()
            )
        }
    }
}

@Composable
fun ChatInputField(inputValue: MutableState<TextFieldValue>, onSendClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min),
        verticalAlignment = Alignment.CenterVertically
    ) {
        ChatOutlinedTextField(inputValue)
        SendButton(inputValue, onSendClick)
    }
}

@Composable
fun ChatOutlinedTextField(inputValue: MutableState<TextFieldValue>) {
    OutlinedTextField(value = inputValue.value, onValueChange = { newValue ->
        inputValue.value = newValue
    }, label = { Text(text = "Enter request") }, modifier = Modifier
        .height(IntrinsicSize.Min)
        .fillMaxWidth(0.8f))
}

@Composable
fun SendButton(inputValue: MutableState<TextFieldValue>, onSendClick: () -> Unit) {
    IconButton(
        onClick = onSendClick,
        modifier = Modifier
            .height(IntrinsicSize.Min)
            .fillMaxWidth(1f)
            .padding(top = 5.dp)
    ) {
        Icon(imageVector = if (inputValue.value.text.isEmpty()) Icons.Default.ThumbUp else Icons.Default.Send, contentDescription = "Send message button")
    }
}

@Composable
fun CircularProgressBar() {
    CircularProgressIndicator(
        modifier = Modifier
            .size(16.dp)
            .padding(16.dp),
        color = Color.Green
    )
}