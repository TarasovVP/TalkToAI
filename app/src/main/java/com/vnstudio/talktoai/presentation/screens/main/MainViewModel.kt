package com.vnstudio.talktoai.presentation.screens.main

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.vnstudio.talktoai.CommonExtensions.EMPTY
import com.vnstudio.talktoai.CommonExtensions.isNotNull
import com.vnstudio.talktoai.CommonExtensions.isTrue
import com.vnstudio.talktoai.domain.usecases.MainUseCase
import com.vnstudio.talktoai.presentation.screens.base.BaseViewModel
import com.vnstudio.talktoai.domain.sealed_classes.Result
import com.vnstudio.talktoai.domain.models.CurrentUser
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val mainUseCase: MainUseCase, application: Application) : BaseViewModel(application) {

    val onBoardingSeenLiveData = MutableLiveData<Boolean>()

    fun getOnBoardingSeen() {
        launch {
            mainUseCase.getOnBoardingSeen().collect { isOnBoardingSeen ->
                onBoardingSeenLiveData.postValue(isOnBoardingSeen.isTrue())
            }
        }
    }

    fun isLoggedInUser(): Boolean {
        return mainUseCase.isLoggedInUser()
    }

    fun getCurrentUser() {
        launch {
            mainUseCase.getCurrentUser { operationResult ->
                when(operationResult) {
                    is Result.Success -> operationResult.data.takeIf { it.isNotNull() }?.let { setCurrentUserData(it) } ?: exceptionLiveData.postValue(String.EMPTY)
                    is Result.Failure -> operationResult.errorMessage?.let { exceptionLiveData.postValue(it) }
                }
            }
        }
    }

    private fun setCurrentUserData(currentUser: CurrentUser) {

    }
}