package com.vnstudio.talktoai.presentation.screens.main

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.vnstudio.talktoai.CommonExtensions.isTrue
import com.vnstudio.talktoai.data.database.db_entities.Chat
import com.vnstudio.talktoai.data.database.db_entities.Message
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

    private var remoteChatListener: ValueEventListener? = null
    private var remoteMessageListener: ValueEventListener? = null

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

    fun isAuthorisedUser(): Boolean {
        return mainUseCase.isAuthorisedUser()
    }

    fun getRemoteUser() {
        launch {
            mainUseCase.getRemoteUser { operationResult ->
                when (operationResult) {
                    is Result.Success -> {
                        addRemoteChatListener()
                        addRemoteMessageListener()
                    }
                    is Result.Failure -> operationResult.errorMessage?.let {
                        exceptionLiveData.postValue(
                            it
                        )
                    }
                }
            }
        }
    }

    private fun addRemoteChatListener() {
        remoteChatListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val chats = arrayListOf<Chat>()
                dataSnapshot.children.forEach { child ->
                    child.getValue(Chat::class.java)
                        ?.let { chats.add(it) }
                }
                launch {
                    mainUseCase.insertChats(chats)
                }
                Log.e("changeDBTAG", "RealDataBaseRepositoryImpl listenRemoteChatChanges onDataChange chats $chats")
            }

            override fun onCancelled(databaseError: DatabaseError) {
                exceptionLiveData.postValue(databaseError.message)
            }
        }
        remoteChatListener?.let { mainUseCase.addRemoteChatListener(it) }
    }

    private fun addRemoteMessageListener() {
        remoteMessageListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val messages = arrayListOf<Message>()
                dataSnapshot.children.forEach { child ->
                    child.getValue(Message::class.java)
                        ?.let { messages.add(it) }
                }
                launch {
                    mainUseCase.insertMessages(messages)
                }
                Log.e("changeDBTAG", "RealDataBaseRepositoryImpl listenRemoteMessageChanges onDataChange messages $messages")
            }

            override fun onCancelled(databaseError: DatabaseError) {
                exceptionLiveData.postValue(databaseError.message)
            }
        }
        remoteMessageListener?.let { mainUseCase.addRemoteMessageListener(it) }
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
        if (mainUseCase.isAuthorisedUser()) {
            checkNetworkAvailable {
                showProgress()
                mainUseCase.insertRemoteChat(chat) { authResult ->
                    when (authResult) {
                        is Result.Success -> {

                        }
                        is Result.Failure -> authResult.errorMessage?.let {
                            exceptionLiveData.postValue(it)
                        }
                    }
                    hideProgress()
                }
            }
        } else {
            launch {
                mainUseCase.insertChat(chat)
            }
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

    override fun onCleared() {
        super.onCleared()
        remoteChatListener?.let { mainUseCase.removeRemoteChatListener(it) }
        remoteMessageListener?.let { mainUseCase.removeRemoteMessageListener(it) }
    }

}