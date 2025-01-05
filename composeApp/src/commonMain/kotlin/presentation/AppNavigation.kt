package presentation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import checkCurrentDestUpdateWithStartDest
import com.vnteam.talktoai.Constants
import com.vnteam.talktoai.presentation.ui.NavigationScreen
import presentation.screens.authorization.login.LoginScreen
import presentation.screens.authorization.onboarding.OnboardingScreen
import presentation.screens.authorization.signup.SignUpScreen
import presentation.screens.chat.ChatContent
import presentation.screens.settings.settings_account.SettingsAccountScreen
import presentation.screens.settings.settings_chat.SettingsChatContent
import presentation.screens.settings.settings_feedback.SettingsFeedbackContent
import presentation.screens.settings.settings_language.SettingsLanguageContent
import presentation.screens.settings.settings_privacy_policy.SettingsPrivacyPolicyContent
import presentation.screens.settings.settings_sign_up.SettingsSignUpScreen
import presentation.screens.settings.settings_theme.SettingsThemeContent

@Composable
fun AppNavigation(
    navController: NavHostController,
    startDestination: String
) {
    println("AppNavigationTAG: startDestination: $startDestination")
    navController.checkCurrentDestUpdateWithStartDest(startDestination)
    NavHost(navController = navController, startDestination = startDestination) {
        composable(NavigationScreen.ONBOARDING_SCREEN) {
            OnboardingScreen()
        }
        composable(NavigationScreen.LOGIN_SCREEN) {
            LoginScreen()
        }
        composable(NavigationScreen.SIGN_UP_SCREEN) {
            SignUpScreen()
        }
        composable(route = NavigationScreen.CHAT_SCREEN,
            arguments = listOf(
                navArgument(NavigationScreen.CHAT_ID) {
                    type = NavType.LongType
                }
            )
        ) { backStackEntry ->
            val chatId = backStackEntry.arguments?.getLong(
                NavigationScreen.CHAT_ID,
                Constants.DEFAULT_CHAT_ID
            ) ?: Constants.DEFAULT_CHAT_ID
            ChatContent(chatId)
        }
        composable(NavigationScreen.SETTINGS_CHAT_SCREEN) {
            SettingsChatContent()
        }
        composable(NavigationScreen.SETTINGS_ACCOUNT_SCREEN) {
            SettingsAccountScreen()
        }
        composable(NavigationScreen.SETTINGS_LANGUAGE_SCREEN) {
            SettingsLanguageContent()
        }
        composable(NavigationScreen.SETTINGS_THEME_SCREEN) {
            SettingsThemeContent()
        }
        composable(NavigationScreen.SETTINGS_FEEDBACK_SCREEN) {
            SettingsFeedbackContent()
        }
        composable(NavigationScreen.SETTINGS_PRIVACY_POLICY_SCREEN) {
            SettingsPrivacyPolicyContent()
        }
        composable(NavigationScreen.SETTINGS_SIGN_UP_SCREEN) {
            SettingsSignUpScreen()
        }
    }
}