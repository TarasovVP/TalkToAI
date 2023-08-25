package com.vnstudio.talktoai.presentation.settings.settings_list

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.vnstudio.talktoai.R

@Composable
fun SettingsListScreen(onNextScreen: (String) -> Unit) {

    val viewModel: SettingsListViewModel = hiltViewModel()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        SettingsItem(
            "Hастройки чата",
            R.drawable.ic_settings_chat
        ) {
            onNextScreen.invoke("destination_settings_chat_screen")
        }
        SettingsItem(
            "Ваш аккаунт",
            R.drawable.ic_settings_account
        ) {
            onNextScreen.invoke("destination_settings_account_screen")
        }
        SettingsItem(
            "Язык",
            R.drawable.ic_settings_language
        ) {
            onNextScreen.invoke("destination_settings_language_screen")
        }
        SettingsItem(
            "Выбор темы",
            R.drawable.ic_settings_theme
        ) {
            onNextScreen.invoke("destination_settings_theme_screen")
        }
        SettingsItem(
            "Написать разработчику",
            R.drawable.ic_settings_feedback
        ) {

        }
        SettingsItem(
            "Политика конфиденциальности",
            R.drawable.ic_settings_privacy
        ) {
            onNextScreen.invoke("destination_settings_privacy_policy_screen")
        }
    }

}

@Composable
fun SettingsItem(title: String, icon: Int, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp)
            .clickable {
                onClick.invoke()
            },
        elevation = 1.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Image(imageVector = ImageVector.vectorResource(id = icon), contentDescription = title,
                modifier = Modifier
                .padding(end = 8.dp))
            Text(text = title)
        }
    }
}