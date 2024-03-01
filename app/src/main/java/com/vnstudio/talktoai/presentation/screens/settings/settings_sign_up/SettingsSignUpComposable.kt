package com.vnstudio.talktoai.presentation.screens.settings.settings_sign_up

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.Switch
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
import com.google.android.gms.common.api.CommonStatusCodes
import com.vnstudio.talktoai.domain.models.InfoMessage
import com.vnstudio.talktoai.domain.models.RemoteUser
import com.vnstudio.talktoai.infrastructure.Constants.DEFAULT_CHAT_ID
import com.vnstudio.talktoai.infrastructure.Constants.DESTINATION_CHAT_SCREEN
import com.vnstudio.talktoai.presentation.components.ConfirmationDialog
import com.vnstudio.talktoai.presentation.components.ExceptionMessageHandler
import com.vnstudio.talktoai.presentation.components.GoogleButton
import com.vnstudio.talktoai.presentation.components.OrDivider
import com.vnstudio.talktoai.presentation.components.PasswordTextField
import com.vnstudio.talktoai.presentation.components.PrimaryButton
import com.vnstudio.talktoai.presentation.components.PrimaryTextField
import com.vnstudio.talktoai.presentation.components.ProgressVisibilityHandler
import com.vnstudio.talktoai.presentation.components.stringRes
import com.vnstudio.talktoai.presentation.theme.Primary300
import org.koin.androidx.compose.koinViewModel

@Composable
fun SettingsSignUpScreen(
    infoMessageState: MutableState<InfoMessage?>,
    progressVisibilityState: MutableState<Boolean>,
    onNextScreen: (String) -> Unit,
) {

    val viewModel: SettingSignUpViewModel = koinViewModel()
    val emailInputValue = remember { mutableStateOf(TextFieldValue()) }
    val passwordInputValue = remember { mutableStateOf(TextFieldValue()) }
    val showAccountExistDialog = remember { mutableStateOf(false) }
    val transferDataState = remember { mutableStateOf(true) }

    val accountExistState = viewModel.accountExistLiveData.collectAsState()
    LaunchedEffect(accountExistState.value) {
        accountExistState.value.takeIf { it.isNotEmpty() }?.let {
            viewModel.googleSignInClient.signOut()
            showAccountExistDialog.value = true
        }
    }
    val isEmailAccountExistState = viewModel.createEmailAccountLiveData.collectAsState()
    LaunchedEffect(isEmailAccountExistState.value) {
        if (isEmailAccountExistState.value) {
            viewModel.createUserWithEmailAndPassword(
                emailInputValue.value.text.trim(),
                passwordInputValue.value.text
            )
        }
    }
    val isGoogleAccountExistState = viewModel.createGoogleAccountLiveData.collectAsState()
    LaunchedEffect(isGoogleAccountExistState.value) {
        isGoogleAccountExistState.value.takeIf { it.isNotEmpty() }?.let { idToken ->
            viewModel.createUserWithGoogle(idToken, false)
        }
    }

    val successAuthorisationState = viewModel.successAuthorisationLiveData.collectAsState()
    LaunchedEffect(successAuthorisationState.value) {
        successAuthorisationState.value?.let { isExistUser ->
            if (transferDataState.value) {
                viewModel.createRemoteUser(isExistUser)
            } else {
                viewModel.remoteUserLiveData.value = Pair(isExistUser, RemoteUser())
            }
        }
    }

    val localUserState = viewModel.remoteUserLiveData.collectAsState()
    LaunchedEffect(localUserState.value) {
        localUserState.value?.let { userState ->
            if (userState.first) {
                userState.second?.let { it -> viewModel.updateRemoteCurrentUser(it) }
            } else {
                userState.second?.let { it -> viewModel.insertRemoteCurrentUser(it) }
            }
        }
    }

    val successRemoteUserState = viewModel.successRemoteUserLiveData.collectAsState()
    LaunchedEffect(successRemoteUserState.value) {
        if (successRemoteUserState.value) {
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
        PrimaryTextField(stringRes().AUTHORIZATION_EMAIL, emailInputValue)
        PasswordTextField(passwordInputValue, stringRes().AUTHORIZATION_PASSWORD)
        PrimaryButton(
            text = stringRes().AUTHORIZATION_SIGN_UP,
            emailInputValue.value.text.isNotEmpty() && passwordInputValue.value.text.isNotEmpty(),
            modifier = Modifier
        ) {
            viewModel.fetchSignInMethodsForEmail(emailInputValue.value.text.trim())
        }
        TransferDataCard(transferDataState)
    }
    ConfirmationDialog(
        stringRes().SETTINGS_ACCOUNT_EXIST,
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
    ProgressVisibilityHandler(progressVisibilityState, viewModel.progressVisibilityLiveData)
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
                    text = stringRes().SETTINGS_ACCOUNT_TRANSFER_DATA_TITLE,
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
                text = if (transferDataState.value) stringRes().SETTINGS_ACCOUNT_TRANSFER_DATA_TURN_ON else stringRes().SETTINGS_ACCOUNT_TRANSFER_DATA_TURN_OFF,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            )
        }
    }
}

