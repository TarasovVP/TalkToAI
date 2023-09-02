package com.vnstudio.talktoai.presentation.screens.main

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.vnstudio.talktoai.CommonExtensions.EMPTY
import com.vnstudio.talktoai.CommonExtensions.isNotNull
import com.vnstudio.talktoai.CommonExtensions.isTrue
import com.vnstudio.talktoai.data.database.db_entities.Chat
import com.vnstudio.talktoai.domain.models.RemoteUser
import com.vnstudio.talktoai.domain.sealed_classes.Result
import com.vnstudio.talktoai.domain.usecases.MainUseCase
import com.vnstudio.talktoai.presentation.screens.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val mainUseCase: MainUseCase,
    application: Application,
) : BaseViewModel(application) {

    val onBoardingSeenLiveData = MutableLiveData<Boolean>()
    val chatsLiveData = MutableLiveData<List<Chat>>()

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

    fun getRemoteUser() {
        launch {
            mainUseCase.getRemoteUser { operationResult ->
                when (operationResult) {
                    is Result.Success -> operationResult.data.takeIf { it.isNotNull() }
                        ?.let { setCurrentUserData(it) }
                        ?: exceptionLiveData.postValue(String.EMPTY)
                    is Result.Failure -> operationResult.errorMessage?.let {
                        exceptionLiveData.postValue(
                            it
                        )
                    }
                }
            }
        }
    }

    private fun setCurrentUserData(remoteUser: RemoteUser) {
        launch {
            mainUseCase.insertChats(remoteUser.chats)
            mainUseCase.insertMessages(remoteUser.messages)
        }
    }

    fun getChats() {
        showProgress()
        launch {
            mainUseCase.getChats().catch {
                hideProgress()
                Log.e(
                    "apiTAG",
                    "MainViewModel getChats catch localizedMessage ${it.localizedMessage}"
                )
            }.collect { chats ->
                chatsLiveData.postValue(chats)
                hideProgress()
                Log.e("apiTAG", "MainViewModel getChats chats $chats")
            }
        }
    }

    fun updateChats(chats: List<Chat>) {
        showProgress()
        launch {
            mainUseCase.updateChats(chats)
        }
    }

    fun insertChat(chat: Chat) {
        showProgress()
        launch {
            mainUseCase.insertChat(chat)
        }
    }

    fun updateChat(chat: Chat) {
        showProgress()
        launch {
            mainUseCase.updateChat(chat)
        }
    }

    fun deleteChat(chat: Chat) {
        showProgress()
        launch {
            mainUseCase.deleteChat(chat)
        }
    }

}