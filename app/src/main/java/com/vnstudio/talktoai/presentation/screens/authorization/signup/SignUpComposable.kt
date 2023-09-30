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
import com.vnstudio.talktoai.R
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
                viewModel.exceptionLiveData.postValue(CommonStatusCodes.getStatusCodeString(e.statusCode))
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
            stringResource(id = R.string.authorization_signing_up),
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(16.dp)
        ) {
            launcher.launch(viewModel.googleSignInClient.signInIntent)
        }
        OrDivider(modifier = Modifier)
        PrimaryTextField(
            stringResource(id = R.string.authorization_email), emailInputValue)
        PasswordTextField(passwordInputValue, stringResource(id = R.string.authorization_password))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = stringResource(id = R.string.authorization_entrance_title), modifier = Modifier.padding(start = 16.dp))
            LinkButton(text = stringResource(id = R.string.authorization_entrance), modifier = Modifier.wrapContentSize()) {
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
    ConfirmationDialog(
        stringResource(id = R.string.authorization_account_exist),
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