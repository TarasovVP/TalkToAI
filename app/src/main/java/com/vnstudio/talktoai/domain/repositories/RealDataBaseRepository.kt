package com.vnstudio.talktoai.domain.repositories

import com.vnstudio.talktoai.domain.models.Feedback
import com.vnstudio.talktoai.data.database.db_entities.Chat
import com.vnstudio.talktoai.data.database.db_entities.Message
import com.vnstudio.talktoai.domain.models.CurrentUser
import com.vnstudio.talktoai.data.network.Result

interface RealDataBaseRepository {

    fun createCurrentUser(currentUser: CurrentUser, result: (Result<Unit>) -> Unit)

    fun updateCurrentUser(currentUser: CurrentUser, result: (Result<Unit>) -> Unit)

    fun getCurrentUser(result: (Result<CurrentUser>) -> Unit)

    fun deleteCurrentUser(result: (Result<Unit>) -> Unit)

    fun insertChat(chat: Chat, result: (Result<Unit>) -> Unit)

    fun updateChat(chat: Chat, result: (Result<Unit>) -> Unit)

    fun deleteChat(chat: Chat, result: (Result<Unit>) -> Unit)

    fun insertMessage(message: Message, result: (Result<Unit>) -> Unit)

    fun deleteMessageList(messageIdList: List<String>, result: (Result<Unit>) -> Unit)

    fun setReviewVoted(result: (Result<Unit>) -> Unit)

    fun insertFeedback(feedback: Feedback, result: (Result<Unit>) -> Unit)

    fun getPrivacyPolicy(appLang: String, result: (Result<String>) -> Unit)
}