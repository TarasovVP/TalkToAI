package presentation.screens.settings.settings_sign_up

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
import androidx.compose.material3.Card
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
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
import com.vnstudio.talktoai.infrastructure.Constants.DEFAULT_CHAT_ID
import com.vnstudio.talktoai.infrastructure.Constants.DESTINATION_CHAT_SCREEN
import com.vnteam.talktoai.domain.models.RemoteUser
import com.vnteam.talktoai.presentation.uimodels.screen.ScreenState
import com.vnteam.talktoai.presentation.viewmodels.SettingsSignUpViewModel
import components.ConfirmationDialog
import components.ExceptionMessageHandler
import components.GoogleButton
import components.OrDivider
import components.PasswordTextField
import components.PrimaryButton
import components.PrimaryTextField
import components.ProgressVisibilityHandler
import org.koin.compose.viewmodel.koinViewModel
import resources.LocalStringResources
import theme.Primary300

@Composable
fun SettingsSignUpContent(
    screenState: ScreenState
) {

    val viewModel: SettingsSignUpViewModel = koinViewModel()
    val emailInputValue = remember { mutableStateOf(TextFieldValue()) }
    val passwordInputValue = remember { mutableStateOf(TextFieldValue()) }
    val showAccountExistDialog = remember { mutableStateOf(false) }
    val transferDataState = remember { mutableStateOf(true) }

    val accountExistState = viewModel.accountExistLiveData.collectAsState()
    LaunchedEffect(accountExistState.value) {
        accountExistState.value.takeIf { it.isNotEmpty() }?.let {
            //viewModel.googleSignInClient.signOut()
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
            screenState.currentScreenState.value = "${DESTINATION_CHAT_SCREEN}/${DEFAULT_CHAT_ID}"
        }
    }

    /*val launcher =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                val account = task.getResult(ApiException::class.java)
                account.email?.let { viewModel.fetchSignInMethodsForEmail(it, account.idToken) }
            } catch (e: ApiException) {
                viewModel.googleSignInClient.signOut()
                viewModel.exceptionLiveData.value = CommonStatusCodes.getStatusCodeString(e.statusCode)
            }
        }*/

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top
    ) {
        Spacer(modifier = Modifier.height(40.dp))
        Text(
            text = LocalStringResources.current.AUTHORIZATION_WITH_GOOGLE_ACCOUNT,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )
        GoogleButton(
            LocalStringResources.current.AUTHORIZATION_SIGNING_UP,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(16.dp)
        ) {
            //launcher.launch(viewModel.googleSignInClient.signInIntent)
        }
        OrDivider(modifier = Modifier)
        PrimaryTextField(LocalStringResources.current.AUTHORIZATION_EMAIL, emailInputValue)
        PasswordTextField(passwordInputValue, LocalStringResources.current.AUTHORIZATION_PASSWORD)
        PrimaryButton(
            text = LocalStringResources.current.AUTHORIZATION_SIGN_UP,
            emailInputValue.value.text.isNotEmpty() && passwordInputValue.value.text.isNotEmpty(),
            modifier = Modifier
        ) {
            viewModel.fetchSignInMethodsForEmail(emailInputValue.value.text.trim())
        }
        TransferDataCard(transferDataState)
    }
    ConfirmationDialog(
        LocalStringResources.current.SETTINGS_ACCOUNT_EXIST,
        showAccountExistDialog,
        onDismiss = {
            //viewModel.googleSignInClient.signOut()
            showAccountExistDialog.value = false
        }) {
        viewModel.accountExistLiveData.value.takeIf { it.isNotEmpty() }?.let { idToken ->
            viewModel.createUserWithGoogle(idToken, true)
        } ?: viewModel.signInWithEmailAndPassword(
            emailInputValue.value.text,
            passwordInputValue.value.text
        )
        showAccountExistDialog.value = false
    }
    ExceptionMessageHandler(screenState.infoMessageState, viewModel.exceptionLiveData)
    ProgressVisibilityHandler(mutableStateOf(screenState.isProgressVisible), viewModel.progressVisibilityLiveData)
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
                    text = LocalStringResources.current.SETTINGS_ACCOUNT_TRANSFER_DATA_TITLE,
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
                text = if (transferDataState.value) LocalStringResources.current.SETTINGS_ACCOUNT_TRANSFER_DATA_TURN_ON else LocalStringResources.current.SETTINGS_ACCOUNT_TRANSFER_DATA_TURN_OFF,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            )
        }
    }
}

