package presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.vnteam.talktoai.presentation.ui.resources.LocalStringResources
import com.vnteam.talktoai.presentation.uimodels.screen.ScreenState
import presentation.screens.authorization.login.LoginContent
import presentation.screens.authorization.onboarding.OnboardingContent
import presentation.screens.authorization.signup.SignUpContent
import presentation.screens.chat.ChatContent
import presentation.screens.settings.settings_account.SettingsAccountContent
import presentation.screens.settings.settings_chat.SettingsChatContent
import presentation.screens.settings.settings_feedback.SettingsFeedbackContent
import presentation.screens.settings.settings_language.SettingsLanguageContent
import presentation.screens.settings.settings_privacy_policy.SettingsPrivacyPolicyContent
import presentation.screens.settings.settings_sign_up.SettingsSignUpContent
import presentation.screens.settings.settings_theme.SettingsThemeContent

@Composable
fun AppNavigation(navController: NavHostController, screenState: MutableState<ScreenState?>) {
    val startDestination = when {
        screenState.value?.isOnboardingSeen == false -> NavigationScreen.ONBOARDING_SCREEN
        screenState.value?.loggedInUser == false -> NavigationScreen.LOGIN_SCREEN
        else -> NavigationScreen.CHAT_SCREEN
    }
    NavHost(navController = navController, startDestination = startDestination) {
        composable(NavigationScreen.ONBOARDING_SCREEN) {
            screenState.value = screenState.value?.copy(
                appBarState = screenState.value?.appBarState?.copy(
                    appBarTitle = NavigationScreen.ONBOARDING_SCREEN,
                    topAppBarActionVisible = false,
                ),
                floatingActionState = screenState.value?.floatingActionState?.copy(
                    floatingActionButtonVisible = true,
                    floatingActionButtonTitle = "LocalStringResources.current.ADD",
                    floatingActionButtonAction = {
                        navController.navigate("${NavigationScreen.CHAT_SCREEN}-1")
                    }
                )
            )
            screenState.value?.let { it1 -> OnboardingContent(it1) }
        }
        composable(NavigationScreen.LOGIN_SCREEN) {
            screenState.value = screenState.value?.copy(
                appBarState = screenState.value?.appBarState?.copy(
                    appBarTitle = NavigationScreen.LOGIN_SCREEN,
                    topAppBarActionVisible = false,
                ),
                floatingActionState = screenState.value?.floatingActionState?.copy(
                    floatingActionButtonVisible = true,
                    floatingActionButtonTitle = "LocalStringResources.current.ADD",
                    floatingActionButtonAction = {
                        navController.navigate("${NavigationScreen.CHAT_SCREEN}-1")
                    }
                )
            )
            screenState.value?.let { it1 -> LoginContent(it1) }
        }
        composable(NavigationScreen.SIGN_UP_SCREEN) {
            screenState.value = screenState.value?.copy(
                appBarState = screenState.value?.appBarState?.copy(
                    appBarTitle = NavigationScreen.SIGN_UP_SCREEN,
                    topAppBarActionVisible = false,
                ),
                floatingActionState = screenState.value?.floatingActionState?.copy(
                    floatingActionButtonVisible = true,
                    floatingActionButtonTitle = "LocalStringResources.current.ADD",
                    floatingActionButtonAction = {
                        navController.navigate("${NavigationScreen.CHAT_SCREEN}-1")
                    }
                )
            )
            screenState.value?.let { it1 -> SignUpContent(it1) }
        }
        composable(NavigationScreen.CHAT_SCREEN) {
            screenState.value = screenState.value?.copy(
                appBarState = screenState.value?.appBarState?.copy(
                    appBarTitle = LocalStringResources.current.APP_NAME,
                    topAppBarActionVisible = false,
                ),
                floatingActionState = screenState.value?.floatingActionState?.copy(
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
            screenState.value = screenState.value?.copy(
                appBarState = screenState.value?.appBarState?.copy(
                    appBarTitle = NavigationScreen.SETTINGS_CHAT_SCREEN,
                    topAppBarActionVisible = true,
                    topAppBarAction = {
                        navController.navigateUp()
                    }
                ),
                floatingActionState = screenState.value?.floatingActionState?.copy(
                    floatingActionButtonVisible = false
                ))
            screenState.value?.let { it1 -> SettingsChatContent(it1) }
        }
        composable(NavigationScreen.SETTINGS_ACCOUNT_SCREEN) {
            screenState.value = screenState.value?.copy(
                appBarState = screenState.value?.appBarState?.copy(
                    appBarTitle = NavigationScreen.SETTINGS_ACCOUNT_SCREEN,
                    topAppBarActionVisible = true,
                    topAppBarAction = {
                        navController.navigateUp()
                    }
                ),
                floatingActionState = screenState.value?.floatingActionState?.copy(
                    floatingActionButtonVisible = false
                ))
            screenState.value?.let { it1 -> SettingsAccountContent(it1) }
        }
        composable(NavigationScreen.SETTINGS_LANGUAGE_SCREEN) {
            screenState.value = screenState.value?.copy(
                appBarState = screenState.value?.appBarState?.copy(
                    appBarTitle = NavigationScreen.SETTINGS_LANGUAGE_SCREEN,
                    topAppBarActionVisible = true,
                    topAppBarAction = {
                        navController.navigateUp()
                    }
                ),
                floatingActionState = screenState.value?.floatingActionState?.copy(
                    floatingActionButtonVisible = false
                ))
            screenState.value?.let { it1 -> SettingsLanguageContent(it1) }
        }
        composable(NavigationScreen.SETTINGS_THEME_SCREEN) {
            screenState.value = screenState.value?.copy(
                appBarState = screenState.value?.appBarState?.copy(
                    appBarTitle = NavigationScreen.SETTINGS_THEME_SCREEN,
                    topAppBarActionVisible = true,
                    topAppBarAction = {
                        navController.navigateUp()
                    }
                ),
                floatingActionState = screenState.value?.floatingActionState?.copy(
                    floatingActionButtonVisible = false
                ))
            screenState.value?.let { it1 -> SettingsThemeContent(it1) }
        }
        composable(NavigationScreen.SETTINGS_FEEDBACK_SCREEN) {
            screenState.value = screenState.value?.copy(
                appBarState = screenState.value?.appBarState?.copy(
                    appBarTitle = NavigationScreen.SETTINGS_FEEDBACK_SCREEN,
                    topAppBarActionVisible = true,
                    topAppBarAction = {
                        navController.navigateUp()
                    }
                ),
                floatingActionState = screenState.value?.floatingActionState?.copy(
                    floatingActionButtonVisible = false
                ))
            screenState.value?.let { it1 -> SettingsFeedbackContent(it1) }
        }
        composable(NavigationScreen.SETTINGS_PRIVACY_POLICY_SCREEN) {
            screenState.value = screenState.value?.copy(
                appBarState = screenState.value?.appBarState?.copy(
                    appBarTitle = NavigationScreen.SETTINGS_PRIVACY_POLICY_SCREEN,
                    topAppBarActionVisible = true,
                    topAppBarAction = {
                        navController.navigateUp()
                    }
                ),
                floatingActionState = screenState.value?.floatingActionState?.copy(
                    floatingActionButtonVisible = false
                ))
            screenState.value?.let { it1 -> SettingsPrivacyPolicyContent(it1) }
        }
        composable(NavigationScreen.SETTINGS_SIGN_UP_SCREEN) {
            screenState.value = screenState.value?.copy(
                appBarState = screenState.value?.appBarState?.copy(
                    appBarTitle = NavigationScreen.SETTINGS_SIGN_UP_SCREEN,
                    topAppBarActionVisible = true,
                    topAppBarAction = {
                        navController.navigateUp()
                    }
                ),
                floatingActionState = screenState.value?.floatingActionState?.copy(
                    floatingActionButtonVisible = false
                ))
            screenState.value?.let { it1 -> SettingsSignUpContent(it1) }
        }
    }
}