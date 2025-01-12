package com.vnteam.talktoai.domain.usecase

import com.vnteam.talktoai.data.network.Result
import com.vnteam.talktoai.domain.models.Chat
import com.vnteam.talktoai.domain.models.Message
import kotlinx.coroutines.flow.Flow

interface MainUseCase {

    fun addAuthStateListener()

    fun removeAuthStateListener()

    fun isLoggedInUser(): Boolean

    fun isAuthorisedUser(): Boolean

    fun addRemoteChatListener()

    fun addRemoteMessageListener()

    fun removeRemoteChatListener()

    fun removeRemoteMessageListener()

    suspend fun insertChats(chats: List<Chat>)

    suspend fun insertMessages(messages: List<Message>)

    suspend fun getChats(): Flow<Result<List<Chat>>>

    suspend fun updateChats(chats: List<Chat>)

    fun updateRemoteChats(chats: List<Chat>): Flow<Result<Unit>>

    suspend fun insertChat(chat: Chat)

    fun insertRemoteChat(chat: Chat): Flow<Result<Unit>>

    suspend fun updateChat(chat: Chat)

    fun updateRemoteChat(chat: Chat): Flow<Result<Unit>>

    suspend fun deleteChat(chat: Chat)

    fun deleteRemoteChat(chat: Chat): Flow<Result<Unit>>

    suspend fun updateMessages(messages: List<Message>)
}