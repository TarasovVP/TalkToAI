package com.vnteam.talktoai.presentation.viewmodels

import com.vnteam.talktoai.data.ANONYMOUS_USER
import com.vnteam.talktoai.data.GOOGLE_USER
import com.vnteam.talktoai.data.network.onSuccess
import com.vnteam.talktoai.domain.enums.AuthState
import com.vnteam.talktoai.domain.enums.isAuthorisedUser
import com.vnteam.talktoai.domain.models.Chat
import com.vnteam.talktoai.domain.usecase.MainUseCase
import com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.settings.UserLoginUseCase
import com.vnteam.talktoai.utils.NetworkState
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class ChatListViewModel(
    private val mainUseCase: MainUseCase,
    private val networkState: NetworkState,

    private val userLoginUseCase: UserLoginUseCase
) : BaseViewModel() {

    private val _chatsList = MutableStateFlow<List<Chat>?>(null)
    val chatsList = _chatsList.asStateFlow()
    private val _authState = MutableStateFlow<AuthState?>(null)
    val authState = _authState.asStateFlow()
    private val _animationResource = MutableStateFlow<String?>(null)
    val animationResource = _animationResource.asStateFlow()

    /*private var remoteChatListener: ValueEventListener? = null
    private var remoteMessageListener: ValueEventListener? = null*/
    private var chatsFlowSubscription: Job? = null


    fun getUserLogin() {
        launchWithResultHandling {
            userLoginUseCase.getUserLogin().onSuccess { userLogin ->
                _authState.value = when {
                    userLogin.isNullOrEmpty() -> AuthState.UNAUTHORISED
                    userLogin == ANONYMOUS_USER -> AuthState.AUTHORISED_ANONYMOUSLY
                    userLogin.contains(GOOGLE_USER) -> AuthState.AUTHORISED_GOOGLE
                    else -> AuthState.AUTHORISED_EMAIL
                }
            }
        }
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
        chatsFlowSubscription?.cancel()
        chatsFlowSubscription = launchWithResultHandling {
            mainUseCase.getChats().onSuccess { chats ->
                _chatsList.value = chats
                hideProgress()
            }
        }
    }

    fun updateChats(chats: List<Chat>) {
        if (authState.value.isAuthorisedUser()) {
            launchWithNetworkCheck(networkState) {
                mainUseCase.updateRemoteChats(chats)
            }
        } else {
            launchWithErrorHandling {
                mainUseCase.updateChats(_chatsList.value.orEmpty())
            }
        }
    }

    fun insertChat(chat: Chat) {
        if (authState.value.isAuthorisedUser()) {
            launchWithNetworkCheck(networkState) {
                mainUseCase.insertRemoteChat(chat)
            }
        } else {
            launchWithErrorHandling {
                mainUseCase.insertChat(chat)
                // TODO add temporary
                getChats()
            }
        }
    }

    fun updateChat(chat: Chat) {
        if (authState.value.isAuthorisedUser()) {
            launchWithNetworkCheck(networkState) {
                mainUseCase.updateRemoteChat(chat)
            }
        } else {
            launchWithErrorHandling {
                mainUseCase.updateChat(chat)
            }
        }
    }

    fun deleteChat(chat: Chat) {
        if (authState.value.isAuthorisedUser()) {
            launchWithNetworkCheck(networkState) {
                mainUseCase.deleteRemoteChat(chat)
            }
        } else {
            launchWithErrorHandling {
                mainUseCase.deleteChat(chat)
            }
        }
    }

    fun removeRemoteUserListeners() {
        /*remoteChatListener?.let { mainUseCase.removeRemoteChatListener(it) }
        remoteMessageListener?.let { mainUseCase.removeRemoteMessageListener(it) }*/
    }

    override fun onCleared() {
        super.onCleared()
        removeRemoteUserListeners()
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