package com.vnstudio.talktoai.domain.usecases

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ValueEventListener
import com.vnstudio.talktoai.data.database.db_entities.Chat
import com.vnstudio.talktoai.data.database.db_entities.Message
import com.vnstudio.talktoai.domain.sealed_classes.Result
import kotlinx.coroutines.flow.Flow

interface MainUseCase {

    suspend fun getOnBoardingSeen(): Flow<Boolean?>

    fun addAuthStateListener(authStateListener: FirebaseAuth.AuthStateListener)

    fun removeAuthStateListener(authStateListener: FirebaseAuth.AuthStateListener)

    fun isLoggedInUser(): Boolean

    fun isAuthorisedUser(): Boolean

    fun addRemoteChatListener(remoteChatListener: ValueEventListener)

    fun addRemoteMessageListener(remoteMessageListener: ValueEventListener)

    fun removeRemoteChatListener(remoteChatListener: ValueEventListener)

    fun removeRemoteMessageListener(remoteMessageListener: ValueEventListener)

    suspend fun setReviewVoted(isReviewVoted: Boolean)

    suspend fun insertChats(chats: List<Chat>)

    suspend fun insertMessages(messages: List<Message>)

    suspend fun getChats(): Flow<List<Chat>>

    suspend fun updateChats(chats: List<Chat>)

    suspend fun insertChat(chat: Chat)

    fun insertRemoteChat(chat: Chat, result: (Result<Unit>) -> Unit)

    suspend fun updateChat(chat: Chat)

    fun updateRemoteChat(chat: Chat, result: (Result<Unit>) -> Unit)

    suspend fun deleteChat(chat: Chat)

    fun deleteRemoteChat(chat: Chat, result: (Result<Unit>) -> Unit)

    suspend fun updateMessages(messages: List<Message>)
}