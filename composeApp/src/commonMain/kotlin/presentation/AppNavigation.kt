package presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import resources.LocalStringResources
import com.vnteam.talktoai.presentation.uimodels.screen.ScreenState
import presentation.screens.authorization.onboarding.OnboardingPage
import presentation.screens.chat.ChatContent
import presentation.screens.settings.settings_account.SettingsAccountContent
import presentation.screens.settings.settings_chat.SettingsChatContent
import presentation.screens.settings.settings_feedback.SettingsFeedbackContent
import presentation.screens.settings.settings_language.SettingsLanguageContent
import presentation.screens.settings.settings_privacy_policy.SettingsPrivacyPolicyContent
import presentation.screens.settings.settings_sign_up.SettingsSignUpContent
import presentation.screens.settings.settings_theme.SettingsThemeContent

@Composable
fun AppNavigation(navController: NavHostController, screenState: MutableState<ScreenState>) {
    NavHost(navController = navController, startDestination = NavigationScreen.CHAT_SCREEN) {
        composable(NavigationScreen.ONBOARDING_SCREEN) {
            screenState.value = screenState.value.copy(
                appBarState = screenState.value.appBarState.copy(
                    appBarTitle = NavigationScreen.ONBOARDING_SCREEN,
                    topAppBarActionVisible = false,
                ),
                floatingActionState = screenState.value.floatingActionState.copy(
                    floatingActionButtonVisible = true,
                    floatingActionButtonTitle = "LocalStringResources.current.ADD",
                    floatingActionButtonAction = {
                        navController.navigate("${NavigationScreen.CHAT_SCREEN}-1")
                    }
                )
            )
            OnboardingPage(0) {
                navController.navigate(NavigationScreen.LOGIN_SCREEN)
            }
        }
        composable(NavigationScreen.LOGIN_SCREEN) {
            screenState.value = screenState.value.copy(
                appBarState = screenState.value.appBarState.copy(
                    appBarTitle = NavigationScreen.LOGIN_SCREEN,
                    topAppBarActionVisible = false,
                ),
                floatingActionState = screenState.value.floatingActionState.copy(
                    floatingActionButtonVisible = true,
                    floatingActionButtonTitle = "LocalStringResources.current.ADD",
                    floatingActionButtonAction = {
                        navController.navigate("${NavigationScreen.CHAT_SCREEN}-1")
                    }
                )
            )
            ChatContent(1L, mutableStateOf(false), mutableStateOf(false), ScreenState())
        }
        composable(NavigationScreen.SIGN_UP_SCREEN) {
            screenState.value = screenState.value.copy(
                appBarState = screenState.value.appBarState.copy(
                    appBarTitle = NavigationScreen.SIGN_UP_SCREEN,
                    topAppBarActionVisible = false,
                ),
                floatingActionState = screenState.value.floatingActionState.copy(
                    floatingActionButtonVisible = true,
                    floatingActionButtonTitle = "LocalStringResources.current.ADD",
                    floatingActionButtonAction = {
                        navController.navigate("${NavigationScreen.CHAT_SCREEN}-1")
                    }
                )
            )
            ChatContent(1L, mutableStateOf(false), mutableStateOf(false), ScreenState())
        }
        composable(NavigationScreen.CHAT_SCREEN) {
            screenState.value = screenState.value.copy(
                appBarState = screenState.value.appBarState.copy(
                    appBarTitle = LocalStringResources.current.APP_NAME,
                    topAppBarActionVisible = false,
                ),
                floatingActionState = screenState.value.floatingActionState.copy(
                    floatingActionButtonVisible = true,
                    floatingActionButtonTitle = "LocalStringResources.current.ADD",
                    floatingActionButtonAction = {
                        navController.navigate("${NavigationScreen.CHAT_SCREEN}-1")
                    }
                )
            )
            ChatContent(1L, mutableStateOf(false), mutableStateOf(false), ScreenState())
        }
        composable(NavigationScreen.SETTINGS_CHAT_SCREEN) {
            screenState.value = screenState.value.copy(
                appBarState = screenState.value.appBarState.copy(
                    appBarTitle = NavigationScreen.SETTINGS_CHAT_SCREEN,
                    topAppBarActionVisible = true,
                    topAppBarAction = {
                        navController.navigateUp()
                    }
                ),
                floatingActionState = screenState.value.floatingActionState.copy(
                floatingActionButtonVisible = false
            ))
            SettingsChatContent(screenState.value)
        }
        composable(NavigationScreen.SETTINGS_ACCOUNT_SCREEN) {
            screenState.value = screenState.value.copy(
                appBarState = screenState.value.appBarState.copy(
                    appBarTitle = NavigationScreen.SETTINGS_ACCOUNT_SCREEN,
                    topAppBarActionVisible = true,
                    topAppBarAction = {
                        navController.navigateUp()
                    }
                ),
                floatingActionState = screenState.value.floatingActionState.copy(
                    floatingActionButtonVisible = false
                ))
            SettingsAccountContent(screenState.value)
        }
        composable(NavigationScreen.SETTINGS_LANGUAGE_SCREEN) {
            screenState.value = screenState.value.copy(
                appBarState = screenState.value.appBarState.copy(
                    appBarTitle = NavigationScreen.SETTINGS_LANGUAGE_SCREEN,
                    topAppBarActionVisible = true,
                    topAppBarAction = {
                        navController.navigateUp()
                    }
                ),
                floatingActionState = screenState.value.floatingActionState.copy(
                    floatingActionButtonVisible = false
                ))
            SettingsLanguageContent(screenState.value)
        }
        composable(NavigationScreen.SETTINGS_THEME_SCREEN) {
            screenState.value = screenState.value.copy(
                appBarState = screenState.value.appBarState.copy(
                    appBarTitle = NavigationScreen.SETTINGS_THEME_SCREEN,
                    topAppBarActionVisible = true,
                    topAppBarAction = {
                        navController.navigateUp()
                    }
                ),
                floatingActionState = screenState.value.floatingActionState.copy(
                    floatingActionButtonVisible = false
                ))
            SettingsThemeContent(screenState.value)
        }
        composable(NavigationScreen.SETTINGS_FEEDBACK_SCREEN) {
            screenState.value = screenState.value.copy(
                appBarState = screenState.value.appBarState.copy(
                    appBarTitle = NavigationScreen.SETTINGS_FEEDBACK_SCREEN,
                    topAppBarActionVisible = true,
                    topAppBarAction = {
                        navController.navigateUp()
                    }
                ),
                floatingActionState = screenState.value.floatingActionState.copy(
                    floatingActionButtonVisible = false
                ))
            SettingsFeedbackContent(screenState.value)
        }
        composable(NavigationScreen.SETTINGS_PRIVACY_POLICY_SCREEN) {
            screenState.value = screenState.value.copy(
                appBarState = screenState.value.appBarState.copy(
                    appBarTitle = NavigationScreen.SETTINGS_PRIVACY_POLICY_SCREEN,
                    topAppBarActionVisible = true,
                    topAppBarAction = {
                        navController.navigateUp()
                    }
                ),
                floatingActionState = screenState.value.floatingActionState.copy(
                    floatingActionButtonVisible = false
                ))
            SettingsPrivacyPolicyContent(screenState.value)
        }
        composable(NavigationScreen.SETTINGS_SIGN_UP_SCREEN) {
            screenState.value = screenState.value.copy(
                appBarState = screenState.value.appBarState.copy(
                    appBarTitle = NavigationScreen.SETTINGS_SIGN_UP_SCREEN,
                    topAppBarActionVisible = true,
                    topAppBarAction = {
                        navController.navigateUp()
                    }
                ),
                floatingActionState = screenState.value.floatingActionState.copy(
                    floatingActionButtonVisible = false
                ))
            SettingsSignUpContent(screenState.value)
        }
    }
}