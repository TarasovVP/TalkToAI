package presentation.screens.authorization.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
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
import com.vnteam.talktoai.CommonExtensions.EMPTY
import com.vnteam.talktoai.CommonExtensions.isTrue
import com.vnteam.talktoai.Constants.DEFAULT_CHAT_ID
import com.vnteam.talktoai.presentation.ui.NavigationScreen
import com.vnteam.talktoai.presentation.ui.components.ConfirmationDialog
import com.vnteam.talktoai.presentation.ui.components.DataEditDialog
import com.vnteam.talktoai.presentation.ui.components.GoogleButton
import com.vnteam.talktoai.presentation.ui.components.LinkButton
import com.vnteam.talktoai.presentation.ui.components.OrDivider
import com.vnteam.talktoai.presentation.ui.components.PasswordTextField
import com.vnteam.talktoai.presentation.ui.components.PrimaryButton
import com.vnteam.talktoai.presentation.ui.components.PrimaryTextField
import com.vnteam.talktoai.presentation.ui.components.SecondaryButton
import com.vnteam.talktoai.presentation.ui.resources.LocalStringResources
import com.vnteam.talktoai.presentation.uimodels.screen.AppMessage
import com.vnteam.talktoai.presentation.viewmodels.LoginViewModel
import org.koin.compose.viewmodel.koinViewModel
import presentation.updateScreenState

@Composable
fun LoginScreen() {

    val viewModel = koinViewModel<LoginViewModel>()

    val emailInputValue = remember { mutableStateOf(TextFieldValue()) }
    val passwordInputValue = remember { mutableStateOf(TextFieldValue()) }
    val showForgotPasswordDialog = remember { mutableStateOf(false) }
    val showAccountExistDialog = remember { mutableStateOf(false) }
    val showUnauthorizedEnterDialog = remember { mutableStateOf(false) }

    val updatedScreenRoute = remember { mutableStateOf(String.EMPTY) }
    if (updatedScreenRoute.value.isNotEmpty()) {
        updateScreenState(screenRoute = updatedScreenRoute.value)
        updatedScreenRoute.value = String.EMPTY
    }

    val exceptionMessage = viewModel.exceptionMessage.collectAsState()
    val progress = viewModel.progressVisibilityState.collectAsState()
    println("appTAG LoginComposable exceptionMessage: ${exceptionMessage.value} progress: ${progress.value}")
    updateScreenState(
        isProgressVisible = progress.value,
        appMessage = if (exceptionMessage.value.isNotEmpty()) AppMessage(
            true,
            exceptionMessage.value
        ) else null
    )

    val accountExistState = viewModel.accountExistLiveData.collectAsState()
    LaunchedEffect(accountExistState.value) {
        if (accountExistState.value.isTrue()) {
            viewModel.googleSignOut()
            showAccountExistDialog.value = true
            viewModel.accountExistLiveData.value = false
        }
    }
    val isEmailAccountExistState = viewModel.isEmailAccountExistLiveData.collectAsState()
    LaunchedEffect(isEmailAccountExistState.value) {
        if (isEmailAccountExistState.value.isTrue()) {
            viewModel.signInWithEmailAndPassword(
                emailInputValue.value.text.trim(),
                passwordInputValue.value.text
            )
        }
    }
    val isGoogleAccountExistState = viewModel.isGoogleAccountExistLiveData.collectAsState()
    LaunchedEffect(isGoogleAccountExistState.value) {
        isGoogleAccountExistState.value.takeIf { it.isNotEmpty() }?.let { idToken ->
            viewModel.signInAuthWithGoogle(idToken)
        }
    }
    val resetPasswordText = LocalStringResources.current.AUTHORIZATION_PASSWORD_RESET_SUCCESS
    val successPasswordResetState = viewModel.successPasswordResetLiveData.collectAsState()
    if (successPasswordResetState.value.isTrue()) {
        updateScreenState(appMessage = AppMessage(false, resetPasswordText))
    }
    val successSignInState = viewModel.successSignInLiveData.collectAsState()
    if (successSignInState.value.isTrue()) {
        updateScreenState(screenRoute = "${NavigationScreen.CHAT_DESTINATION}/$DEFAULT_CHAT_ID")
    }

    LoginContent(
        viewModel,
        emailInputValue,
        passwordInputValue,
        showForgotPasswordDialog,
        showAccountExistDialog,
        showUnauthorizedEnterDialog,
        updatedScreenRoute
    )

    DataEditDialog(
        LocalStringResources.current.AUTHORIZATION_FORGOT_PASSWORD_TITLE,
        LocalStringResources.current.AUTHORIZATION_EMAIL,
        emailInputValue,
        showForgotPasswordDialog
    ) { email ->
        viewModel.sendPasswordResetEmail(email)
        showForgotPasswordDialog.value = false
    }

    ConfirmationDialog(
        LocalStringResources.current.AUTHORIZATION_ACCOUNT_NOT_EXIST,
        showAccountExistDialog
    ) {
        updatedScreenRoute.value = NavigationScreen.SignUpScreen.route
    }

    ConfirmationDialog(
        LocalStringResources.current.AUTHORIZATION_UNAUTHORIZED_ENTER,
        showUnauthorizedEnterDialog
    ) {
        viewModel.signInAnonymously()
    }
}

