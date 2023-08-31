package com.vnstudio.talktoai.domain.sealed_classes

sealed class NavigationScreen {
    data class OnboardingScreen(val route: String = "destination_onboarding_screen"): NavigationScreen()
    data class LoginScreen(val route: String = "destination_login_screen"): NavigationScreen()
    data class SignUpScreen(val route: String = "destination_sign_up_screen"): NavigationScreen()
    data class ChatScreen(val route: String = "destination_chat_screen"): NavigationScreen()
    data class SettingsChatScreen(val route: String = "destination_settings_chat_screen"): NavigationScreen()
    data class SettingsAccountScreen(val route: String = "destination_settings_account_screen"): NavigationScreen()
    data class SettingsSignUpScreen(val route: String = "destination_settings_sign_up_screen"): NavigationScreen()
    data class SettingsLanguageScreen(val route: String = "destination_settings_language_screen"): NavigationScreen()
    data class SettingsThemeScreen(val route: String = "destination_settings_theme_screen"): NavigationScreen()
    data class SettingsPrivacyPolicyScreen(val route: String = "destination_settings_privacy_policy_screen"): NavigationScreen()

    companion object {
        fun isTopBarNeeded(destination: String?): Boolean {
            return when (destination) {
                OnboardingScreen().route,
                LoginScreen().route,
                SignUpScreen().route -> false
                else -> true
            }
        }

        fun isSettingsScreen(destination: String?): Boolean {
            return when (destination) {
                SettingsChatScreen().route,
                SettingsLanguageScreen().route,
                SettingsThemeScreen().route,
                SettingsPrivacyPolicyScreen().route -> true
                else -> false
            }
        }
    }
}
