package presentation.screens.main

import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.runtime.Composable
import com.vnteam.talktoai.CommonExtensions.isTrue
import com.vnteam.talktoai.presentation.ui.NavigationScreen
import com.vnteam.talktoai.presentation.uimodels.screen.ScreenState
import presentation.DrawerHeader
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