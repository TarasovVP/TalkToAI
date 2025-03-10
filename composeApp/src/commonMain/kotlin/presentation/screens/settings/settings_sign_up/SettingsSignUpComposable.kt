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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.vnteam.talktoai.presentation.ui.components.ConfirmationDialog
import com.vnteam.talktoai.presentation.ui.components.GoogleButton
import com.vnteam.talktoai.presentation.ui.components.OrDivider
import com.vnteam.talktoai.presentation.ui.components.PasswordTextField
import com.vnteam.talktoai.presentation.ui.components.PrimaryButton
import com.vnteam.talktoai.presentation.ui.components.PrimaryTextField
import com.vnteam.talktoai.presentation.ui.resources.LocalStringResources
import com.vnteam.talktoai.presentation.ui.theme.Primary300
import com.vnteam.talktoai.presentation.viewmodels.settings.SettingsSignUpViewModel
import org.koin.compose.viewmodel.koinViewModel
import presentation.updateScreenState

@Composable
fun SettingsSignUpScreen() {
    val viewModel: SettingsSignUpViewModel = koinViewModel()
    updateScreenState(viewModel.progressVisibilityState.collectAsState().value)
    val emailInputValue = remember { mutableStateOf(TextFieldValue()) }
    val passwordInputValue = remember { mutableStateOf(TextFieldValue()) }
    val showAccountExistDialog = remember { mutableStateOf(false) }
    val transferDataState = remember { mutableStateOf(true) }

    val settingsSignUpUIStateState by viewModel.uiState.collectAsState()
    LaunchedEffect(settingsSignUpUIStateState) {
        settingsSignUpUIStateState.accountExist?.let {
            viewModel.googleSign()
            showAccountExistDialog.value = true
        }
        settingsSignUpUIStateState.createEmailAccount?.let {
            viewModel.createUserWithEmailAndPassword(
                emailInputValue.value.text.trim(),
                passwordInputValue.value.text
            )
        }
        settingsSignUpUIStateState.createGoogleAccount?.let { idToken ->
            viewModel.createUserWithGoogle(idToken, false)
        }
        settingsSignUpUIStateState.successAuthorisation?.let { isExistUser ->
            if (transferDataState.value) {
                viewModel.createRemoteUser(isExistUser)
            }
        }
        settingsSignUpUIStateState.remoteUser?.let { userState ->
            if (userState.first) {
                userState.second?.let { viewModel.updateRemoteCurrentUser(it) }
            } else {
                userState.second?.let { viewModel.insertRemoteCurrentUser(it) }
            }
        }
        settingsSignUpUIStateState.successRemoteUser?.let { successRemoteUser ->
            if (successRemoteUser) {
                //updateScreenState(screenRoute = "${NavigationScreen.CHAT_DESTINATION}/${DEFAULT_CHAT_ID}")
            }
        }
    }

    SettingsSignUpContent(
        viewModel,
        emailInputValue,
        passwordInputValue,
        transferDataState
    )

    ConfirmationDialog(
        LocalStringResources.current.SETTINGS_ACCOUNT_EXIST,
        showAccountExistDialog
    ) {
        /*viewModel.accountExistLiveData.value.takeIf { it.isNotEmpty() }?.let { idToken ->
            viewModel.createUserWithGoogle(idToken, true)
        } ?:*/ viewModel.signInWithEmailAndPassword(
        emailInputValue.value.text,
        passwordInputValue.value.text
    )
        showAccountExistDialog.value = false
    }
}

@Composable
fun SettingsSignUpContent(
    viewModel: SettingsSignUpViewModel,
    emailInputValue: MutableState<TextFieldValue>,
    passwordInputValue: MutableState<TextFieldValue>,
    transferDataState: MutableState<Boolean>
) {
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
            viewModel.googleSign()
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
