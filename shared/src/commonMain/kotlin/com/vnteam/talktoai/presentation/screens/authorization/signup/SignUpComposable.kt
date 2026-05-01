package com.vnteam.talktoai.presentation.screens.authorization.signup

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
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
import com.vnteam.talktoai.domain.models.RemoteUser
import com.vnteam.talktoai.presentation.ui.NavigationScreen
import com.vnteam.talktoai.presentation.ui.components.ConfirmationDialog
import com.vnteam.talktoai.presentation.uimodels.screen.AppMessage
import com.vnteam.talktoai.presentation.ui.components.LinkButton
import com.vnteam.talktoai.presentation.ui.components.PasswordTextField
import com.vnteam.talktoai.presentation.ui.components.PrimaryButton
import com.vnteam.talktoai.presentation.ui.components.PrimaryTextField
import com.vnteam.talktoai.presentation.ui.resources.LocalStringResources
import com.vnteam.talktoai.presentation.viewmodels.authorisation.SignUpViewModel
import org.koin.compose.viewmodel.koinViewModel
import com.vnteam.talktoai.presentation.updateScreenState

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

    val innerProgress = viewModel.innerProgress.collectAsState()
    if (innerProgress.value) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    }

    val exceptionMessage = viewModel.exceptionMessage.collectAsState()
    val progress = viewModel.progressVisibilityState.collectAsState()
    updateScreenState(
        isProgressVisible = progress.value,
        appMessage = if (exceptionMessage.value.isNotEmpty()) AppMessage(true, exceptionMessage.value) else null
    )

    val stringResources = LocalStringResources.current
    val signUpUiState by viewModel.uiState.collectAsState()
    LaunchedEffect(signUpUiState) {
        signUpUiState.accountExist?.let {
            showAccountExistDialog.value = true
            signUpUiState.accountExist = null
        }
        signUpUiState.createEmailAccount?.let {
            viewModel.createUserWithEmailAndPassword(
                emailInputValue.value.text.trim(),
                passwordInputValue.value.text
            )
        }
        signUpUiState.successSignUp?.let {
            viewModel.insertRemoteUser(
                RemoteUser(),
                stringResources.WELCOME_CHAT_NAME,
                stringResources.WELCOME_MESSAGE
            )
        }
    }

    val privacyPolicyAccepted = remember { mutableStateOf(false) }

    SignUpContent(
        viewModel = viewModel,
        emailInputValue = emailInputValue,
        passwordInputValue = passwordInputValue,
        privacyPolicyAccepted = privacyPolicyAccepted,
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
    privacyPolicyAccepted: MutableState<Boolean>,
    updatedScreenRoute: MutableState<String>,
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
            emailInputValue.value.text.isNotEmpty() && passwordInputValue.value.text.isNotEmpty() && privacyPolicyAccepted.value,
            modifier = Modifier
        ) {
            viewModel.createUserWithEmailAndPassword(
                emailInputValue.value.text.trim(),
                passwordInputValue.value.text
            )
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(start = 4.dp)
        ) {
            Checkbox(
                checked = privacyPolicyAccepted.value,
                onCheckedChange = { privacyPolicyAccepted.value = it }
            )
            Text(text = LocalStringResources.current.AUTHORIZATION_PRIVACY_POLICY_AGREE)
            LinkButton(
                text = LocalStringResources.current.AUTHORIZATION_PRIVACY_POLICY_LINK,
                modifier = Modifier.wrapContentSize()
            ) {
                updatedScreenRoute.value = NavigationScreen.SettingsPrivacyPolicyScreen.route
            }
        }
    }
}
