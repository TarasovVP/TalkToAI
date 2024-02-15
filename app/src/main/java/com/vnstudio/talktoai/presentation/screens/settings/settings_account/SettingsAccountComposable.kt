package com.vnstudio.talktoai.presentation.screens.settings.settings_account

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import org.koin.androidx.compose.koinViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.GoogleAuthProvider
import com.vnstudio.talktoai.R
import com.vnstudio.talktoai.domain.enums.AuthState
import com.vnstudio.talktoai.domain.models.InfoMessage
import com.vnstudio.talktoai.domain.sealed_classes.NavigationScreen
import com.vnstudio.talktoai.presentation.components.*
import com.vnstudio.talktoai.presentation.theme.Primary500

@Composable
fun SettingsAccountScreen(
    infoMessageState: MutableState<InfoMessage?>,
    progressVisibilityState: MutableState<Boolean>,
    onNextScreen: (String) -> Unit,
) {

    val viewModel: SettingsAccountViewModel = koinViewModel()
    val authState =  remember { mutableStateOf<AuthState?>(null) }
    val showLogOutDialog = remember { mutableStateOf(false) }
    val showChangePasswordDialog = remember { mutableStateOf(false) }
    val showDeleteGoogleAccountDialog = remember { mutableStateOf(false) }
    val showDeleteEmailAccountDialog = remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        authState.value = viewModel.getAuthState()
    }

    val reAuthenticateState = viewModel.reAuthenticateLiveData.observeAsState()
    LaunchedEffect(reAuthenticateState.value) {
        reAuthenticateState.value?.let {
            viewModel.deleteUser()
        }
    }

    val successChangePasswordMessage = InfoMessage(stringResource(id = R.string.settings_account_change_password_succeed))
    val successChangePasswordState = viewModel.successChangePasswordLiveData.observeAsState()
    LaunchedEffect(successChangePasswordState.value) {
        successChangePasswordState.value?.let {
            infoMessageState.value = successChangePasswordMessage
        }
    }

    val successState = viewModel.successLiveData.observeAsState()
    LaunchedEffect(successState.value) {
        successState.value?.let {
            viewModel.clearDataByKeys(listOf())
            viewModel.clearDataBase()
            onNextScreen.invoke(NavigationScreen.LoginScreen().route)
        }
    }

    val launcher =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                val account = task.getResult(ApiException::class.java)
                account.idToken?.let { idToken ->
                    val authCredential = GoogleAuthProvider.getCredential(idToken, null)
                    viewModel.reAuthenticate(authCredential)
                }
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
        AccountCard(authState.value, viewModel.currentUserEmail()) {
            showLogOutDialog.value = true
        }
        if (authState.value == AuthState.AUTHORISED_EMAIL) {
            PrimaryButton(text = stringResource(id = R.string.settings_account_change_password_title), modifier = Modifier) {
                showChangePasswordDialog.value = true
            }
        }
        if (authState.value == AuthState.AUTHORISED_EMAIL || authState.value == AuthState.AUTHORISED_GOOGLE) {
            SecondaryButton(text = stringResource(id = R.string.settings_account_delete_title), true, modifier = Modifier) {
                if (authState.value == AuthState.AUTHORISED_GOOGLE) {
                    showDeleteGoogleAccountDialog.value = true
                } else {
                    showDeleteEmailAccountDialog.value = true
                }
            }
        } else {
            PrimaryButton(text = stringResource(id = R.string.authorization_signing_up), modifier = Modifier) {
                onNextScreen.invoke(NavigationScreen.SettingsSignUpScreen().route)
            }
            EmptyState(
                text = stringResource(id = R.string.empty_state_account),
                modifier = Modifier
            )
        }
    }

    ChangePasswordDialog(showChangePasswordDialog) { password ->
        viewModel.changePassword(password.first, password.second)
    }

    ConfirmationDialog(
        when (authState.value) {
            AuthState.AUTHORISED_ANONYMOUSLY -> stringResource(id = R.string.settings_account_unauthorised_log_out)
            else -> stringResource(id = R.string.settings_account_log_out)
        }, showLogOutDialog, onDismiss = {
        showLogOutDialog.value = false
    }) {
        viewModel.signOut()
        showLogOutDialog.value = false
    }

    ConfirmationDialog(
        stringResource(id = R.string.settings_account_google_delete),
        showDeleteGoogleAccountDialog,
        onDismiss = {
            showDeleteGoogleAccountDialog.value = false
        }) {
        launcher.launch(viewModel.googleSignInClient.signInIntent)
        showDeleteGoogleAccountDialog.value = false
    }

    DataEditDialog(
        stringResource(id = R.string.settings_account_email_delete),
        placeHolder = stringResource(id = R.string.settings_account_enter_current_password),
        remember {
            mutableStateOf(TextFieldValue())
        },
        showDeleteEmailAccountDialog,
        onDismiss = {
            showDeleteEmailAccountDialog.value = false
        }) { password ->
        val authCredential = EmailAuthProvider.getCredential(viewModel.currentUserEmail(), password)
        viewModel.reAuthenticate(authCredential)
        showDeleteEmailAccountDialog.value = false
    }

    ExceptionMessageHandler(infoMessageState, viewModel.exceptionLiveData)
    ProgressVisibilityHandler(progressVisibilityState, viewModel.progressVisibilityLiveData)
}

