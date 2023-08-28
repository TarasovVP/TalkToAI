package com.vnstudio.talktoai.presentation.settings.settings_account

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.vnstudio.talktoai.R
import com.vnstudio.talktoai.domain.sealed_classes.NavigationScreen
import com.vnstudio.talktoai.presentation.base.EmptyState
import com.vnstudio.talktoai.presentation.base.ShapeableImage
import com.vnstudio.talktoai.presentation.components.*

@Composable
fun SettingsAccountScreen(onNextScreen: (String) -> Unit) {

    val viewModel: SettingsAccountViewModel = hiltViewModel()
    val showLogOutDialog = remember { mutableStateOf(false) }
    val showDeleteAccountDialog = remember { mutableStateOf(false) }

    val successState = viewModel.successLiveData.observeAsState()
    LaunchedEffect(successState.value) {
        successState.value?.let {
            viewModel.clearDataByKeys(listOf())
            viewModel.clearDataBase()
            onNextScreen.invoke(NavigationScreen.LoginScreen().route)
        }
    }

    val accountAvatar = when {
        viewModel.isGoogleAuthUser() -> R.drawable.ic_avatar_google
        viewModel.isAuthorisedUser() -> R.drawable.ic_avatar_email
        else -> R.drawable.ic_avatar_anonymous
    }
    val accountName = if (viewModel.isAuthorisedUser()) viewModel.currentUserEmail() else "Неавторизованный"
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top
    ) {
        AccountCard(accountAvatar, accountName) {
            showLogOutDialog.value = true
        }
        if (viewModel.isAuthorisedUser() && viewModel.isGoogleAuthUser().not()) {
            PrimaryButton(text = "Сменить пароль", modifier = Modifier) {

            }
        }
        if (viewModel.isAuthorisedUser()) {
            SecondaryButton(text = "Удалить аккаунт", true, modifier = Modifier) {

            }
        } else {
            PrimaryButton(text = "Создать аккаунт", modifier = Modifier) {
                onNextScreen.invoke(NavigationScreen.SignUpScreen().route)
            }
            EmptyState(
                text = "Ваши данные хранятся локально в рамках одной сессии и будут потеряны при выходе из этого аккаунта, очистке кеша или переустановке приложения. Для подключения удаленного хранения данных и доступа к нему с любого устройства, зарегистрируйтесь.",
                modifier = Modifier
            )
        }
    }

    ConfirmationDialog(when {
        viewModel.isAuthorisedUser() -> "Вы действительно хотите разлогиниться?"
        else -> "При выходе из неавторизованого аккаунта вы потеряете все созданные данные. Для сохранения зарегистрируйтесь. Все равно выйти?"
    }, showLogOutDialog, onDismiss = {
        showLogOutDialog.value = false
    }) {
        viewModel.signOut()
        showLogOutDialog.value = false
    }

    ConfirmationDialog("Пользователя с таким Email не существует. Сначала необходимо создать аккаунт. Перейти на экран регистрации?", showDeleteAccountDialog, onDismiss = {
        showDeleteAccountDialog.value = false
    }) {
        showDeleteAccountDialog.value = false
    }
}

@Composable
fun AccountCard(accountAvatar: Int, accountName: String, onClick: () -> Unit) {

    Card(modifier = Modifier.fillMaxWidth(), elevation = 1.dp) {
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp), verticalAlignment = Alignment.CenterVertically ) {
            ShapeableImage(modifier = Modifier.size(50.dp), drawableResId = accountAvatar, contentDescription = "Account avatar")
            Column(
                modifier = Modifier.wrapContentSize(),
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = accountName, modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 8.dp, top = 8.dp))
                LinkButton(text = "Выйти", modifier = Modifier
                    .wrapContentSize(), onClick = onClick)
            }
        }
    }
}