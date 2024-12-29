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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.vnteam.talktoai.Constants.DEFAULT_CHAT_ID
import com.vnteam.talktoai.Constants.DESTINATION_CHAT_SCREEN
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
import com.vnteam.talktoai.presentation.uimodels.screen.ScreenState
import com.vnteam.talktoai.presentation.viewmodels.SignUpViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SignUpContent(
    screenState: ScreenState,
    onScreenStateUpdate: (ScreenState?) -> Unit
) {
    val viewModel = koinViewModel<SignUpViewModel>()
    val emailInputValue = remember { mutableStateOf(TextFieldValue()) }
    val passwordInputValue = remember { mutableStateOf(TextFieldValue()) }
    val showAccountExistDialog = remember { mutableStateOf(false) }

    val signUpUiState by viewModel.uiState.collectAsState()

    LaunchedEffect(signUpUiState) {
        signUpUiState.accountExist?.let {
            //viewModel.googleSignInClient.signOut()
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
            onScreenStateUpdate.invoke(screenState.copy(currentScreenRoute = "${DESTINATION_CHAT_SCREEN}/${DEFAULT_CHAT_ID}"))
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
            //launcher.launch(viewModel.googleSignInClient.signInIntent)
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
                onScreenStateUpdate.invoke(screenState.copy(currentScreenRoute = NavigationScreen.LoginScreen.route))
            }
        }
        PrimaryButton(
            text = LocalStringResources.current.AUTHORIZATION_SIGNING_UP,
            emailInputValue.value.text.isNotEmpty() && passwordInputValue.value.text.isNotEmpty(),
            modifier = Modifier
        ) {
            viewModel.fetchSignInMethodsForEmail(emailInputValue.value.text.trim())
        }
    }
    ConfirmationDialog(
        LocalStringResources.current.AUTHORIZATION_ACCOUNT_EXIST,
        showAccountExistDialog,
        onDismiss = {
            showAccountExistDialog.value = false
        }) {
        showAccountExistDialog.value = false
        onScreenStateUpdate.invoke(screenState.copy(currentScreenRoute = NavigationScreen.LoginScreen.route))
    }
}