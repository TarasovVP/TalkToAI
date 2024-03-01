package com.vnstudio.talktoai.presentation.screens.authorization.login

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import com.vnstudio.talktoai.CommonExtensions.isTrue
import com.vnstudio.talktoai.domain.models.InfoMessage
import com.vnstudio.talktoai.domain.sealed_classes.NavigationScreen
import com.vnstudio.talktoai.getStatusCodeText
import com.vnstudio.talktoai.infrastructure.Constants.DEFAULT_CHAT_ID
import com.vnstudio.talktoai.infrastructure.Constants.DESTINATION_CHAT_SCREEN
import com.vnstudio.talktoai.presentation.components.ConfirmationDialog
import com.vnstudio.talktoai.presentation.components.DataEditDialog
import com.vnstudio.talktoai.presentation.components.ExceptionMessageHandler
import com.vnstudio.talktoai.presentation.components.GoogleButton
import com.vnstudio.talktoai.presentation.components.LinkButton
import com.vnstudio.talktoai.presentation.components.OrDivider
import com.vnstudio.talktoai.presentation.components.PasswordTextField
import com.vnstudio.talktoai.presentation.components.PrimaryButton
import com.vnstudio.talktoai.presentation.components.PrimaryTextField
import com.vnstudio.talktoai.presentation.components.ProgressVisibilityHandler
import com.vnstudio.talktoai.presentation.components.SecondaryButton
import com.vnstudio.talktoai.presentation.components.stringRes

@Composable
fun LoginScreen(
    viewModel: LoginViewModel,
    infoMessageState: MutableState<InfoMessage?>,
    progressVisibilityState: MutableState<Boolean>,
    onNextScreen: (String) -> Unit
) {
    val emailInputValue = remember { mutableStateOf(TextFieldValue()) }
    val passwordInputValue = remember { mutableStateOf(TextFieldValue()) }
    val showForgotPasswordDialog = remember { mutableStateOf(false) }
    val showAccountExistDialog = remember { mutableStateOf(false) }
    val showUnauthorizedEnterDialog = remember { mutableStateOf(false) }

    val accountExistState = viewModel.accountExistLiveData.collectAsState()
    LaunchedEffect(accountExistState.value) {
        if (accountExistState.value.isTrue()) {
            viewModel.googleSignInClient.signOut()
            showAccountExistDialog.value = true
            viewModel.accountExistLiveData.value = false
        }
    }
    val isEmailAccountExistState = viewModel.isEmailAccountExistLiveData.collectAsState()
    LaunchedEffect(isEmailAccountExistState.value) {
        if (isEmailAccountExistState.value.isTrue()) {
            viewModel.signInWithEmailAndPassword(
                emailInputValue.value.text.trim(),
                passwordInputValue.value.text
            )
        }
    }
    val isGoogleAccountExistState = viewModel.isGoogleAccountExistLiveData.collectAsState()
    LaunchedEffect(isGoogleAccountExistState.value) {
        isGoogleAccountExistState.value.takeIf { it.isNotEmpty() }?.let { idToken ->
            viewModel.signInAuthWithGoogle(idToken)
        }
    }
    val resetPasswordText = stringRes().AUTHORIZATION_PASSWORD_RESET_SUCCESS
    val successPasswordResetState = viewModel.successPasswordResetLiveData.collectAsState()
    LaunchedEffect(successPasswordResetState.value) {
        if (successPasswordResetState.value.isTrue()) {
            infoMessageState.value = InfoMessage(resetPasswordText)
        }
    }
    val successSignInState = viewModel.successSignInLiveData.collectAsState()
    LaunchedEffect(successSignInState.value) {
        if (successSignInState.value.isTrue()) {
            onNextScreen.invoke("${DESTINATION_CHAT_SCREEN}/$DEFAULT_CHAT_ID")
        }
    }

    val launcher =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                val account = task.getResult(ApiException::class.java)
                account.email?.let { viewModel.fetchSignInMethodsForEmail(it, account.idToken) }
            } catch (e: ApiException) {
                viewModel.googleSignInClient.signOut()
                viewModel.exceptionLiveData.value = e.getStatusCodeText()
            }
        }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top
    ) {
        Spacer(modifier = Modifier.height(40.dp))
        Text(
            text = stringRes().AUTHORIZATION_ENTRANCE, modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp), textAlign = TextAlign.Center
        )
        Text(
            text = stringRes().AUTHORIZATION_WITH_GOOGLE_ACCOUNT,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )
        GoogleButton(
            stringRes().AUTHORIZATION_ENTRANCE,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(16.dp)
        ) {
            launcher.launch(viewModel.googleSignInClient.signInIntent)
        }
        OrDivider(modifier = Modifier)
        PrimaryTextField(stringRes().AUTHORIZATION_EMAIL, emailInputValue)
        PasswordTextField(passwordInputValue, stringRes().AUTHORIZATION_PASSWORD)
        Row(
            modifier = Modifier.fillMaxWidth()
        )  {
            LinkButton(text = stringRes().AUTHORIZATION_SIGN_UP, textAlign = TextAlign.Start, modifier = Modifier
                .wrapContentSize()
                .padding(start = 16.dp)) {
                onNextScreen.invoke(NavigationScreen.SignUpScreen().route)
            }
            Spacer(modifier = Modifier.weight(0.5f))
            LinkButton(text = stringRes().AUTHORIZATION_FORGOT_PASSWORD, textAlign = TextAlign.End, modifier = Modifier
                .wrapContentSize()
                .padding(end = 16.dp)) {
                showForgotPasswordDialog.value = true
            }
        }
        PrimaryButton(
            text = stringRes().AUTHORIZATION_ENTER,
            emailInputValue.value.text.isNotEmpty() && passwordInputValue.value.text.isNotEmpty(),
            modifier = Modifier
        ) {
            viewModel.fetchSignInMethodsForEmail(emailInputValue.value.text.trim())
        }
        OrDivider(modifier = Modifier)
        SecondaryButton(text = stringRes().AUTHORIZATION_CONTINUE_WITHOUT_ACCOUNT, false, modifier = Modifier) {
            showUnauthorizedEnterDialog.value = true
        }
    }

    DataEditDialog(
        stringRes().AUTHORIZATION_FORGOT_PASSWORD_TITLE,
        stringRes().AUTHORIZATION_EMAIL,
        emailInputValue,
        showForgotPasswordDialog,
        onDismiss = {
            showForgotPasswordDialog.value = false
        }) { email ->
        viewModel.sendPasswordResetEmail(email)
        showForgotPasswordDialog.value = false
    }

    ConfirmationDialog(
        stringRes().AUTHORIZATION_ACCOUNT_NOT_EXIST,
        showAccountExistDialog,
        onDismiss = {
            showAccountExistDialog.value = false
        }) {
        showAccountExistDialog.value = false
        onNextScreen.invoke(NavigationScreen.SignUpScreen().route)
    }

    ConfirmationDialog(
        stringRes().AUTHORIZATION_UNAUTHORIZED_ENTER,
        showUnauthorizedEnterDialog,
        onDismiss = {
            showUnauthorizedEnterDialog.value = false
        }) {
        viewModel.signInAnonymously()
        showUnauthorizedEnterDialog.value = false
    }
    ExceptionMessageHandler(infoMessageState, viewModel.exceptionLiveData)
    ProgressVisibilityHandler(progressVisibilityState, viewModel.progressVisibilityLiveData)
}