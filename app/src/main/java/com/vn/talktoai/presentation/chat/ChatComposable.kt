package com.vn.talktoai.presentation.chat

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vn.talktoai.domain.ApiResponse

@Composable
fun MessageList(apiResponse: ApiResponse) {
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        items(apiResponse.choices.orEmpty()) { choice ->
            CustomTextView(text = choice.message?.content.orEmpty())
        }
    }
}

@Composable
fun CustomTextView(text: String) {
    Text(
        text = text,
        fontSize = 20.sp,
        modifier = Modifier.padding(16.dp)
    )
}