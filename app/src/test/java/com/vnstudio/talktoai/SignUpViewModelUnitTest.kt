package com.vnstudio.talktoai

import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.vnstudio.talktoai.domain.usecases.SignUpUseCase
import com.vnstudio.talktoai.presentation.screens.authorization.signup.SignUpViewModel
import com.vnstudio.talktoai.domain.sealed_classes.Result
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Test

@ExperimentalCoroutinesApi
class SignUpViewModelUnitTest: BaseViewModelUnitTest<SignUpViewModel>() {

    @MockK
    private lateinit var useCase: SignUpUseCase

    @MockK
    private lateinit var googleSignInClient: GoogleSignInClient

    override fun createViewModel() = SignUpViewModel(application, useCase, googleSignInClient)

    @Test
    fun createUserWithEmailAndPasswordTest() {
        val expectedResult = Result.Success<Unit>()
        every { application.isNetworkAvailable } returns true
        every { useCase.createUserWithEmailAndPassword(eq("testEmail"), eq("testPassword"), any()) } answers {
            val callback = thirdArg<(Result<Unit>) -> Unit>()
            callback.invoke(expectedResult)
        }
        viewModel.createUserWithEmailAndPassword("testEmail", "testPassword")
        verify { useCase.createUserWithEmailAndPassword("testEmail", "testPassword", any()) }
        assertEquals(true, viewModel.uiState.value.successSignUp)
    }
}