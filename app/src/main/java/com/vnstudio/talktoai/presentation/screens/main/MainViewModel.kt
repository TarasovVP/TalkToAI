package com.vnstudio.talktoai.presentation.screens.main

import android.app.Application
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.vnstudio.talktoai.CommonExtensions.isNotNull
import com.vnstudio.talktoai.CommonExtensions.isTrue
import com.vnstudio.talktoai.data.database.db_entities.Chat
import com.vnstudio.talktoai.data.database.db_entities.Message
import com.vnstudio.talktoai.domain.enums.AuthState
import com.vnstudio.talktoai.domain.sealed_classes.Result
import com.vnstudio.talktoai.domain.usecases.MainUseCase
import com.vnstudio.talktoai.presentation.screens.base.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch


class MainViewModel(
    private val mainUseCase: MainUseCase,
    application: Application,
) : BaseViewModel(application) {

    val onBoardingSeenLiveData = MutableStateFlow<Boolean?>(null)
    val chatsLiveData = MutableStateFlow<List<Chat>?>(listOf())
    val authStateLiveData = MutableStateFlow<AuthState?>(null)

    private var remoteChatListener: ValueEventListener? = null
    private var remoteMessageListener: ValueEventListener? = null
    private var authStateListener: FirebaseAuth.AuthStateListener? = null
    private var chatsFlowSubscription: Job? = null

    fun getOnBoardingSeen() {
        launch {
            mainUseCase.getOnBoardingSeen().collect { isOnBoardingSeen ->
                onBoardingSeenLiveData.value = isOnBoardingSeen.isTrue()
            }
        }
    }

    fun addAuthStateListener() {
        authStateListener  = FirebaseAuth.AuthStateListener { firebaseAuth ->
            val user = firebaseAuth.currentUser
            val authState = when {
                user?.isAnonymous.isTrue() -> AuthState.AUTHORISED_ANONYMOUSLY
                user.isNotNull() -> AuthState.AUTHORISED_EMAIL
                else -> {
                    chatsFlowSubscription?.cancel()
                    AuthState.UNAUTHORISED
                }
            }
            authStateLiveData.value = authState
        }
        authStateListener?.let { mainUseCase.addAuthStateListener(it) }
    }

    fun isLoggedInUser(): Boolean {
        return mainUseCase.isLoggedInUser()
    }

    fun isAuthorisedUser(): Boolean {
        return mainUseCase.isAuthorisedUser()
    }

    fun addRemoteChatListener() {
        remoteChatListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val chats = arrayListOf<Chat>()
                dataSnapshot.children.forEach { child ->
                    child.getValue(Chat::class.java)
                        ?.let { chats.add(it) }
                }
                launch {
                    mainUseCase.updateChats(chats)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                exceptionLiveData.value = databaseError.message
            }
        }
        remoteChatListener?.let { mainUseCase.addRemoteChatListener(it) }
    }

    fun addRemoteMessageListener() {
        remoteMessageListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val messages = arrayListOf<Message>()
                dataSnapshot.children.forEach { child ->
                    child.getValue(Message::class.java)
                        ?.let { messages.add(it) }
                }
                launch {
                    mainUseCase.updateMessages(messages)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                exceptionLiveData.value = databaseError.message
            }
        }
        remoteMessageListener?.let { mainUseCase.addRemoteMessageListener(it) }
    }

    fun getChats() {
        showProgress()
        chatsFlowSubscription?.cancel()
        chatsFlowSubscription = launch {
            mainUseCase.getChats().catch {
                hideProgress()

            }.collect { chats ->
                viewModelScope.launch(Dispatchers.Main) {
                    chatsLiveData.value = null
                }
                chatsLiveData.value = chats
                hideProgress()
            }
        }
    }

    fun updateChats(chats: List<Chat>) {
        launch {
            if (mainUseCase.isAuthorisedUser()) {
                checkNetworkAvailable {
                    showProgress()
                    mainUseCase.updateRemoteChats(chats) { authResult ->
                        when (authResult) {
                            is Result.Success -> {

                            }
                            is Result.Failure -> authResult.errorMessage?.let {
                                exceptionLiveData.value = it
                            }
                        }
                        hideProgress()
                    }
                }
            } else {
                launch {
                    mainUseCase.updateChats(chatsLiveData.value.orEmpty())
                }
            }
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
                            exceptionLiveData.value = it
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
        if (mainUseCase.isAuthorisedUser()) {
            checkNetworkAvailable {
                showProgress()
                mainUseCase.updateRemoteChat(chat) { authResult ->
                    when (authResult) {
                        is Result.Success -> {

                        }
                        is Result.Failure -> authResult.errorMessage?.let {
                            exceptionLiveData.value = it
                        }
                    }
                    hideProgress()
                }
            }
        } else {
            launch {
                mainUseCase.updateChat(chat)
            }
        }
    }

    fun deleteChat(chat: Chat) {
        if (mainUseCase.isAuthorisedUser()) {
            checkNetworkAvailable {
                showProgress()
                mainUseCase.deleteRemoteChat(chat) { authResult ->
                    when (authResult) {
                        is Result.Success -> {

                        }
                        is Result.Failure -> authResult.errorMessage?.let {
                            exceptionLiveData.value = it
                        }
                    }
                    hideProgress()
                }
            }
        } else {
            launch {
                mainUseCase.deleteChat(chat)
            }
        }
    }

    private fun removeAuthStateListener() {
        authStateListener?.let { mainUseCase.removeAuthStateListener(it) }
    }

    fun removeRemoteUserListeners() {
        remoteChatListener?.let { mainUseCase.removeRemoteChatListener(it) }
        remoteMessageListener?.let { mainUseCase.removeRemoteMessageListener(it) }
    }

    override fun onCleared() {
        super.onCleared()
        removeRemoteUserListeners()
        removeAuthStateListener()
        chatsFlowSubscription?.cancel()
    }

    fun swapChats(firstIndex: Int, secondIndex: Int) {
        val fromItem = chatsLiveData.value?.get(firstIndex)
        val toItem = chatsLiveData.value?.get(secondIndex)
        val newList = chatsLiveData.value.orEmpty().toMutableList()
        toItem?.let { newList[firstIndex] = it.apply {
            listOrder = chatsLiveData.value.orEmpty().size - firstIndex
        } }
        fromItem?.let { newList[secondIndex] = it.apply {
            listOrder = chatsLiveData.value.orEmpty().size - secondIndex
        } }
        chatsLiveData.value = null
        chatsLiveData.value = newList
    }
}