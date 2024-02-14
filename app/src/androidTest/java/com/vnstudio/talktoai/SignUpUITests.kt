package com.vnstudio.talktoai

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.PasswordVisualTransformation
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.vnstudio.talktoai.domain.models.InfoMessage
import com.vnstudio.talktoai.domain.sealed_classes.NavigationScreen
import com.vnstudio.talktoai.domain.usecases.SignUpUseCase
import com.vnstudio.talktoai.presentation.screens.authorization.signup.SignUpScreen
import com.vnstudio.talktoai.presentation.screens.authorization.signup.SignUpViewModel
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test



@HiltAndroidTest
class SignUpUITests {

    @get:Rule(order = 1)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 2)
    val composeTestRule = createComposeRule()

    @Inject
    lateinit var application: Application

    @Inject
    lateinit var signUpUseCase: SignUpUseCase

    @Inject
    lateinit var googleSignInClient: GoogleSignInClient

    private val infoMessageState = mutableStateOf<InfoMessage?>(null)
    private val progressVisibilityState = mutableStateOf(false)
    private var onNextScreenValue: String? = null
    private var onNextScreen: (String) -> Unit = {
        onNextScreenValue = it
    }

    @Before
    fun setUp() {
        hiltRule.inject()
        composeTestRule.setContent {
            SignUpScreen(
                SignUpViewModel(application, signUpUseCase, googleSignInClient),
                infoMessageState = infoMessageState,
                progressVisibilityState = progressVisibilityState,
                onNextScreen
            )
        }
    }

    @Test
    fun checkSignUpTitle() {
        composeTestRule.onNodeWithText(
            application.getString(
                R.string.authorization_sign_up
            )
        ).assertIsDisplayed()
    }

    @Test
    fun checkSignUpEmail() {
        composeTestRule.onNodeWithText(
            application.getString(R.string.authorization_email)
        ).assertIsDisplayed().performTextInput("testEmail")
        composeTestRule.onNodeWithText("testEmail").assertIsDisplayed()
    }

    @Test
    fun checkSignUpPassword() {
        composeTestRule.onNodeWithText(
            application.getString(R.string.authorization_password)
        ).assertIsDisplayed().performTextInput("testPassword")
        composeTestRule.onNodeWithText(PasswordVisualTransformation().filter(AnnotatedString("testPassword")).text.toString()).assertIsDisplayed()
    }

    @Test
    fun checkSignUpEntranceTitle() {
        composeTestRule.onNodeWithText(
            application.getString(R.string.authorization_entrance_title)
        ).assertIsDisplayed()
        composeTestRule.onNodeWithText(
            application.getString(R.string.authorization_entrance)
        ).assertIsDisplayed()
    }

    @Test
    fun checkSignUpContinue() {
        composeTestRule.onNodeWithTag("sign_up_button").apply {
            assertIsDisplayed()
            assertIsNotEnabled()
            composeTestRule.onNodeWithText(application.getString(R.string.authorization_email)).assertIsDisplayed().performTextInput("testEmail")
            assertIsNotEnabled()
            composeTestRule.onNodeWithText(application.getString(R.string.authorization_password)).performTextInput("testPassword")
            assertIsEnabled().performClick()
            composeTestRule.waitUntil(10000) {
                "The email address is badly formatted." == infoMessageState.value?.message
            }
        }
    }

    @Test
    fun checkSignUpEntrance() {
        composeTestRule.onNodeWithText(application.getString(R.string.authorization_entrance)).assertIsDisplayed().performClick()
        assert(NavigationScreen.LoginScreen().route == onNextScreenValue)
    }
}