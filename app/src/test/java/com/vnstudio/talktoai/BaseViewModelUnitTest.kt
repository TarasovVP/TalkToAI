package com.vnstudio.talktoai

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.vnstudio.talktoai.presentation.screens.base.BaseViewModel
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule

@ExperimentalCoroutinesApi
abstract class BaseViewModelUnitTest<VM : BaseViewModel> {

    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    @MockK
    protected lateinit var application: TalkToAIApp

    protected lateinit var viewModel: VM

    abstract fun createViewModel(): VM

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        viewModel = createViewModel()
    }
}