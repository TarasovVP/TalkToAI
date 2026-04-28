package com.vnteam.talktoai.presentation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.fadeOut
import androidx.compose.animation.core.snap
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.vnteam.talktoai.Constants
import com.vnteam.talktoai.checkCurrentDestUpdateWithStartDest
import com.vnteam.talktoai.presentation.screens.authorization.login.LoginScreen
import com.vnteam.talktoai.presentation.screens.authorization.onboarding.OnboardingScreen
import com.vnteam.talktoai.presentation.screens.authorization.signup.SignUpScreen
import com.vnteam.talktoai.presentation.screens.chat.ChatContent
import com.vnteam.talktoai.presentation.screens.settings.settings_account.SettingsAccountScreen
import com.vnteam.talktoai.presentation.screens.settings.settings_list.SettingsListScreen
import com.vnteam.talktoai.presentation.screens.settings.settings_chat.SettingsChatContent
import com.vnteam.talktoai.presentation.screens.settings.settings_feedback.SettingsFeedbackContent
import com.vnteam.talktoai.presentation.screens.settings.settings_language.SettingsLanguageContent
import com.vnteam.talktoai.presentation.screens.settings.settings_login.SettingsLoginScreen
import com.vnteam.talktoai.presentation.screens.settings.settings_privacy_policy.SettingsPrivacyPolicyContent
import com.vnteam.talktoai.presentation.screens.settings.settings_sign_up.SettingsSignUpScreen
import com.vnteam.talktoai.presentation.screens.settings.settings_theme.SettingsThemeContent
import com.vnteam.talktoai.presentation.ui.NavigationScreen

@Composable
private fun Screen(content: @Composable () -> Unit) {
    Surface(modifier = Modifier.fillMaxSize()) {
        content()
    }
}

@Composable
fun AppNavigation(
    navController: NavHostController,
    startDestination: String,
) {
    println("AppContentTAG AppNavigationTAG: startDestination: $startDestination")
    navController.checkCurrentDestUpdateWithStartDest(startDestination)
    NavHost(
        navController = navController,
        startDestination = startDestination,
        enterTransition = { EnterTransition.None },
        exitTransition = { fadeOut(animationSpec = snap()) },
        popEnterTransition = { EnterTransition.None },
        popExitTransition = { fadeOut(animationSpec = snap()) }
    ) {
        composable(NavigationScreen.ONBOARDING_SCREEN) {
            Screen {
                OnboardingScreen(onComplete = {
                    navController.navigate(NavigationScreen.LOGIN_SCREEN) {
                        popUpTo(NavigationScreen.ONBOARDING_SCREEN) { inclusive = true }
                    }
                })
            }
        }
        composable(NavigationScreen.LOGIN_SCREEN) {
            Screen { LoginScreen() }
        }
        composable(NavigationScreen.SIGN_UP_SCREEN) {
            Screen { SignUpScreen() }
        }
        composable(
            route = NavigationScreen.CHAT_SCREEN,
            arguments = listOf(
                navArgument(NavigationScreen.CHAT_ID) {
                    type = NavType.LongType
                }
            )
        ) { backStackEntry ->
            val chatId = backStackEntry.arguments?.let {
                NavType.LongType[it, NavigationScreen.CHAT_ID]
            } ?: Constants.DEFAULT_CHAT_ID
            Screen { ChatContent(chatId) }
        }
        composable(NavigationScreen.SETTINGS_LIST_SCREEN) {
            Screen { SettingsListScreen() }
        }
        composable(NavigationScreen.SETTINGS_CHAT_SCREEN) {
            Screen { SettingsChatContent() }
        }
        composable(NavigationScreen.SETTINGS_ACCOUNT_SCREEN) {
            Screen { SettingsAccountScreen() }
        }
        composable(NavigationScreen.SETTINGS_LANGUAGE_SCREEN) {
            Screen { SettingsLanguageContent() }
        }
        composable(NavigationScreen.SETTINGS_THEME_SCREEN) {
            Screen { SettingsThemeContent() }
        }
        composable(NavigationScreen.SETTINGS_FEEDBACK_SCREEN) {
            Screen { SettingsFeedbackContent() }
        }
        composable(NavigationScreen.SETTINGS_PRIVACY_POLICY_SCREEN) {
            Screen { SettingsPrivacyPolicyContent() }
        }
        composable(NavigationScreen.SETTINGS_SIGN_UP_SCREEN) {
            Screen { SettingsSignUpScreen() }
        }
        composable(NavigationScreen.SETTINGS_LOGIN_SCREEN) {
            Screen { SettingsLoginScreen() }
        }
    }
}