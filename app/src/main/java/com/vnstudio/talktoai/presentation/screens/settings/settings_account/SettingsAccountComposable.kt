package com.vnstudio.talktoai.presentation.screens.settings.settings_account

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.GoogleAuthProvider
import com.vnstudio.talktoai.domain.enums.AuthState
import com.vnstudio.talktoai.domain.models.InfoMessage
import com.vnstudio.talktoai.domain.models.ScreenState
import com.vnstudio.talktoai.domain.sealed_classes.NavigationScreen
import com.vnstudio.talktoai.infrastructure.Constants
import com.vnstudio.talktoai.infrastructure.Constants.REVIEW_VOTE
import com.vnstudio.talktoai.presentation.components.ConfirmationDialog
import com.vnstudio.talktoai.presentation.components.DataEditDialog
import com.vnstudio.talktoai.presentation.components.EmptyState
import com.vnstudio.talktoai.presentation.components.ExceptionMessageHandler
import com.vnstudio.talktoai.presentation.components.LinkButton
import com.vnstudio.talktoai.presentation.components.PasswordTextField
import com.vnstudio.talktoai.presentation.components.PrimaryButton
import com.vnstudio.talktoai.presentation.components.ProgressVisibilityHandler
import com.vnstudio.talktoai.presentation.components.SecondaryButton
import com.vnstudio.talktoai.presentation.components.ShapeableImage
import com.vnstudio.talktoai.presentation.components.SubmitButtons
import com.vnstudio.talktoai.presentation.components.stringRes
import com.vnstudio.talktoai.presentation.theme.Primary500
import com.vnstudio.talktoai.resources.DrawableResources
import org.koin.androidx.compose.koinViewModel

@Composable
fun SettingsAccountContent(
    screenState: ScreenState
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

    val reAuthenticateState = viewModel.reAuthenticateLiveData.collectAsState()
    LaunchedEffect(reAuthenticateState.value) {
        if (reAuthenticateState.value) {
            viewModel.deleteUser()
        }
    }

    val successChangePasswordMessage = InfoMessage(stringRes().SETTINGS_ACCOUNT_CHANGE_PASSWORD_SUCCEED)
    val successChangePasswordState = viewModel.successChangePasswordLiveData.collectAsState()
    LaunchedEffect(successChangePasswordState.value) {
        if (successChangePasswordState.value) {
            screenState.infoMessageState.value = successChangePasswordMessage
        }
    }

    val successState = viewModel.successLiveData.collectAsState()
    LaunchedEffect(successState.value) {
        if (successState.value) {
            viewModel.clearDataByKeys(listOf(REVIEW_VOTE))
            viewModel.clearDataBase()
            screenState.currentScreenState.value = NavigationScreen.LoginScreen().route
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
                viewModel.exceptionLiveData.value = CommonStatusCodes.getStatusCodeString(e.statusCode)
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
            PrimaryButton(text = stringRes().SETTINGS_ACCOUNT_CHANGE_PASSWORD_TITLE, modifier = Modifier) {
                showChangePasswordDialog.value = true
            }
        }
        if (authState.value == AuthState.AUTHORISED_EMAIL || authState.value == AuthState.AUTHORISED_GOOGLE) {
            SecondaryButton(text = stringRes().SETTINGS_ACCOUNT_DELETE_TITLE, true, modifier = Modifier) {
                if (authState.value == AuthState.AUTHORISED_GOOGLE) {
                    showDeleteGoogleAccountDialog.value = true
                } else {
                    showDeleteEmailAccountDialog.value = true
                }
            }
        } else {
            PrimaryButton(text = stringRes().AUTHORIZATION_SIGNING_UP, modifier = Modifier) {
                screenState.currentScreenState.value =  NavigationScreen.SettingsSignUpScreen().route
            }
            EmptyState(
                text = stringRes().EMPTY_STATE_ACCOUNT,
                modifier = Modifier
            )
        }
    }

    ChangePasswordDialog(showChangePasswordDialog) { password ->
        viewModel.changePassword(password.first, password.second)
    }

    ConfirmationDialog(
        when (authState.value) {
            AuthState.AUTHORISED_ANONYMOUSLY -> stringRes().SETTINGS_ACCOUNT_UNAUTHORISED_LOG_OUT
            else -> stringRes().SETTINGS_ACCOUNT_LOG_OUT
        }, showLogOutDialog, onDismiss = {
        showLogOutDialog.value = false
    }) {
        viewModel.signOut()
        showLogOutDialog.value = false
    }

    ConfirmationDialog(
        stringRes().SETTINGS_ACCOUNT_GOOGLE_DELETE,
        showDeleteGoogleAccountDialog,
        onDismiss = {
            showDeleteGoogleAccountDialog.value = false
        }) {
        launcher.launch(viewModel.googleSignInClient.signInIntent)
        showDeleteGoogleAccountDialog.value = false
    }

    DataEditDialog(
        stringRes().SETTINGS_ACCOUNT_EMAIL_DELETE,
        placeHolder = stringRes().SETTINGS_ACCOUNT_ENTER_CURRENT_PASSWORD,
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

    ExceptionMessageHandler(screenState.infoMessageState, viewModel.exceptionLiveData)
    ProgressVisibilityHandler(screenState.progressVisibilityState, viewModel.progressVisibilityLiveData)
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
                    AuthState.AUTHORISED_GOOGLE -> DrawableResources.IC_AVATAR_GOOGLE
                    AuthState.AUTHORISED_EMAIL -> DrawableResources.IC_AVATAR_EMAIL
                    else -> DrawableResources.IC_AVATAR_ANONYMOUS
                },
                contentDescription = stringRes().ACCOUNT_AVATAR
            )
            Column(
                modifier = Modifier.wrapContentSize(),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = if (authState != AuthState.AUTHORISED_ANONYMOUSLY) email else stringRes().SETTINGS_ACCOUNT_UNAUTHORISED, modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 8.dp, top = 8.dp)
                )
                LinkButton(
                    text = if (authState != AuthState.AUTHORISED_ANONYMOUSLY) stringRes().SETTINGS_ACCOUNT_LOG_OUT_TITLE else stringRes().SETTINGS_ACCOUNT_UNAUTHORISED_LOG_OUT_TITLE, modifier = Modifier
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
                            text = stringRes().SETTINGS_ACCOUNT_CHANGE_PASSWORD_TITLE,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            textAlign = TextAlign.Center,
                        )
                        PasswordTextField(currentPasswordInputValue, stringRes().SETTINGS_ACCOUNT_ENTER_CURRENT_PASSWORD)
                        PasswordTextField(newPasswordInputValue, stringRes().SETTINGS_ACCOUNT_ENTER_NEW_PASSWORD)
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