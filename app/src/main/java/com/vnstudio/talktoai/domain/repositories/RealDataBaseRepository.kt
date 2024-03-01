package com.vnstudio.talktoai.domain.repositories

import com.google.firebase.database.ValueEventListener
import com.vnstudio.talktoai.data.database.db_entities.Chat
import com.vnstudio.talktoai.data.database.db_entities.Message
import com.vnstudio.talktoai.domain.models.Feedback
import com.vnstudio.talktoai.domain.models.RemoteUser
import com.vnstudio.talktoai.domain.sealed_classes.Result

interface RealDataBaseRepository {

    fun insertRemoteUser(remoteUser: RemoteUser, result: (Result<Unit>) -> Unit)

    fun updateRemoteUser(remoteUser: RemoteUser, result: (Result<Unit>) -> Unit)

    fun deleteRemoteUser(result: (Result<Unit>) -> Unit)

    fun updateRemoteChats(chats: List<Chat>, result: (Result<Unit>) -> Unit)

    fun addRemoteChatListener(remoteChatListener: ValueEventListener)

    fun addRemoteMessageListener(remoteMessageListener: ValueEventListener)

    fun removeRemoteChatListener(remoteChatListener: ValueEventListener)

    fun removeRemoteMessageListener(remoteMessageListener: ValueEventListener)

    fun insertChat(chat: Chat, result: (Result<Unit>) -> Unit)

    fun updateChat(chat: Chat, result: (Result<Unit>) -> Unit)

    fun deleteChat(chat: Chat, result: (Result<Unit>) -> Unit)

    fun insertMessage(message: Message, result: (Result<Unit>) -> Unit)

    fun deleteMessages(messageIds: List<String>, result: (Result<Unit>) -> Unit)

    fun deleteMessagesByChatId(chatId: Long, result: (Result<Unit>) -> Unit)

    fun setReviewVoted(result: (Result<Unit>) -> Unit)

    fun insertFeedback(feedback: Feedback, result: (Result<Unit>) -> Unit)

    fun getPrivacyPolicy(appLang: String, result: (Result<String>) -> Unit)
}