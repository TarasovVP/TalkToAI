package com.vnteam.talktoai.presentation.screens.settings.settings_login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import com.vnteam.talktoai.presentation.screens.settings.settings_sign_up.TransferDataCard
import com.vnteam.talktoai.presentation.ui.components.GoogleButton
import com.vnteam.talktoai.presentation.ui.components.OrDivider
import com.vnteam.talktoai.presentation.ui.components.PasswordTextField
import com.vnteam.talktoai.presentation.ui.components.PrimaryButton
import com.vnteam.talktoai.presentation.ui.components.PrimaryTextField
import com.vnteam.talktoai.presentation.ui.resources.LocalStringResources
import com.vnteam.talktoai.presentation.viewmodels.settings.SettingsLoginViewModel
import org.koin.compose.viewmodel.koinViewModel
import com.vnteam.talktoai.presentation.updateScreenState

@Composable
fun SettingsLoginScreen() {
    val viewModel: SettingsLoginViewModel = koinViewModel()
    updateScreenState(viewModel.progressVisibilityState.collectAsState().value)
    val emailInputValue = remember { mutableStateOf(TextFieldValue()) }
    val passwordInputValue = remember { mutableStateOf(TextFieldValue()) }
    val transferDataState = remember { mutableStateOf(true) }

    val settingsLoginUIStateState by viewModel.uiState.collectAsState()
    
    LaunchedEffect(settingsLoginUIStateState) {
        settingsLoginUIStateState.successAuthorisation?.let { isExistUser ->
            if (transferDataState.value) {
                viewModel.createRemoteUser(isExistUser)
            }
        }
        settingsLoginUIStateState.remoteUser?.let { userState ->
            if (userState.first) {
                userState.second?.let { viewModel.updateRemoteCurrentUser(it) }
            } else {
                userState.second?.let { viewModel.insertRemoteCurrentUser(it) }
            }
        }
        settingsLoginUIStateState.successRemoteUser?.let { successRemoteUser ->
            if (successRemoteUser) {
                // Navigate on success if required, handled by observer or upper logic
            }
        }
    }

    SettingsLoginContent(
        viewModel,
        emailInputValue,
        passwordInputValue,
        transferDataState
    )
}

@Composable
fun SettingsLoginContent(
    viewModel: SettingsLoginViewModel,
    emailInputValue: MutableState<TextFieldValue>,
    passwordInputValue: MutableState<TextFieldValue>,
    transferDataState: MutableState<Boolean>,
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
            LocalStringResources.current.AUTHORIZATION_ENTER,
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
            text = LocalStringResources.current.AUTHORIZATION_ENTER,
            emailInputValue.value.text.isNotEmpty() && passwordInputValue.value.text.isNotEmpty(),
            modifier = Modifier
        ) {
            viewModel.signInWithEmailAndPassword(
                emailInputValue.value.text.trim(),
                passwordInputValue.value.text
            )
        }
        TransferDataCard(transferDataState)
    }
}