@Composable
fun LoginContent(
    viewModel: LoginViewModel,
    emailInputValue: MutableState<TextFieldValue>,
    passwordInputValue: MutableState<TextFieldValue>,
    showForgotPasswordDialog: MutableState<Boolean>,
    showAccountExistDialog: MutableState<Boolean>,
    showUnauthorizedEnterDialog: MutableState<Boolean>,
    updatedScreenRoute: MutableState<String>
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top
    ) {
        Spacer(modifier = Modifier.height(40.dp))
        Text(
            text = LocalStringResources.current.AUTHORIZATION_ENTRANCE, modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp), textAlign = TextAlign.Center
        )
        Text(
            text = LocalStringResources.current.AUTHORIZATION_WITH_GOOGLE_ACCOUNT,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )
        GoogleButton(
            LocalStringResources.current.AUTHORIZATION_ENTRANCE,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(16.dp)
        ) {
            viewModel.googleSignIn()
        }
        OrDivider(modifier = Modifier)
        PrimaryTextField(LocalStringResources.current.AUTHORIZATION_EMAIL, emailInputValue)
        PasswordTextField(passwordInputValue, LocalStringResources.current.AUTHORIZATION_PASSWORD)
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            LinkButton(
                text = LocalStringResources.current.AUTHORIZATION_SIGN_UP,
                textAlign = TextAlign.Start,
                modifier = Modifier
                    .wrapContentSize()
                    .padding(start = 16.dp)
            ) {
                updatedScreenRoute.value = NavigationScreen.SignUpScreen.route
            }
            Spacer(modifier = Modifier.weight(0.5f))
            LinkButton(
                text = LocalStringResources.current.AUTHORIZATION_FORGOT_PASSWORD,
                textAlign = TextAlign.End,
                modifier = Modifier
                    .wrapContentSize()
                    .padding(end = 16.dp)
            ) {
                showForgotPasswordDialog.value = true
            }
        }
        PrimaryButton(
            text = LocalStringResources.current.AUTHORIZATION_ENTER,
            emailInputValue.value.text.isNotEmpty() && passwordInputValue.value.text.isNotEmpty(),
            modifier = Modifier
        ) {
            viewModel.fetchSignInMethodsForEmail(emailInputValue.value.text.trim())
        }
        OrDivider()
        SecondaryButton(
            text = LocalStringResources.current.AUTHORIZATION_CONTINUE_WITHOUT_ACCOUNT,
            false,
            modifier = Modifier
        ) {
            showUnauthorizedEnterDialog.value = true
        }
    }
}