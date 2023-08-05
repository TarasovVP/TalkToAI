package com.vn.talktoai.presentation.chat

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
    var items = remember { mutableStateOf(mutableListOf(Choice(message = Message(role = "Init", content = "Start"), "reason", 0))) }
    viewModel.completionLiveData.safeSingleObserve(lifecycleOwner) { apiResponse ->
        val updatedItems = items.value.toMutableList()
        updatedItems.addAll(apiResponse.choices.orEmpty())
        items.value = updatedItems
        Log.e("apiTAG", "ChatComposable ChatContent safeSingleObserve items.value ${items.value}")
    }
    val inputValue = remember { mutableStateOf(TextFieldValue()) }
    Log.e("apiTAG", "ChatComposable ChatContent items.value ${items.value}")
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top
    ) {

        LazyColumn(
            modifier = Modifier.fillMaxWidth()
        ) {
            items(items.value) { choice ->
                CustomTextView(text = "${choice.message?.role.orEmpty()}: ${choice.message?.content.orEmpty()}")
            }
        }

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
                val updatedItems = items.value.toMutableList()
                updatedItems.add(Choice(message = Message(role = "me", content = inputValue.value.text), "reason", 0))
                items.value = updatedItems
                inputValue.value = TextFieldValue("")
            }
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
            .wrapContentWidth()
    ) {
        Text(text = "Send")
    }
}