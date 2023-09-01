package com.vnstudio.talktoai.presentation.screens.authorization.login

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.CommonStatusCodes
import com.vnstudio.talktoai.R
import com.vnstudio.talktoai.domain.models.InfoMessage
import com.vnstudio.talktoai.domain.sealed_classes.NavigationScreen
import com.vnstudio.talktoai.presentation.components.*
import com.vnstudio.talktoai.presentation.theme.Primary50

@Composable
fun LoginScreen(infoMessageState: MutableState<InfoMessage?>, onNextScreen: (String) -> Unit) {

    val viewModel: LoginViewModel = hiltViewModel()
    val emailInputValue = remember { mutableStateOf(TextFieldValue()) }
    val passwordInputValue = remember { mutableStateOf(TextFieldValue()) }
    val showForgotPasswordDialog = remember { mutableStateOf(false) }
    val showAccountExistDialog = remember { mutableStateOf(false) }
    val showUnauthorizedEnterDialog = remember { mutableStateOf(false) }

    val accountExistState = viewModel.accountExistLiveData.observeAsState()
    LaunchedEffect(accountExistState.value) {
        accountExistState.value?.let {
            showAccountExistDialog.value = true
        }
    }
    val isEmailAccountExistState = viewModel.isEmailAccountExistLiveData.observeAsState()
    LaunchedEffect(isEmailAccountExistState.value) {
        isEmailAccountExistState.value?.let {
            viewModel.signInWithEmailAndPassword(
                emailInputValue.value.text.trim(),
                passwordInputValue.value.text
            )
        }
    }
    val isGoogleAccountExistState = viewModel.isGoogleAccountExistLiveData.observeAsState()
    LaunchedEffect(isGoogleAccountExistState.value) {
        isGoogleAccountExistState.value?.let { idToken ->
            viewModel.signInAuthWithGoogle(idToken)
        }
    }
    val successPasswordResetState = viewModel.successPasswordResetLiveData.observeAsState()
    LaunchedEffect(successPasswordResetState.value) {
        successPasswordResetState.value?.let {
            viewModel.exceptionLiveData.postValue("Пароль успешно сброшен")
        }
    }
    val successSignInState = viewModel.successSignInLiveData.observeAsState()
    LaunchedEffect(successSignInState.value) {
        successSignInState.value?.let {
            onNextScreen.invoke(NavigationScreen.ChatScreen().route)
        }
    }

    val launcher =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                val account = task.getResult(ApiException::class.java)
                account.email?.let { viewModel.fetchSignInMethodsForEmail(it, account.idToken) }
            } catch (e: ApiException) {
                viewModel.exceptionLiveData.postValue(CommonStatusCodes.getStatusCodeString(e.statusCode))
            }
        }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(Primary50),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Вход", modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp), textAlign = TextAlign.Center
        )
        Text(
            text = "С помощью Google",
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )
        GoogleButton(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(16.dp)
        ) {
            launcher.launch(viewModel.googleSignInClient.signInIntent)
        }
        OrDivider(modifier = Modifier)
        PrimaryTextField("Email", emailInputValue)
        PasswordTextField(passwordInputValue, stringResource(id = R.string.authorization_password))
        Row {
            LinkButton(
                text = "Регистрация", modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                onNextScreen.invoke(NavigationScreen.SignUpScreen().route)
            }
            TextButton(
                onClick = {
                    showForgotPasswordDialog.value = true
                }, modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                Text(text = "Забыли пароль?", color = Color.Blue, textAlign = TextAlign.End)
            }
        }
        PrimaryButton(
            text = "Войти",
            emailInputValue.value.text.isNotEmpty() && passwordInputValue.value.text.isNotEmpty(),
            modifier = Modifier
        ) {
            viewModel.fetchSignInMethodsForEmail(emailInputValue.value.text.trim())
        }
        OrDivider(modifier = Modifier)
        SecondaryButton(text = "Без регистрации", false, modifier = Modifier) {
            showUnauthorizedEnterDialog.value = true
        }
    }

    DataEditDialog(
        "Введите ваш Email и мы отправим  Ваш пароль",
        "Email",
        emailInputValue,
        showForgotPasswordDialog,
        onDismiss = {
            showForgotPasswordDialog.value = false
        }) { email ->
        viewModel.sendPasswordResetEmail(email)
        showForgotPasswordDialog.value = false
    }

    ConfirmationDialog(
        "Пользователя с таким Email не существует. Сначала необходимо создать аккаунт. Перейти на экран регистрации?",
        showAccountExistDialog,
        onDismiss = {
            showAccountExistDialog.value = false
        }) {
        viewModel.googleSignInClient.signOut()
        showAccountExistDialog.value = false
        onNextScreen.invoke(NavigationScreen.SignUpScreen().route)
    }

    ConfirmationDialog(
        "У неавторизованого пользователя недоступен ряд возмножностей. В том числе нет доступа к хранению данных в удаленном доступе",
        showUnauthorizedEnterDialog,
        onDismiss = {
            showUnauthorizedEnterDialog.value = false
        }) {
        viewModel.signInAnonymously()
        showUnauthorizedEnterDialog.value = false
    }
    ExceptionMessageHandler(infoMessageState, viewModel.exceptionLiveData)
}