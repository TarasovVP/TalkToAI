package com.vnstudio.talktoai.presentation.screens.authorization.signup

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.vnstudio.talktoai.domain.models.RemoteUser
import com.vnstudio.talktoai.domain.sealed_classes.NavigationScreen
import com.vnstudio.talktoai.infrastructure.Constants
import com.vnstudio.talktoai.infrastructure.Constants.DEFAULT_CHAT_ID
import com.vnstudio.talktoai.infrastructure.Constants.DESTINATION_CHAT_SCREEN
import com.vnstudio.talktoai.presentation.components.*
import com.vnstudio.talktoai.presentation.theme.Primary50

@Composable
fun SignUpScreen(
    infoMessageState: MutableState<InfoMessage?>,
    isMainProgressVisible: MutableState<Boolean>,
    onNextScreen: (String) -> Unit
) {

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
            viewModel.createUserWithEmailAndPassword(
                emailInputValue.value.text.trim(),
                passwordInputValue.value.text
            )
        }
    }
    val isGoogleAccountExistState = viewModel.createGoogleAccountLiveData.observeAsState()
    LaunchedEffect(isGoogleAccountExistState.value) {
        isGoogleAccountExistState.value?.let { idToken ->
            viewModel.createUserWithGoogle(idToken)
        }
    }
    val successSignUpState = viewModel.successSignUpLiveData.observeAsState()
    LaunchedEffect(successSignUpState.value) {
        successSignUpState.value?.let {
            viewModel.insertRemoteUser(RemoteUser())
        }
    }
    val successSignInState = viewModel.createCurrentUserLiveData.observeAsState()
    LaunchedEffect(successSignInState.value) {
        successSignInState.value?.let {
            onNextScreen.invoke("${DESTINATION_CHAT_SCREEN}/${DEFAULT_CHAT_ID}")
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
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = stringResource(id = R.string.authorization_sign_up), modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp), textAlign = TextAlign.Center
        )
        Text(
            text = stringResource(id = R.string.authorization_with_google_account),
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
        PrimaryTextField(
            stringResource(id = R.string.authorization_enter), emailInputValue)
        PasswordTextField(passwordInputValue, stringResource(id = R.string.authorization_password))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = stringResource(id = R.string.authorization_entrance_title))
            LinkButton(
                text = stringResource(id = R.string.authorization_enter), modifier = Modifier
                    .wrapContentSize()
            ) {
                onNextScreen.invoke(NavigationScreen.LoginScreen().route)
            }
        }
        PrimaryButton(
            text = stringResource(id = R.string.authorization_signing_up),
            emailInputValue.value.text.isNotEmpty() && passwordInputValue.value.text.isNotEmpty(),
            modifier = Modifier
        ) {
            viewModel.fetchSignInMethodsForEmail(emailInputValue.value.text.trim())
        }
    }
    ExceptionMessageHandler(infoMessageState, viewModel.exceptionLiveData)
}