package com.vnteam.talktoai.presentation.viewmodels

import com.vnteam.talktoai.data.network.NetworkResult
import com.vnteam.talktoai.domain.enums.AuthState
import com.vnteam.talktoai.domain.models.Chat
import com.vnteam.talktoai.domain.usecase.MainUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class ChatListViewModel(
    private val mainUseCase: MainUseCase
) : BaseViewModel() {

    private val _chatsList = MutableStateFlow<List<Chat>?>(null)
    val chatsList = _chatsList.asStateFlow()
    private val _authState = MutableStateFlow<AuthState?>(null)
    val authState = _authState.asStateFlow()
    private val _animationResource = MutableStateFlow<String?>(null)
    val animationResource = _animationResource.asStateFlow()

    /*private var remoteChatListener: ValueEventListener? = null
    private var remoteMessageListener: ValueEventListener? = null
    private var authStateListener: FirebaseAuth.AuthStateListener? = null*/
    private var chatsFlowSubscription: Job? = null

    fun addAuthStateListener() {
        /*authStateListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            val user = firebaseAuth.currentUser
            val authState = when {
                user?.isAnonymous.isTrue() -> AuthState.AUTHORISED_ANONYMOUSLY
                user.isNotNull() -> AuthState.AUTHORISED_EMAIL
                else -> {
                    chatsFlowSubscription?.cancel()
                    AuthState.UNAUTHORISED
                }
            }
            _authState.value = authState
        }
        authStateListener?.let { mainUseCase.addAuthStateListener(it) }*/
    }

    fun isLoggedInUser(): Boolean {
        return mainUseCase.isLoggedInUser()
    }

    fun isAuthorisedUser(): Boolean {
        return mainUseCase.isAuthorisedUser()
    }

    fun addRemoteChatListener() {
        /*remoteChatListener = object : ValueEventListener {
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
        remoteChatListener?.let { mainUseCase.addRemoteChatListener(it) }*/
    }

    fun addRemoteMessageListener() {
        /*remoteMessageListener = object : ValueEventListener {
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
        remoteMessageListener?.let { mainUseCase.addRemoteMessageListener(it) }*/
    }

    fun getChats() {
        showProgress()
        chatsFlowSubscription?.cancel()
        chatsFlowSubscription = launch {
            mainUseCase.getChats().catch {
                hideProgress()

            }.collect { chats ->
                _chatsList.value = chats
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
                            is NetworkResult.Success -> {

                            }

                            is NetworkResult.Failure -> authResult.errorMessage?.let {
                                exceptionLiveData.value = it
                            }
                        }
                        hideProgress()
                    }
                }
            } else {
                launch {
                    mainUseCase.updateChats(_chatsList.value.orEmpty())
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
                        is NetworkResult.Success -> {

                        }

                        is NetworkResult.Failure -> authResult.errorMessage?.let {
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
                        is NetworkResult.Success -> {

                        }

                        is NetworkResult.Failure -> authResult.errorMessage?.let {
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
                        is NetworkResult.Success -> {

                        }

                        is NetworkResult.Failure -> authResult.errorMessage?.let {
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
        //authStateListener?.let { mainUseCase.removeAuthStateListener(it) }
    }

    fun removeRemoteUserListeners() {
        /*remoteChatListener?.let { mainUseCase.removeRemoteChatListener(it) }
        remoteMessageListener?.let { mainUseCase.removeRemoteMessageListener(it) }*/
    }

    override fun onCleared() {
        super.onCleared()
        removeRemoteUserListeners()
        removeAuthStateListener()
        chatsFlowSubscription?.cancel()
    }

    fun swapChats(firstIndex: Int, secondIndex: Int) {
        val fromItem = _chatsList.value?.get(firstIndex)
        val toItem = _chatsList.value?.get(secondIndex)
        val newList = _chatsList.value.orEmpty().toMutableList()
        toItem?.let {
            newList[firstIndex] = it.apply {
                listOrder = (_chatsList.value.orEmpty().size - firstIndex).toLong()
            }
        }
        fromItem?.let {
            newList[secondIndex] = it.apply {
                listOrder = (_chatsList.value.orEmpty().size - secondIndex).toLong()
            }
        }
        _chatsList.value = null
        _chatsList.value = newList
    }
}