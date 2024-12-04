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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.vnteam.talktoai.Constants.REVIEW_VOTE
import com.vnteam.talktoai.Res
import com.vnteam.talktoai.domain.enums.AuthState
import com.vnteam.talktoai.domain.models.InfoMessage
import com.vnteam.talktoai.ic_avatar_anonymous
import com.vnteam.talktoai.ic_avatar_email
import com.vnteam.talktoai.ic_avatar_google
import com.vnteam.talktoai.presentation.ui.components.ConfirmationDialog
import com.vnteam.talktoai.presentation.ui.components.DataEditDialog
import com.vnteam.talktoai.presentation.ui.components.EmptyState
import com.vnteam.talktoai.presentation.ui.components.ExceptionMessageHandler
import com.vnteam.talktoai.presentation.ui.components.LinkButton
import com.vnteam.talktoai.presentation.ui.components.PasswordTextField
import com.vnteam.talktoai.presentation.ui.components.PrimaryButton
import com.vnteam.talktoai.presentation.ui.components.ProgressVisibilityHandler
import com.vnteam.talktoai.presentation.ui.components.SecondaryButton
import com.vnteam.talktoai.presentation.ui.components.ShapeableImage
import com.vnteam.talktoai.presentation.ui.components.SubmitButtons
import com.vnteam.talktoai.presentation.ui.resources.LocalStringResources
import com.vnteam.talktoai.presentation.ui.theme.Primary500
import com.vnteam.talktoai.presentation.uimodels.screen.ScreenState
import com.vnteam.talktoai.presentation.viewmodels.SettingsAccountViewModel
import org.koin.compose.viewmodel.koinViewModel
import presentation.NavigationScreen

@Composable
fun SettingsAccountContent(
    screenState: ScreenState
) {

    val viewModel: SettingsAccountViewModel = koinViewModel()
    val authState = remember { mutableStateOf<AuthState?>(null) }
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

    val successChangePasswordMessage =
        InfoMessage(LocalStringResources.current.SETTINGS_ACCOUNT_CHANGE_PASSWORD_SUCCEED)
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

    /*val launcher =
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
        }*/

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
                screenState.currentScreenState.value = NavigationScreen.SettingsSignUpScreen().route
            }
            EmptyState(
                text = LocalStringResources.current.EMPTY_STATE_ACCOUNT,
                modifier = Modifier
            )
        }
    }

    ChangePasswordDialog(showChangePasswordDialog) { password ->
        viewModel.changePassword(password.first, password.second)
    }

    ConfirmationDialog(
        when (authState.value) {
            AuthState.AUTHORISED_ANONYMOUSLY -> LocalStringResources.current.SETTINGS_ACCOUNT_UNAUTHORISED_LOG_OUT
            else -> LocalStringResources.current.SETTINGS_ACCOUNT_LOG_OUT
        }, showLogOutDialog, onDismiss = {
            showLogOutDialog.value = false
        }) {
        viewModel.signOut()
        showLogOutDialog.value = false
    }

    ConfirmationDialog(
        LocalStringResources.current.SETTINGS_ACCOUNT_GOOGLE_DELETE,
        showDeleteGoogleAccountDialog,
        onDismiss = {
            showDeleteGoogleAccountDialog.value = false
        }) {
        //launcher.launch(viewModel.googleSignInClient.signInIntent)
        showDeleteGoogleAccountDialog.value = false
    }

    DataEditDialog(
        LocalStringResources.current.SETTINGS_ACCOUNT_EMAIL_DELETE,
        placeHolder = LocalStringResources.current.SETTINGS_ACCOUNT_ENTER_CURRENT_PASSWORD,
        remember {
            mutableStateOf(TextFieldValue())
        },
        showDeleteEmailAccountDialog,
        onDismiss = {
            showDeleteEmailAccountDialog.value = false
        }) { password ->
        /*val authCredential = EmailAuthProvider.getCredential(viewModel.currentUserEmail(), password)
        viewModel.reAuthenticate(authCredential)*/
        showDeleteEmailAccountDialog.value = false
    }

    ExceptionMessageHandler(screenState.infoMessageState, viewModel.exceptionLiveData)
    ProgressVisibilityHandler(
        mutableStateOf(screenState.isProgressVisible),
        viewModel.progressVisibilityLiveData
    )
}

@Composable
fun AccountCard(authState: AuthState?, email: String, onClick: () -> Unit) {
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
                    text = if (authState != AuthState.AUTHORISED_ANONYMOUSLY) email else LocalStringResources.current.SETTINGS_ACCOUNT_UNAUTHORISED,
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