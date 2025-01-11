package com.vnteam.talktoai.domain.usecase

import com.vnteam.talktoai.data.network.NetworkResult
import com.vnteam.talktoai.domain.models.Chat
import com.vnteam.talktoai.domain.models.Message
import kotlinx.coroutines.flow.Flow

interface MainUseCase {

    suspend fun getOnBoardingSeen(): Flow<NetworkResult<Boolean?>>

    fun addAuthStateListener()

    fun removeAuthStateListener()

    fun isLoggedInUser(): Boolean

    fun isAuthorisedUser(): Boolean

    fun addRemoteChatListener()

    fun addRemoteMessageListener()

    fun removeRemoteChatListener()

    fun removeRemoteMessageListener()

    suspend fun setReviewVoted(isReviewVoted: Boolean)

    suspend fun insertChats(chats: List<Chat>)

    suspend fun insertMessages(messages: List<Message>)

    suspend fun getChats(): Flow<List<Chat>>

    suspend fun updateChats(chats: List<Chat>)

    fun updateRemoteChats(chats: List<Chat>): Flow<NetworkResult<Unit>>

    suspend fun insertChat(chat: Chat)

    fun insertRemoteChat(chat: Chat): Flow<NetworkResult<Unit>>

    suspend fun updateChat(chat: Chat)

    fun updateRemoteChat(chat: Chat): Flow<NetworkResult<Unit>>

    suspend fun deleteChat(chat: Chat)

    fun deleteRemoteChat(chat: Chat): Flow<NetworkResult<Unit>>

    suspend fun updateMessages(messages: List<Message>)
}