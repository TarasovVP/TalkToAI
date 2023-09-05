package com.vnstudio.talktoai.presentation.screens.main

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
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
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val mainUseCase: MainUseCase,
    application: Application,
) : BaseViewModel(application) {

    val onBoardingSeenLiveData = MutableLiveData<Boolean>()
    val chatsLiveData = MutableLiveData<List<Chat>>()
    val authStateLiveData = MutableLiveData<AuthState>()

    private var remoteChatListener: ValueEventListener? = null
    private var remoteMessageListener: ValueEventListener? = null
    private var authStateListener: FirebaseAuth.AuthStateListener? = null
    private var chatsFlowSubscription: Job? = null

    fun getOnBoardingSeen() {
        launch {
            mainUseCase.getOnBoardingSeen().collect { isOnBoardingSeen ->
                onBoardingSeenLiveData.postValue(isOnBoardingSeen.isTrue())
            }
        }
    }

    fun addAuthStateListener() {
        Log.e("authTAG", "MainViewModel addAuthStateListener")
        authStateListener  = FirebaseAuth.AuthStateListener { firebaseAuth ->
            val user = firebaseAuth.currentUser
            val authState = when {
                user?.isAnonymous.isTrue() -> AuthState.AUTHORISED_ANONYMOUSLY
                user.isNotNull() -> AuthState.AUTHORISED
                else -> {
                    chatsFlowSubscription?.cancel()
                    AuthState.UNAUTHORISED
                }
            }
            authStateLiveData.postValue(authState)
            Log.e("authTAG", "MainViewModel addAuthStateListener authStateListener user.isNotNull() ${user.isNotNull()} user?.email ${user?.email} user?.isAnonymous ${user?.isAnonymous} ")

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
                Log.e("changeDBTAG", "RealDataBaseRepositoryImpl listenRemoteChatChanges onDataChange chats $chats")
            }

            override fun onCancelled(databaseError: DatabaseError) {
                exceptionLiveData.postValue(databaseError.message)
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
        chatsFlowSubscription?.cancel()
        chatsFlowSubscription = launch {
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
                Log.e("changeDBTAG", "MainViewModel getChats chatsLiveData.value ${chatsLiveData.value} chats $chats")
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
        if (mainUseCase.isAuthorisedUser()) {
            checkNetworkAvailable {
                showProgress()
                mainUseCase.updateRemoteChat(chat) { authResult ->
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
                            exceptionLiveData.postValue(it)
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
        Log.e("changeDBTAG", "RealDataBaseRepositoryImpl onCleared")
        removeRemoteUserListeners()
        removeAuthStateListener()
        chatsFlowSubscription?.cancel()
    }
}