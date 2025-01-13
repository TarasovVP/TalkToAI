package presentation.screens.settings.settings_account

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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.vnteam.talktoai.CommonExtensions.EMPTY
import com.vnteam.talktoai.CommonExtensions.isTrue
import com.vnteam.talktoai.Res
import com.vnteam.talktoai.data.ANONYMOUS_USER
import com.vnteam.talktoai.data.GOOGLE_USER
import com.vnteam.talktoai.domain.enums.AuthState
import com.vnteam.talktoai.ic_avatar_anonymous
import com.vnteam.talktoai.ic_avatar_email
import com.vnteam.talktoai.ic_avatar_google
import com.vnteam.talktoai.presentation.ui.NavigationScreen
import com.vnteam.talktoai.presentation.ui.components.ConfirmationDialog
import com.vnteam.talktoai.presentation.ui.components.DataEditDialog
import com.vnteam.talktoai.presentation.ui.components.EmptyState
import com.vnteam.talktoai.presentation.ui.components.LinkButton
import com.vnteam.talktoai.presentation.ui.components.PasswordTextField
import com.vnteam.talktoai.presentation.ui.components.PrimaryButton
import com.vnteam.talktoai.presentation.ui.components.SecondaryButton
import com.vnteam.talktoai.presentation.ui.components.ShapeableImage
import com.vnteam.talktoai.presentation.ui.components.SubmitButtons
import com.vnteam.talktoai.presentation.ui.resources.LocalStringResources
import com.vnteam.talktoai.presentation.ui.theme.Primary500
import com.vnteam.talktoai.presentation.uimodels.screen.AppMessage
import com.vnteam.talktoai.presentation.viewmodels.SettingsAccountViewModel
import org.koin.compose.viewmodel.koinViewModel
import presentation.updateScreenState

@Composable
fun SettingsAccountScreen() {
    val viewModel = koinViewModel<SettingsAccountViewModel>()
    updateScreenState(viewModel.progressVisibilityState.collectAsState().value)

    val authState = remember { mutableStateOf<AuthState?>(null) }
    val showLogOutDialog = remember { mutableStateOf(false) }
    val showChangePasswordDialog = remember { mutableStateOf(false) }
    val showDeleteGoogleAccountDialog = remember { mutableStateOf(false) }
    val showDeleteEmailAccountDialog = remember { mutableStateOf(false) }

    val updatedScreenRoute = remember { mutableStateOf(String.EMPTY) }
    if (updatedScreenRoute.value.isNotEmpty()) {
        updateScreenState(screenRoute = updatedScreenRoute.value)
        updatedScreenRoute.value = String.EMPTY
    }

    val userLogin by viewModel.userLogin.collectAsState()
    LaunchedEffect(userLogin) {
        if (userLogin.isNullOrEmpty()) {
            viewModel.currentUserEmail()
        } else {
            authState.value = when {
                userLogin.isNullOrEmpty() -> AuthState.UNAUTHORISED
                userLogin == ANONYMOUS_USER -> AuthState.AUTHORISED_ANONYMOUSLY
                userLogin?.contains(GOOGLE_USER).isTrue() -> AuthState.AUTHORISED_GOOGLE
                else -> AuthState.AUTHORISED_EMAIL
            }
        }
    }

    val reAuthenticateState = viewModel.reAuthenticateLiveData.collectAsState()
    LaunchedEffect(reAuthenticateState.value) {
        if (reAuthenticateState.value) {
            viewModel.deleteUser()
        }
    }

    val successChangePasswordState = viewModel.successChangePasswordLiveData.collectAsState()
    if (successChangePasswordState.value) {
        updateScreenState(
            appMessage = AppMessage(
                false,
                LocalStringResources.current.SETTINGS_ACCOUNT_CHANGE_PASSWORD_SUCCEED
            )
        )
    }

    val successState = viewModel.successLiveData.collectAsState()
    LaunchedEffect(successState.value) {
        if (successState.value) {
            viewModel.clearData()
            updatedScreenRoute.value = NavigationScreen.LoginScreen.route
        }
    }

    SettingsAccountContent(
        userLogin,
        authState,
        showLogOutDialog,
        showChangePasswordDialog,
        showDeleteGoogleAccountDialog,
        showDeleteEmailAccountDialog,
        updatedScreenRoute
    )

    ChangePasswordDialog(showChangePasswordDialog) { password ->
        viewModel.changePassword(password.first, password.second)
    }

    ConfirmationDialog(
        when (authState.value) {
            AuthState.AUTHORISED_ANONYMOUSLY -> LocalStringResources.current.SETTINGS_ACCOUNT_UNAUTHORISED_LOG_OUT
            else -> LocalStringResources.current.SETTINGS_ACCOUNT_LOG_OUT
        },
        showLogOutDialog
    ) {
        viewModel.signOut()
    }

    ConfirmationDialog(
        LocalStringResources.current.SETTINGS_ACCOUNT_GOOGLE_DELETE,
        showDeleteGoogleAccountDialog
    ) {
        viewModel.googleSignIn()
    }

    DataEditDialog(
        LocalStringResources.current.SETTINGS_ACCOUNT_EMAIL_DELETE,
        placeHolder = LocalStringResources.current.SETTINGS_ACCOUNT_ENTER_CURRENT_PASSWORD,
        remember {
            mutableStateOf(TextFieldValue())
        },
        showDeleteEmailAccountDialog
    ) {
        viewModel.reAuthenticate()
        showDeleteEmailAccountDialog.value = false
    }
}

