package presentation.screens.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vnteam.talktoai.CommonExtensions.isTrue
import com.vnteam.talktoai.Res
import com.vnteam.talktoai.avatar_ai
import com.vnteam.talktoai.ic_chat
import com.vnteam.talktoai.ic_settings
import com.vnteam.talktoai.presentation.ui.NavigationScreen
import com.vnteam.talktoai.presentation.ui.resources.LocalStringResources
import com.vnteam.talktoai.presentation.ui.theme.Neutral50
import com.vnteam.talktoai.presentation.ui.theme.Primary700
import com.vnteam.talktoai.presentation.uimodels.screen.ScreenState
import org.jetbrains.compose.resources.painterResource
import presentation.screens.chat_list.ChatListComposable
import presentation.screens.settings.settings_list.SettingsListComposable

@Composable
fun DrawerContent(screenState: ScreenState?, onScreenStateUpdate: (ScreenState?, Boolean) -> Unit) {
    ModalDrawerSheet {
        DrawerHeader(screenState?.isSettingsScreen.isTrue()) { settingsDrawerModeState ->
            onScreenStateUpdate(
                screenState?.copy(
                    currentScreenRoute = if (settingsDrawerModeState) {
                        NavigationScreen.SettingsChatScreen.route
                    } else {
                        NavigationScreen.ChatScreen.route
                    }
                ), false
            )
        }
        if (screenState?.isSettingsScreen.isTrue()) {
            SettingsListComposable(screenState?.currentScreenRoute) { route ->
                onScreenStateUpdate(screenState?.copy(currentScreenRoute = route), true)
            }
        } else {
            ChatListComposable(
                onChatClick = { chatId ->
                    onScreenStateUpdate(
                        screenState?.copy(
                            currentScreenRoute = "${NavigationScreen.ChatScreen.route}/$chatId"
                        ),
                        false
                    )
                }
            )
        }
    }
}

@Composable
fun DrawerHeader(isSettingsDrawerMode: Boolean, onDrawerModeClick: (Boolean) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().background(color = Primary700),
        verticalAlignment = Alignment.Top
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Image(
                painter = painterResource(if (isSettingsDrawerMode) Res.drawable.ic_settings else Res.drawable.avatar_ai),
                contentDescription = LocalStringResources.current.SETTINGS,
                modifier = Modifier.padding(start = 16.dp, top = 16.dp).size(60.dp)
            )
            Text(
                text = if (isSettingsDrawerMode) LocalStringResources.current.SETTINGS else LocalStringResources.current.APP_NAME,
                fontSize = 16.sp,
                color = Neutral50,
                modifier = Modifier.padding(start = 16.dp, bottom = 16.dp, top = 8.dp)
            )
        }
        IconButton(onClick = {
            onDrawerModeClick.invoke(isSettingsDrawerMode.not())
        }) {
            Image(
                painter = painterResource(if (isSettingsDrawerMode) Res.drawable.ic_chat else Res.drawable.ic_settings),
                contentDescription = LocalStringResources.current.NAVIGATION_ICON,
                modifier = Modifier.padding(end = 16.dp, top = 16.dp).size(24.dp)
            )
        }
    }
}