package com.vn.talktoai.presentation.chat

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vn.talktoai.ui.theme.TalkToAITheme

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    TalkToAITheme {
        Greeting("Android")
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