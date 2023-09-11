package com.vnstudio.talktoai.presentation.screens.main

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ValueEventListener
import com.vnstudio.talktoai.data.database.db_entities.Chat
import com.vnstudio.talktoai.data.database.db_entities.Message
import com.vnstudio.talktoai.domain.repositories.*
import com.vnstudio.talktoai.domain.sealed_classes.Result
import com.vnstudio.talktoai.domain.usecases.MainUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MainUseCaseImpl @Inject constructor(
    private val authRepository: AuthRepository,
    private val dataStoreRepository: DataStoreRepository,
    private val realDataBaseRepository: RealDataBaseRepository,
    private val chatRepository: ChatRepository,
    private val messageRepository: MessageRepository,
) : MainUseCase {

    override suspend fun getOnBoardingSeen(): Flow<Boolean?> = dataStoreRepository.onBoardingSeen()

    override fun addAuthStateListener(authStateListener: FirebaseAuth.AuthStateListener) = authRepository.addAuthStateListener(authStateListener)

    override fun removeAuthStateListener(authStateListener: FirebaseAuth.AuthStateListener) = authRepository.removeAuthStateListener(authStateListener)

    override fun isLoggedInUser() = authRepository.isLoggedInUser()

    override fun isAuthorisedUser() = authRepository.isAuthorisedUser()

    override fun addRemoteChatListener(remoteChatListener: ValueEventListener) {
        realDataBaseRepository.addRemoteChatListener(remoteChatListener)
    }

    override fun addRemoteMessageListener(remoteMessageListener: ValueEventListener) {
        realDataBaseRepository.addRemoteMessageListener(remoteMessageListener)
    }

    override fun removeRemoteChatListener(remoteChatListener: ValueEventListener) {
        realDataBaseRepository.removeRemoteChatListener(remoteChatListener)
    }

    override fun removeRemoteMessageListener(remoteMessageListener: ValueEventListener) {
        realDataBaseRepository.removeRemoteMessageListener(remoteMessageListener)
    }

    override suspend fun setReviewVoted(isReviewVoted: Boolean) =
        dataStoreRepository.setReviewVoted(isReviewVoted)

    override suspend fun insertChats(chats: List<Chat>) = chatRepository.insertChats(chats)

    override suspend fun insertMessages(messages: List<Message>) = messageRepository.insertMessages(messages)

    override suspend fun getChats(): Flow<List<Chat>> = chatRepository.getChats()

    override suspend fun updateChats(chats: List<Chat>) = chatRepository.updateChats(chats)

    override fun updateRemoteChats(chats: List<Chat>, result: (Result<Unit>) -> Unit) = realDataBaseRepository.updateRemoteChats(chats, result)

    override suspend fun insertChat(chat: Chat) = chatRepository.insertChat(chat)

    override fun insertRemoteChat(chat: Chat, result: (Result<Unit>) -> Unit) = realDataBaseRepository.insertChat(chat, result)

    override suspend fun updateChat(chat: Chat) = chatRepository.updateChat(chat)

    override fun updateRemoteChat(chat: Chat, result: (Result<Unit>) -> Unit) = realDataBaseRepository.updateChat(chat, result)

    override suspend fun deleteChat(chat: Chat) {
        chatRepository.deleteChat(chat)
        messageRepository.deleteMessagesFromChat(chat.id)
    }

    override fun deleteRemoteChat(chat: Chat, result: (Result<Unit>) -> Unit) {
        realDataBaseRepository.deleteMessagesByChatId(chat.id) {
            realDataBaseRepository.deleteChat(chat, result)
        }
    }

    override suspend fun updateMessages(messages: List<Message>) = messageRepository.updateMessages(messages)
}