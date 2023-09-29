package com.vnstudio.talktoai

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.platform.app.InstrumentationRegistry
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.vnstudio.talktoai.domain.models.InfoMessage
import com.vnstudio.talktoai.domain.usecases.SignUpUseCase
import com.vnstudio.talktoai.presentation.screens.authorization.signup.SignUpScreen
import com.vnstudio.talktoai.presentation.screens.authorization.signup.SignUpViewModel
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject


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

    @Before
    fun setUp() {
        hiltRule.inject()
        composeTestRule.setContent {
            SignUpScreen(
                SignUpViewModel(application, signUpUseCase, googleSignInClient),
                infoMessageState = infoMessageState,
                progressVisibilityState = progressVisibilityState
            ) {}
        }
    }

    @Test
    fun checkSignUpTitle() {
        composeTestRule.onNodeWithText(
            InstrumentationRegistry.getInstrumentation().targetContext.getString(
                R.string.authorization_sign_up
            )
        ).assertExists()

    }
}