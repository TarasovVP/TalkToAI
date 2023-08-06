package com.vn.talktoai.presentation.chat

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.LifecycleOwner
import com.vn.talktoai.CommonExtensions.safeSingleObserve
import com.vn.talktoai.R
import com.vn.talktoai.domain.ApiRequest
import com.vn.talktoai.domain.models.Choice
import com.vn.talktoai.domain.models.Message

@Composable
fun ChatContent(
    viewModel: ChatViewModel,
    lifecycleOwner: LifecycleOwner
) {
    var items = mutableListOf<Choice>().toMutableStateList()
    viewModel.completionLiveData.safeSingleObserve(lifecycleOwner) { apiResponse ->
        val updatedItems = items
        updatedItems.addAll(apiResponse.choices.orEmpty())
        items = updatedItems
        Log.e("apiTAG", "ChatComposable ChatContent safeSingleObserve items $items")
    }
    val inputValue = remember { mutableStateOf(TextFieldValue()) }
    Log.e("apiTAG", "ChatComposable fun ChatContent items $items")
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top
    ) {

        MessageList(items)

        Spacer(modifier = Modifier
            .weight(1f))

        ChatInputField(inputValue = inputValue)  {
            val apiRequest = ApiRequest(model = "gpt-3.5-turbo", temperature = 0.7f, messages = listOf(Message(role = "user", content = inputValue.value.text)))
            viewModel.getCompletions(apiRequest)
            val updatedItems = items
            updatedItems.add(Choice(message = Message(role = "me", content = inputValue.value.text), "reason", 0))
            items = updatedItems
            inputValue.value = TextFieldValue("")
            Log.e("apiTAG", "ChatComposable ChatContent SendButton items $items")
        }

    }
}

@Composable
fun MessageList(messages: List<Choice>) {
    if (messages.isEmpty()) {
        IntroMessage()
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxWidth()
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
fun IntroMessage() {
    Text(
        text = "You haven`t hot message. Start conversation with AI.",
        fontSize = 16.sp,
        modifier = Modifier
            .wrapContentSize()
            .background(Color.Green)
    )
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
        SendButton(onSendClick)
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
fun SendButton(onSendClick: () -> Unit) {
    Button(
        onClick = onSendClick,
        modifier = Modifier
            .height(IntrinsicSize.Min)
            .fillMaxWidth(1f)
            .padding(top = 5.dp)
    ) {
        Text(text = "Send")
    }
}