package presentation.screens.authorization.signup

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.vnteam.talktoai.CommonExtensions.EMPTY
import com.vnteam.talktoai.Constants.DEFAULT_CHAT_ID
import com.vnteam.talktoai.domain.models.RemoteUser
import com.vnteam.talktoai.presentation.ui.NavigationScreen
import com.vnteam.talktoai.presentation.ui.components.ConfirmationDialog
import com.vnteam.talktoai.presentation.ui.components.GoogleButton
import com.vnteam.talktoai.presentation.ui.components.LinkButton
import com.vnteam.talktoai.presentation.ui.components.OrDivider
import com.vnteam.talktoai.presentation.ui.components.PasswordTextField
import com.vnteam.talktoai.presentation.ui.components.PrimaryButton
import com.vnteam.talktoai.presentation.ui.components.PrimaryTextField
import com.vnteam.talktoai.presentation.ui.resources.LocalStringResources
import com.vnteam.talktoai.presentation.viewmodels.authorisation.SignUpViewModel
import org.koin.compose.viewmodel.koinViewModel
import presentation.updateScreenState

@Composable
fun SignUpScreen() {
    val viewModel = koinViewModel<SignUpViewModel>()

    val emailInputValue = remember { mutableStateOf(TextFieldValue()) }
    val passwordInputValue = remember { mutableStateOf(TextFieldValue()) }
    val showAccountExistDialog = remember { mutableStateOf(false) }

    val updatedScreenRoute = remember { mutableStateOf(String.EMPTY) }
    if (updatedScreenRoute.value.isNotEmpty()) {
        updateScreenState(screenRoute = updatedScreenRoute.value)
        updatedScreenRoute.value = String.EMPTY
    }

    val signUpUiState by viewModel.uiState.collectAsState()
    LaunchedEffect(signUpUiState) {
        signUpUiState.accountExist?.let {
            viewModel.googleSignOut()
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
            updatedScreenRoute.value = "${NavigationScreen.CHAT_DESTINATION}/${DEFAULT_CHAT_ID}"
        }
    }

    SignUpContent(
        viewModel = viewModel,
        emailInputValue = emailInputValue,
        passwordInputValue = passwordInputValue,
        updatedScreenRoute = updatedScreenRoute
    )

    ConfirmationDialog(
        LocalStringResources.current.AUTHORIZATION_ACCOUNT_EXIST,
        showAccountExistDialog
    ) {
        updatedScreenRoute.value = NavigationScreen.LoginScreen.route
    }
}

@Composable
fun SignUpContent(
    viewModel: SignUpViewModel,
    emailInputValue: MutableState<TextFieldValue>,
    passwordInputValue: MutableState<TextFieldValue>,
    updatedScreenRoute: MutableState<String>
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top
    ) {
        Spacer(modifier = Modifier.height(160.dp))
        Text(
            text = LocalStringResources.current.AUTHORIZATION_SIGN_UP, modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp), textAlign = TextAlign.Center
        )
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
            viewModel.googleSignIn()
        }
        OrDivider(modifier = Modifier)
        PrimaryTextField(
            LocalStringResources.current.AUTHORIZATION_EMAIL, emailInputValue
        )
        PasswordTextField(passwordInputValue, LocalStringResources.current.AUTHORIZATION_PASSWORD)
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = LocalStringResources.current.AUTHORIZATION_ENTRANCE_TITLE,
                modifier = Modifier.padding(start = 16.dp)
            )
            LinkButton(
                text = LocalStringResources.current.AUTHORIZATION_ENTRANCE,
                modifier = Modifier.wrapContentSize()
            ) {
                updatedScreenRoute.value = NavigationScreen.LoginScreen.route
            }
        }
        PrimaryButton(
            text = LocalStringResources.current.AUTHORIZATION_SIGNING_UP,
            emailInputValue.value.text.isNotEmpty() && passwordInputValue.value.text.isNotEmpty(),
            modifier = Modifier
        ) {
            println("AuthTAG SignUpContent email: ${emailInputValue.value.text.trim()} password: ${passwordInputValue.value.text}")
            viewModel.createUserWithEmailAndPassword(
                emailInputValue.value.text.trim(),
                passwordInputValue.value.text
            )
            viewModel.fetchProvidersForEmailUseCase(emailInputValue.value.text.trim())
        }
    }
}