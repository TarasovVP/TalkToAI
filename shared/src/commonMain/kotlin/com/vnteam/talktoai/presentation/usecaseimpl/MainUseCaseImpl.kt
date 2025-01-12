package com.vnteam.talktoai.presentation.usecaseimpl

import com.vnteam.talktoai.data.network.Result
import com.vnteam.talktoai.domain.models.Chat
import com.vnteam.talktoai.domain.models.Message
import com.vnteam.talktoai.domain.repositories.AuthRepository
import com.vnteam.talktoai.domain.repositories.ChatRepository
import com.vnteam.talktoai.domain.repositories.MessageRepository
import com.vnteam.talktoai.domain.repositories.RealDataBaseRepository
import com.vnteam.talktoai.domain.usecase.MainUseCase
import kotlinx.coroutines.flow.Flow

class MainUseCaseImpl(
    private val authRepository: AuthRepository,
    private val realDataBaseRepository: RealDataBaseRepository,
    private val chatRepository: ChatRepository,
    private val messageRepository: MessageRepository,
) : MainUseCase {

    override fun addRemoteChatListener() {
        //realDataBaseRepository.addRemoteChatListener(remoteChatListener)
    }

    override fun addRemoteMessageListener() {
        //realDataBaseRepository.addRemoteMessageListener(remoteMessageListener)
    }

    override fun removeRemoteChatListener() {
        //realDataBaseRepository.removeRemoteChatListener(remoteChatListener)
    }

    override fun removeRemoteMessageListener() {
        //realDataBaseRepository.removeRemoteMessageListener(remoteMessageListener)
    }

    override suspend fun insertChats(chats: List<Chat>) = chatRepository.insertChats(chats)

    override suspend fun insertMessages(messages: List<Message>) =
        messageRepository.insertMessages(messages)

    override suspend fun getChats(): Flow<Result<List<Chat>>> = chatRepository.getChats()

    override suspend fun updateChats(chats: List<Chat>) = chatRepository.updateChats(chats)

    override fun updateRemoteChats(chats: List<Chat>): Flow<Result<Unit>> =
        realDataBaseRepository.updateRemoteChats(chats)

    override suspend fun insertChat(chat: Chat) = chatRepository.insertChat(chat)

    override fun insertRemoteChat(chat: Chat): Flow<Result<Unit>> =
        realDataBaseRepository.insertChat(chat)

    override suspend fun updateChat(chat: Chat) = chatRepository.updateChat(chat)

    override fun updateRemoteChat(chat: Chat): Flow<Result<Unit>> =
        realDataBaseRepository.updateChat(chat)

    override suspend fun deleteChat(chat: Chat) {
        chatRepository.deleteChat(chat)
        messageRepository.deleteMessagesFromChat(chat.id)
    }

    override fun deleteRemoteChat(chat: Chat): Flow<Result<Unit>> {
        return realDataBaseRepository.deleteMessagesByChatId(chat.id)/* {
            realDataBaseRepository.deleteChat(chat)
        }*/
    }

    override suspend fun updateMessages(messages: List<Message>) =
        messageRepository.updateMessages(messages)
}