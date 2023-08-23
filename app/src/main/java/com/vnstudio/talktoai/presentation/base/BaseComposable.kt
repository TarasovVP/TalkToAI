package com.vnstudio.talktoai.presentation.base

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vnstudio.talktoai.R
import com.vnstudio.talktoai.ui.theme.*


@Composable
fun AddChatItem(modifier: Modifier, onAddChatClicked: () -> Unit) {
    Row(modifier = modifier
        .fillMaxWidth()
        .background(color = Primary700, shape = RoundedCornerShape(16.dp))
        .clickable {
            onAddChatClicked.invoke()
        }) {
        Image(
            imageVector = ImageVector.vectorResource(id = R.drawable.ic_chat_add),
            contentDescription = "Add chat button",
            modifier = Modifier
                .padding(8.dp)
        )
        Text(
            text = "New chat",
            fontSize = 16.sp,
            color = Neutral50,
            modifier = Modifier.weight(1f).padding(vertical = 8.dp)
        )
    }
}

@Composable
fun OrDivider(modifier: Modifier) {
    Row(modifier = modifier
        .fillMaxWidth()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .weight(1f)
                .background(Primary300)
                .align(Alignment.CenterVertically)
        )
        Text(
            text = "Или",
            fontSize = 16.sp,
            modifier = Modifier.weight(1f).padding(vertical = 8.dp).wrapContentSize()
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .weight(1f)
                .background(Primary300)
                .align(Alignment.CenterVertically)
        )
    }
}