@Composable
fun SettingsAccountContent(
    userLogin: String?,
    authState: MutableState<AuthState?>,
    showLogOutDialog: MutableState<Boolean>,
    showChangePasswordDialog: MutableState<Boolean>,
    showDeleteGoogleAccountDialog: MutableState<Boolean>,
    showDeleteEmailAccountDialog: MutableState<Boolean>,
    updateScreenRoute: MutableState<String>
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top
    ) {
        AccountCard(authState.value, userLogin) {
            showLogOutDialog.value = true
        }
        if (authState.value == AuthState.AUTHORISED_EMAIL) {
            PrimaryButton(
                text = LocalStringResources.current.SETTINGS_ACCOUNT_CHANGE_PASSWORD_TITLE,
                modifier = Modifier
            ) {
                showChangePasswordDialog.value = true
            }
        }
        if (authState.value == AuthState.AUTHORISED_EMAIL || authState.value == AuthState.AUTHORISED_GOOGLE) {
            SecondaryButton(
                text = LocalStringResources.current.SETTINGS_ACCOUNT_DELETE_TITLE,
                true,
                modifier = Modifier
            ) {
                if (authState.value == AuthState.AUTHORISED_GOOGLE) {
                    showDeleteGoogleAccountDialog.value = true
                } else {
                    showDeleteEmailAccountDialog.value = true
                }
            }
        } else {
            PrimaryButton(
                text = LocalStringResources.current.AUTHORIZATION_SIGNING_UP,
                modifier = Modifier
            ) {
                updateScreenRoute.value = NavigationScreen.SettingsSignUpScreen.route
            }
            EmptyState(
                text = LocalStringResources.current.EMPTY_STATE_ACCOUNT,
                modifier = Modifier
            )
        }
    }
}

@Composable
fun AccountCard(authState: AuthState?, email: String?, onClick: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth(), elevation = CardDefaults.cardElevation(1.dp)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp), verticalAlignment = Alignment.CenterVertically
        ) {
            ShapeableImage(
                modifier = Modifier.size(50.dp),
                drawableRes = when (authState) {
                    AuthState.AUTHORISED_GOOGLE -> Res.drawable.ic_avatar_google
                    AuthState.AUTHORISED_EMAIL -> Res.drawable.ic_avatar_email
                    else -> Res.drawable.ic_avatar_anonymous
                },
                contentDescription = LocalStringResources.current.ACCOUNT_AVATAR
            )
            Column(
                modifier = Modifier.wrapContentSize(),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = if (authState != AuthState.AUTHORISED_ANONYMOUSLY) email.orEmpty() else LocalStringResources.current.SETTINGS_ACCOUNT_UNAUTHORISED,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 8.dp, top = 8.dp)
                )
                LinkButton(
                    text = if (authState != AuthState.AUTHORISED_ANONYMOUSLY) LocalStringResources.current.SETTINGS_ACCOUNT_LOG_OUT_TITLE else LocalStringResources.current.SETTINGS_ACCOUNT_UNAUTHORISED_LOG_OUT_TITLE,
                    modifier = Modifier
                        .wrapContentSize(),
                    onClick = onClick
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
                            text = LocalStringResources.current.SETTINGS_ACCOUNT_CHANGE_PASSWORD_TITLE,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            textAlign = TextAlign.Center,
                        )
                        PasswordTextField(
                            currentPasswordInputValue,
                            LocalStringResources.current.SETTINGS_ACCOUNT_ENTER_CURRENT_PASSWORD
                        )
                        PasswordTextField(
                            newPasswordInputValue,
                            LocalStringResources.current.SETTINGS_ACCOUNT_ENTER_NEW_PASSWORD
                        )
                        SubmitButtons(
                            currentPasswordInputValue.value.text.isNotEmpty() && newPasswordInputValue.value.text.isNotEmpty(),
                            {
                                showDialog.value = false
                            },
                            {
                                showDialog.value = false
                                onConfirmationClick.invoke(
                                    Pair(
                                        currentPasswordInputValue.value.text,
                                        newPasswordInputValue.value.text
                                    )
                                )
                            })
                    }
                }
            )
        }
    }
}