package com.vnstudio.talktoai.presentation.screens.settings.settings_sign_up

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.Switch
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
import com.vnstudio.talktoai.domain.models.RemoteUser
import com.vnstudio.talktoai.domain.models.InfoMessage
import com.vnstudio.talktoai.domain.sealed_classes.NavigationScreen
import com.vnstudio.talktoai.presentation.components.*
import com.vnstudio.talktoai.presentation.theme.Primary300
import com.vnstudio.talktoai.presentation.theme.Primary50

@Composable
fun SettingsSignUpScreen(
    infoMessageState: MutableState<InfoMessage?>,
    isMainProgressVisible: MutableState<Boolean>,
    onNextScreen: (String) -> Unit,
) {

    val viewModel: SettingSignUpViewModel = hiltViewModel()
    val emailInputValue = remember { mutableStateOf(TextFieldValue()) }
    val passwordInputValue = remember { mutableStateOf(TextFieldValue()) }
    val showAccountExistDialog = remember { mutableStateOf(false) }
    val transferDataState = remember { mutableStateOf(true) }

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
            viewModel.createUserWithGoogle(idToken, false)
        }
    }

    val successAuthorisationState = viewModel.successAuthorisationLiveData.observeAsState()
    LaunchedEffect(successAuthorisationState.value) {
        successAuthorisationState.value?.let { isExistUser ->
            if (transferDataState.value) {
                viewModel.createRemoteUser(isExistUser)
            } else {
                viewModel.remoteUserLiveData.postValue(Pair(isExistUser, RemoteUser()))
            }
        }
    }

    val localUserState = viewModel.remoteUserLiveData.observeAsState()
    LaunchedEffect(localUserState.value) {
        localUserState.value?.let {
            if (it.first) {
                viewModel.updateRemoteCurrentUser(it.second)
            } else {
                viewModel.insertRemoteCurrentUser(it.second)
            }
        }
    }

    val successRemoteUserState = viewModel.successRemoteUserLiveData.observeAsState()
    LaunchedEffect(successRemoteUserState.value) {
        successRemoteUserState.value?.let {
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
        verticalArrangement = Arrangement.Top
    ) {
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
        PrimaryTextField(stringResource(id = R.string.authorization_email), emailInputValue)
        PasswordTextField(passwordInputValue, stringResource(id = R.string.authorization_password))
        PrimaryButton(
            text = stringResource(id = R.string.authorization_sign_up),
            emailInputValue.value.text.isNotEmpty() && passwordInputValue.value.text.isNotEmpty(),
            modifier = Modifier
        ) {
            viewModel.fetchSignInMethodsForEmail(emailInputValue.value.text.trim())
        }
        TransferDataCard(transferDataState)
    }
    ConfirmationDialog(
        stringResource(id = R.string.authorization_account_exist),
        showAccountExistDialog,
        onDismiss = {
            viewModel.googleSignInClient.signOut()
            showAccountExistDialog.value = false
        }) {
        viewModel.accountExistLiveData.value.takeIf { it.isNullOrEmpty().not() }?.let { idToken ->
            viewModel.createUserWithGoogle(idToken, true)
        } ?: viewModel.signInWithEmailAndPassword(
            emailInputValue.value.text,
            passwordInputValue.value.text
        )
        showAccountExistDialog.value = false
    }
    ExceptionMessageHandler(infoMessageState, viewModel.exceptionLiveData)
}

@Composable
fun TransferDataCard(transferDataState: MutableState<Boolean>) {
    Card(modifier = Modifier.padding(top = 16.dp)) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = stringResource(id = R.string.settings_account_transfer_data_title),
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 8.dp)
                )
                Switch(checked = transferDataState.value, onCheckedChange = { isChecked ->
                    transferDataState.value = isChecked
                })
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(Primary300)
            )
            Text(
                text = if (transferDataState.value) stringResource(id = R.string.settings_account_transfer_data_turn_on) else stringResource(id = R.string.settings_account_transfer_data_turn_off),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            )
        }
    }
}

