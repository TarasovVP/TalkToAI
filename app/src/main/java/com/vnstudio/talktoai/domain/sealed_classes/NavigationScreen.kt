package com.vnstudio.talktoai.domain.sealed_classes

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import cafe.adriel.voyager.core.screen.Screen
import com.vnstudio.talktoai.CommonExtensions.EMPTY
import com.vnstudio.talktoai.presentation.screens.authorization.login.LoginContent
import com.vnstudio.talktoai.presentation.screens.authorization.onboarding.OnboardingContent
import com.vnstudio.talktoai.presentation.screens.authorization.signup.SignUpContent
import com.vnstudio.talktoai.presentation.screens.chat.ChatContent
import com.vnstudio.talktoai.presentation.screens.settings.settings_account.SettingsAccountContent
import com.vnstudio.talktoai.presentation.screens.settings.settings_chat.SettingsChatContent
import com.vnstudio.talktoai.presentation.screens.settings.settings_feedback.SettingsFeedbackContent
import com.vnstudio.talktoai.presentation.screens.settings.settings_language.SettingsLanguageContent
import com.vnstudio.talktoai.presentation.screens.settings.settings_privacy_policy.SettingsPrivacyPolicyContent
import com.vnstudio.talktoai.presentation.screens.settings.settings_theme.SettingsThemeContent
import com.vnstudio.talktoai.resources.StringResources

sealed class NavigationScreen(val route: String, val icon: String = String.EMPTY): Screen {
    class OnboardingScreen(private val content: Unit = Unit) :
        NavigationScreen("destination_onboarding_screen", "ic_onboarding") {
        @Composable
        override fun Content() {
            content
        }
    }
    class LoginScreen(private val content: Unit = Unit) : NavigationScreen( "destination_login_screen", "ic_login") {
        @Composable
        override fun Content() {
            content
        }
    }
    class SignUpScreen(private val content: Unit = Unit) : NavigationScreen("destination_sign_up_screen", "ic_sign_up") {
        @Composable
        override fun Content() {
            content
        }
    }
    class ChatScreen(private val content: Unit = Unit) : NavigationScreen("destination_chat_screen/{current_chat_id}", "ic_chat") {
        @Composable
        override fun Content() {
            content
        }
    }
    class SettingsChatScreen(private val content: Unit = Unit) :
        NavigationScreen("destination_settings_chat_screen", "ic_settings_chat") {
        @Composable
        override fun Content() {
            content
        }
    }
    class SettingsAccountScreen(private val content: Unit = Unit) :
        NavigationScreen("destination_settings_account_screen", "ic_settings_account") {
        @Composable
        override fun Content() {
            content
        }
    }
    class SettingsSignUpScreen(private val content: Unit = Unit) :
        NavigationScreen("destination_settings_sign_up_screen", "ic_settings_sign_up") {
        @Composable
        override fun Content() {
            content
        }
    }
    class SettingsLanguageScreen(private val content: Unit = Unit) :
        NavigationScreen("destination_settings_language_screen", "ic_settings_language") {
        @Composable
        override fun Content() {
            content
        }
    }
    class SettingsThemeScreen(private val content: Unit = Unit) :
        NavigationScreen("destination_settings_theme_screen", "ic_settings_theme") {
        @Composable
        override fun Content() {
            content
        }
    }
    class SettingsFeedbackScreen(private val content: Unit = Unit) :
        NavigationScreen("destination_settings_feedback_screen", "ic_settings_feedback") {
        @Composable
        override fun Content() {
            content
        }
    }
    class SettingsPrivacyPolicyScreen(private val content: Unit = Unit) :
        NavigationScreen( "destination_settings_privacy_policy_screen", "ic_settings_privacy") {
        @Composable
        override fun Content() {
            content
        }
    }

    companion object {

        val settingScreens = listOf(
            SettingsChatScreen(),
            SettingsAccountScreen(),
            SettingsLanguageScreen(),
            SettingsThemeScreen(),
            SettingsFeedbackScreen(),
            SettingsPrivacyPolicyScreen()
        )
        fun isSettingsScreen(route: String?): Boolean {
            return route in settingScreens.map { it.route }
        }
        fun settingsScreenNameByRoute(route: String?, stringRes: StringResources): String {
            return when(route) {
                SettingsChatScreen().route -> return stringRes.SETTINGS_CHAT
                SettingsAccountScreen().route -> return stringRes.SETTINGS_ACCOUNT
                SettingsLanguageScreen().route -> return stringRes.SETTINGS_LANGUAGE
                SettingsThemeScreen().route -> return stringRes.SETTINGS_THEME
                SettingsFeedbackScreen().route -> return stringRes.SETTINGS_FEEDBACK
                SettingsPrivacyPolicyScreen().route -> return stringRes.SETTINGS_PRIVACY_POLICY
                else -> stringRes.APP_NAME
            }
        }

        @Composable
        fun fromRoute(route: String?, onNextScreen: (String) -> Unit): Screen {
            return when(route) {
                OnboardingScreen().route -> OnboardingScreen(OnboardingContent(onNextScreen))
                LoginScreen().route -> LoginScreen(SettingsPrivacyPolicyContent(mutableStateOf(false)))
                SignUpScreen().route -> SignUpScreen(/*SignUpContent(onNextScreen)*/)
                ChatScreen().route -> ChatScreen(OnboardingContent(onNextScreen)/*ChatContent(onNextScreen)*/)
                SettingsChatScreen().route -> SettingsChatScreen(/*SettingsChatContent(onNextScreen)*/)
                SettingsAccountScreen().route -> SettingsAccountScreen(/*SettingsAccountContent(onNextScreen)*/)
                SettingsLanguageScreen().route -> SettingsLanguageScreen(/*SettingsLanguageContent(onNextScreen)*/)
                SettingsThemeScreen().route -> SettingsThemeScreen(/*SettingsThemeContent(onNextScreen)*/)
                SettingsFeedbackScreen().route -> SettingsFeedbackScreen(/*SettingsFeedbackContent(onNextScreen)*/)
                SettingsPrivacyPolicyScreen().route -> SettingsPrivacyPolicyScreen(/*SettingsPrivacyPolicyContent(onNextScreen)*/)
                else -> OnboardingScreen()
            }
        }
    }
}
