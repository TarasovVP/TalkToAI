package com.vnstudio.talktoai.presentation.onboarding.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.vnstudio.talktoai.presentation.base.OrDivider
import com.vnstudio.talktoai.presentation.components.*
import com.vnstudio.talktoai.ui.theme.Primary50

@Composable
fun LoginScreen(onClick: () -> Unit) {
    val loginInputValue = remember { mutableStateOf(TextFieldValue()) }
    val passwordInputValue = remember { mutableStateOf(TextFieldValue()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(Primary50),
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Вход", modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp), textAlign = TextAlign.Center)
        Text(text = "С помощью Google", modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
        GoogleButton(modifier = Modifier
            .align(Alignment.CenterHorizontally)
            .padding(16.dp)) {
            onClick.invoke()
        }
        OrDivider(modifier = Modifier)
        PrimaryTextField("Email", loginInputValue)
        PasswordTextField(passwordInputValue)
        Row {
            TextButton(onClick = {

            }, modifier = Modifier.fillMaxWidth().weight(1f)) {
                Text(text = "Регистрация", color = Color.Blue)
            }
            TextButton(onClick = {

            }, modifier = Modifier.fillMaxWidth().weight(1f)) {
                Text(text = "Забыли пароль?", color = Color.Blue, textAlign = TextAlign.End)
            }
        }
        PrimaryButton(text = "Войти", modifier = Modifier, onClick = onClick)
        OrDivider(modifier = Modifier)
        SecondaryButton(text = "Без регистрации", modifier = Modifier) {

        }
    }
}

@Preview(showBackground = false)
@Composable
fun DefaultPreview() {
    LoginScreen {

    }
}