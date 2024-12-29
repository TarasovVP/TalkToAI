package presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.vnteam.talktoai.presentation.ui.NavigationScreen
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
fun AppNavigation(
    navController: NavHostController,
    screenState: ScreenState,
    onScreenStateUpdate: (ScreenState?) -> Unit
) {
    NavHost(navController = navController, startDestination = screenState.startDestination) {
        composable(NavigationScreen.ONBOARDING_SCREEN) {
            OnboardingContent(screenState, onScreenStateUpdate)
        }
        composable(NavigationScreen.LOGIN_SCREEN) {
            LoginContent(screenState, onScreenStateUpdate)
        }
        composable(NavigationScreen.SIGN_UP_SCREEN) {
            SignUpContent(screenState, onScreenStateUpdate)
        }
        composable(NavigationScreen.CHAT_SCREEN) {
            ChatContent(-1L, mutableStateOf(false), screenState, onScreenStateUpdate)
        }
        composable(NavigationScreen.SETTINGS_CHAT_SCREEN) {
            SettingsChatContent(screenState, onScreenStateUpdate)
        }
        composable(NavigationScreen.SETTINGS_ACCOUNT_SCREEN) {
            SettingsAccountContent(screenState, onScreenStateUpdate)
        }
        composable(NavigationScreen.SETTINGS_LANGUAGE_SCREEN) {
            SettingsLanguageContent(screenState, onScreenStateUpdate)
        }
        composable(NavigationScreen.SETTINGS_THEME_SCREEN) {
            SettingsThemeContent(screenState, onScreenStateUpdate)
        }
        composable(NavigationScreen.SETTINGS_FEEDBACK_SCREEN) {
            SettingsFeedbackContent(screenState, onScreenStateUpdate)
        }
        composable(NavigationScreen.SETTINGS_PRIVACY_POLICY_SCREEN) {
            SettingsPrivacyPolicyContent(screenState, onScreenStateUpdate)
        }
        composable(NavigationScreen.SETTINGS_SIGN_UP_SCREEN) {
            SettingsSignUpContent(screenState, onScreenStateUpdate)
        }
    }
}