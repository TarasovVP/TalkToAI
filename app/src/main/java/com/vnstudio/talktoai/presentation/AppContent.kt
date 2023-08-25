package com.vnstudio.talktoai.presentation

import androidx.compose.runtime.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.vnstudio.talktoai.presentation.chat.ChatScreen
import com.vnstudio.talktoai.presentation.onboarding.login.LoginScreen
import com.vnstudio.talktoai.presentation.onboarding.onboarding.OnboardingScreen
import com.vnstudio.talktoai.presentation.onboarding.signup.SignUpScreen
import com.vnstudio.talktoai.presentation.settings.settings_account.SettingsAccountScreen
import com.vnstudio.talktoai.presentation.settings.settings_chat.SettingsChatScreen
import com.vnstudio.talktoai.presentation.settings.settings_language.SettingsLanguageScreen
import com.vnstudio.talktoai.presentation.settings.settings_list.SettingsListScreen
import com.vnstudio.talktoai.presentation.settings.settings_privacy_policy.SettingsPrivacyPolicyScreen
import com.vnstudio.talktoai.presentation.settings.settings_sign_up.SettingsSignUpScreen
import com.vnstudio.talktoai.presentation.settings.settings_theme.SettingsThemeScreen

@Composable
fun AppContent(
    startDestination: String,
) {
    val navController = rememberNavController()
    NavHost(navController, startDestination = startDestination) {
        composable(
            route = "destination_onboarding_screen"
        ) {
            OnboardingScreen {
                navController.navigate("destination_login_screen")
            }
        }
        composable(
            route = "destination_login_screen"
        ) {
            LoginScreen { destination ->
                navController.navigate(destination)
            }
        }
        composable(
            route = "destination_sign_up_screen"
        ) {
            SignUpScreen { destination ->
                navController.navigate(destination)
            }
        }
        composable(
            route = "destination_chat_screen"
        ) {
            ChatScreen(
                onChatClicked = {
                    navController.navigate("destination_chat_screen")
                },
                onSettingsClicked = {
                    navController.navigate("destination_settings_list_screen")
                }
            )

        }
        composable(
            route = "destination_settings_list_screen"
        ) {
            SettingsListScreen {

            }
        }
        composable(
            route = "destination_settings_chat_screen"
        ) {
            SettingsChatScreen {

            }
        }
        composable(
            route = "destination_settings_account_screen"
        ) {
            SettingsAccountScreen {

            }
        }
        composable(
            route = "destination_settings_sign_up_screen"
        ) {
            SettingsSignUpScreen {

            }
        }
        composable(
            route = "destination_settings_language_screen"
        ) {
            SettingsLanguageScreen {

            }
        }
        composable(
            route = "destination_settings_theme_screen"
        ) {
            SettingsThemeScreen {

            }
        }
        composable(
            route = "destination_settings_privacy_policy_screen"
        ) {
            SettingsPrivacyPolicyScreen {

            }
        }
    }
}