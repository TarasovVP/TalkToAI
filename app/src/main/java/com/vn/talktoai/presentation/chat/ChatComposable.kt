package com.vn.talktoai.presentation.chat

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.LifecycleOwner
import com.vn.talktoai.CommonExtensions.safeSingleObserve
import com.vn.talktoai.domain.ApiRequest
import com.vn.talktoai.domain.models.Choice
import com.vn.talktoai.domain.models.Message

@Composable
fun ChatContent(viewModel: ChatViewModel, lifecycleOwner: LifecycleOwner) {
    var items = mutableListOf(Choice(message = Message(role = "Init", content = "Start"), "reason", 0)).toMutableStateList()
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

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ChatOutlinedTextField(inputValue)
            SendButton {
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
}

@Composable
fun MessageList(messages: List<Choice>) {
    LazyColumn(
        modifier = Modifier.fillMaxWidth()
    ) {
        items(messages) { choice ->
            CustomTextView(text = "${choice.message?.role.orEmpty()}: ${choice.message?.content.orEmpty()}")
        }
    }
}

@Composable
fun CustomTextView(text: String) {
    Text(
        text = text,
        fontSize = 16.sp,
        modifier = Modifier
            .padding(bottom = 8.dp)
            .fillMaxWidth()
            .background(Color.Red)
    )
}

@Composable
fun ChatOutlinedTextField(inputValue: MutableState<TextFieldValue>) {
    OutlinedTextField(value = inputValue.value, onValueChange = { newValue ->
        inputValue.value = newValue
    }, label = { Text(text = "Enter request") }, modifier = Modifier
        .wrapContentHeight()
        .wrapContentSize())
}

@Composable
fun SendButton(onSendClick: () -> Unit) {
    Button(
        onClick = onSendClick,
        modifier = Modifier
            .wrapContentHeight()
            .wrapContentWidth()
    ) {
        Text(text = "Send")
    }
}