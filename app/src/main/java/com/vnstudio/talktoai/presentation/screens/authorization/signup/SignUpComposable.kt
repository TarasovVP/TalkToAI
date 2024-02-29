package com.vnstudio.talktoai.presentation.screens.authorization.signup

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.CommonStatusCodes

import com.vnstudio.talktoai.domain.models.InfoMessage
import com.vnstudio.talktoai.domain.models.RemoteUser
import com.vnstudio.talktoai.domain.sealed_classes.NavigationScreen
import com.vnstudio.talktoai.infrastructure.Constants.DEFAULT_CHAT_ID
import com.vnstudio.talktoai.infrastructure.Constants.DESTINATION_CHAT_SCREEN
import com.vnstudio.talktoai.presentation.components.*

@Composable
fun SignUpScreen(
    viewModel: SignUpViewModel,
    infoMessageState: MutableState<InfoMessage?>,
    progressVisibilityState: MutableState<Boolean>,
    onNextScreen: (String) -> Unit
) {
    val emailInputValue = remember { mutableStateOf(TextFieldValue()) }
    val passwordInputValue = remember { mutableStateOf(TextFieldValue()) }
    val showAccountExistDialog = remember { mutableStateOf(false) }

    val signUpUiState by viewModel.uiState.collectAsState()

    LaunchedEffect(signUpUiState) {
        signUpUiState.accountExist?.let {
            viewModel.googleSignInClient.signOut()
            showAccountExistDialog.value = true
            signUpUiState.accountExist = null
        }
        signUpUiState.createEmailAccount?.let {
            viewModel.createUserWithEmailAndPassword(
                emailInputValue.value.text.trim(),
                passwordInputValue.value.text
            )
        }
        signUpUiState.createGoogleAccount?.let { idToken ->
            viewModel.createUserWithGoogle(idToken)
        }
        signUpUiState.successSignUp?.let {
            viewModel.insertRemoteUser(RemoteUser())
        }
        signUpUiState.createCurrentUser?.let {
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
                viewModel.googleSignInClient.signOut()
                viewModel.exceptionLiveData.value = CommonStatusCodes.getStatusCodeString(e.statusCode)
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
            text = stringRes().AUTHORIZATION_SIGN_UP, modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp), textAlign = TextAlign.Center
        )
        Text(
            text = stringRes().AUTHORIZATION_WITH_GOOGLE_ACCOUNT,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )
        GoogleButton(
            stringRes().AUTHORIZATION_SIGNING_UP,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(16.dp)
        ) {
            launcher.launch(viewModel.googleSignInClient.signInIntent)
        }
        OrDivider(modifier = Modifier)
        PrimaryTextField(
            stringRes().AUTHORIZATION_EMAIL, emailInputValue)
        PasswordTextField(passwordInputValue, stringRes().AUTHORIZATION_PASSWORD)
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = stringRes().AUTHORIZATION_ENTRANCE_TITLE, modifier = Modifier.padding(start = 16.dp))
            LinkButton(text = stringRes().AUTHORIZATION_ENTRANCE, modifier = Modifier.wrapContentSize()) {
                onNextScreen.invoke(NavigationScreen.LoginScreen().route)
            }
        }
        PrimaryButton(
            text = stringRes().AUTHORIZATION_SIGNING_UP,
            emailInputValue.value.text.isNotEmpty() && passwordInputValue.value.text.isNotEmpty(),
            modifier = Modifier
        ) {
            viewModel.fetchSignInMethodsForEmail(emailInputValue.value.text.trim())
        }
    }
    ConfirmationDialog(
        stringRes().AUTHORIZATION_ACCOUNT_EXIST,
        showAccountExistDialog,
        onDismiss = {
            showAccountExistDialog.value = false
        }) {
        showAccountExistDialog.value = false
        onNextScreen.invoke(NavigationScreen.LoginScreen().route)
    }
    ExceptionMessageHandler(infoMessageState, viewModel.exceptionLiveData)
    ProgressVisibilityHandler(progressVisibilityState, viewModel.progressVisibilityLiveData)
}