@Composable
fun AccountCard(authState: AuthState?, email: String, onClick: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth(), elevation = 1.dp) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp), verticalAlignment = Alignment.CenterVertically
        ) {
            ShapeableImage(
                modifier = Modifier.size(50.dp),
                drawableResId = when (authState) {
                    AuthState.AUTHORISED_GOOGLE -> R.drawable.ic_avatar_google
                    AuthState.AUTHORISED_EMAIL -> R.drawable.ic_avatar_email
                    else -> R.drawable.ic_avatar_anonymous
                },
                contentDescription = "Account avatar"
            )
            Column(
                modifier = Modifier.wrapContentSize(),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = if (authState != AuthState.AUTHORISED_ANONYMOUSLY) email else stringResource(id = R.string.settings_account_unauthorised), modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 8.dp, top = 8.dp)
                )
                LinkButton(
                    text = stringResource(id = if (authState != AuthState.AUTHORISED_ANONYMOUSLY) R.string.settings_account_log_out_title else R.string.settings_account_unauthorised_log_out_title), modifier = Modifier
                        .wrapContentSize(), onClick = onClick
                )
            }
        }
    }
}

@Composable
fun ChangePasswordDialog(
    showDialog: MutableState<Boolean>,
    onConfirmationClick: (Pair<String, String>) -> Unit,
) {
    if (showDialog.value) {
        val currentPasswordInputValue = remember {
            mutableStateOf(TextFieldValue())
        }
        val newPasswordInputValue = remember {
            mutableStateOf(TextFieldValue())
        }
        Column {
            Dialog(
                onDismissRequest = {
                    showDialog.value = false
                },
                content = {
                    Column(
                        modifier = Modifier
                            .wrapContentSize()
                            .border(1.dp, Primary500, shape = RoundedCornerShape(16.dp))
                            .background(color = Color.White, shape = RoundedCornerShape(16.dp)),
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = stringResource(id = R.string.settings_account_change_password_title),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            textAlign = TextAlign.Center,
                        )
                        PasswordTextField(currentPasswordInputValue, stringResource(id = R.string.settings_account_enter_current_password))
                        PasswordTextField(newPasswordInputValue, stringResource(id = R.string.settings_account_enter_new_password))
                        SubmitButtons(currentPasswordInputValue.value.text.isNotEmpty() && newPasswordInputValue.value.text.isNotEmpty(), {
                            showDialog.value = false
                        }, {
                            showDialog.value = false
                            onConfirmationClick.invoke(Pair(currentPasswordInputValue.value.text, newPasswordInputValue.value.text))
                        })
                    }
                }
            )
        }
    }
}