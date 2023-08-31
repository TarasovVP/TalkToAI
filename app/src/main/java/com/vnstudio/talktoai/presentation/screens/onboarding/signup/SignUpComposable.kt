package com.vnstudio.talktoai.presentation.screens.onboarding.signup

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.CommonStatusCodes
import com.vnstudio.talktoai.domain.models.InfoMessage
import com.vnstudio.talktoai.domain.sealed_classes.NavigationScreen
import com.vnstudio.talktoai.presentation.components.*
import com.vnstudio.talktoai.presentation.theme.Primary50

@Composable
fun SignUpScreen(messageState: MutableState<InfoMessage?>, onNextScreen: (String) -> Unit) {

    val viewModel: SignUpViewModel = hiltViewModel()
    val emailInputValue = remember { mutableStateOf(TextFieldValue()) }
    val passwordInputValue = remember { mutableStateOf(TextFieldValue()) }
    val showAccountExistDialog = remember { mutableStateOf(false) }

    val accountExistState = viewModel.accountExistLiveData.observeAsState()
    LaunchedEffect(accountExistState.value) {
        accountExistState.value?.let {
            showAccountExistDialog.value = true
        }
    }
    val isEmailAccountExistState = viewModel.createEmailAccountLiveData.observeAsState()
    LaunchedEffect(isEmailAccountExistState.value) {
        isEmailAccountExistState.value?.let {
            viewModel.createUserWithEmailAndPassword(emailInputValue.value.text.trim(),
                passwordInputValue.value.text)
        }
    }
    val isGoogleAccountExistState = viewModel.createGoogleAccountLiveData.observeAsState()
    LaunchedEffect(isGoogleAccountExistState.value) {
        isGoogleAccountExistState.value?.let { idToken ->
            viewModel.createUserWithGoogle(idToken)
        }
    }
    val successSignInState = viewModel.createCurrentUserLiveData.observeAsState()
    LaunchedEffect(successSignInState.value) {
        successSignInState.value?.let {
            onNextScreen.invoke(NavigationScreen.ChatScreen().route)
        }
    }

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
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
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = "Регистрация", modifier = Modifier
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
        PasswordTextField(passwordInputValue)
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = "Есть аккаунт?")
            LinkButton(text = "Вход", modifier = Modifier
                .wrapContentSize()) {
                onNextScreen.invoke(NavigationScreen.LoginScreen().route)
            }
        }
        PrimaryButton(text = "Зарегистрироваться", emailInputValue.value.text.isNotEmpty() && passwordInputValue.value.text.isNotEmpty(), modifier = Modifier) {
            viewModel.fetchSignInMethodsForEmail(emailInputValue.value.text.trim())
        }
    }
    ExceptionMessageHandler(messageState, viewModel.exceptionLiveData)
}