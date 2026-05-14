package com.vnteam.talktoai.data.repositoryimpl

import com.vnteam.talktoai.Constants.CHATS
import com.vnteam.talktoai.Constants.FEEDBACK
import com.vnteam.talktoai.Constants.MESSAGES
import com.vnteam.talktoai.Constants.PRIVACY_POLICY
import com.vnteam.talktoai.Constants.USERS
import com.vnteam.talktoai.data.network.Result
import com.vnteam.talktoai.data.network.firestore.FirestoreCollectionSelector
import com.vnteam.talktoai.data.network.firestore.FirestoreDocument
import com.vnteam.talktoai.data.network.firestore.FirestoreFieldFilter
import com.vnteam.talktoai.data.network.firestore.FirestoreFieldReference
import com.vnteam.talktoai.data.network.firestore.FirestoreFilter
import com.vnteam.talktoai.data.network.firestore.FirestoreQuery
import com.vnteam.talktoai.data.network.firestore.FirestoreService
import com.vnteam.talktoai.data.network.firestore.FirestoreStructuredQuery
import com.vnteam.talktoai.data.network.firestore.FirestoreValue
import com.vnteam.talktoai.data.network.firestore.firestoreBool
import com.vnteam.talktoai.data.network.firestore.firestoreDouble
import com.vnteam.talktoai.data.network.firestore.firestoreInt
import com.vnteam.talktoai.data.network.firestore.firestoreString
import com.vnteam.talktoai.domain.enums.MessageStatus
import com.vnteam.talktoai.domain.models.Chat
import com.vnteam.talktoai.domain.models.Feedback
import com.vnteam.talktoai.domain.models.Message
import com.vnteam.talktoai.domain.models.RemoteUser
import com.vnteam.talktoai.domain.repositories.PreferencesRepository
import com.vnteam.talktoai.domain.repositories.RealDataBaseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow

class RealDataBaseRepositoryImpl(
    private val firestoreService: FirestoreService,
    private val preferencesRepository: PreferencesRepository,
) : RealDataBaseRepository {

    private suspend fun idToken(): String {
        val token = preferencesRepository.getIdToken().firstOrNull().orEmpty()
        println("firestoreTAG idToken: length=${token.length} empty=${token.isEmpty()}")
        return token
    }

    private suspend fun uid(): String {
        val uid = preferencesRepository.getUid().firstOrNull().orEmpty()
        println("firestoreTAG uid: value='$uid' empty=${uid.isEmpty()}")
        return uid
    }

    private fun userChatsPath(uid: String) = "$USERS/$uid/$CHATS"
    private fun userMessagesPath(uid: String) = "$USERS/$uid/$MESSAGES"

    // ---- Chat extensions ----

    private fun Chat.toFields(): Map<String, FirestoreValue> = mapOf(
        "id" to firestoreInt(id),
        "name" to firestoreString(name),
        "updated" to firestoreInt(updated),
        "listOrder" to firestoreInt(listOrder),
        "aiModel" to firestoreString(aiModel),
        "temperature" to firestoreDouble(temperature?.toDouble()),
        "context" to firestoreString(context),
    )

    private fun FirestoreDocument.toChat(): Chat? {
        val f = fields ?: return null
        return Chat(
            id = f["id"]?.integerValue?.toLongOrNull(),
            name = f["name"]?.stringValue,
            updated = f["updated"]?.integerValue?.toLongOrNull(),
            listOrder = f["listOrder"]?.integerValue?.toLongOrNull(),
            aiModel = f["aiModel"]?.stringValue,
            temperature = f["temperature"]?.doubleValue?.toFloat(),
            context = f["context"]?.stringValue,
        )
    }

    // ---- Message extensions ----

    private fun Message.toFields(): Map<String, FirestoreValue> = mapOf(
        "id" to firestoreInt(id),
        "chatId" to firestoreInt(chatId),
        "author" to firestoreString(author),
        "message" to firestoreString(message),
        "updatedAt" to firestoreInt(updatedAt),
        "status" to firestoreString(status?.name),
        "errorMessage" to firestoreString(errorMessage),
        "truncated" to firestoreBool(truncated),
    )

    private fun FirestoreDocument.toMessage(): Message? {
        val f = fields ?: return null
        return Message(
            id = f["id"]?.integerValue?.toLongOrNull(),
            chatId = f["chatId"]?.integerValue?.toLongOrNull(),
            author = f["author"]?.stringValue,
            message = f["message"]?.stringValue,
            updatedAt = f["updatedAt"]?.integerValue?.toLongOrNull(),
            status = f["status"]?.stringValue?.let {
                runCatching { MessageStatus.valueOf(it) }.getOrNull()
            },
            errorMessage = f["errorMessage"]?.stringValue.orEmpty(),
            truncated = f["truncated"]?.booleanValue ?: false,
        )
    }

    // ---- Repository methods ----

    override fun insertRemoteUser(remoteUser: RemoteUser): Flow<Unit> = flow {
        val token = idToken()
        val uid = uid()
        if (token.isEmpty() || uid.isEmpty()) return@flow
        remoteUser.chats.forEach { chat ->
            firestoreService.setDocument("${userChatsPath(uid)}/${chat.id}", chat.toFields(), token)
        }
        remoteUser.messages.forEach { message ->
            firestoreService.setDocument(
                "${userMessagesPath(uid)}/${message.id}",
                message.toFields(),
                token
            )
        }
        emit(Unit)
    }

    override fun updateRemoteUser(remoteUser: RemoteUser): Flow<Unit> = flow {
        val token = idToken()
        val uid = uid()
        if (token.isEmpty() || uid.isEmpty()) return@flow
        remoteUser.chats.forEach { chat ->
            firestoreService.setDocument("${userChatsPath(uid)}/${chat.id}", chat.toFields(), token)
        }
        remoteUser.messages.forEach { message ->
            firestoreService.setDocument(
                "${userMessagesPath(uid)}/${message.id}",
                message.toFields(),
                token
            )
        }
        emit(Unit)
    }

    override fun updateRemoteChats(chats: List<Chat>): Flow<Result<Unit>> = flow {
        val token = idToken()
        val uid = uid()
        if (token.isEmpty() || uid.isEmpty()) {
            emit(Result.Failure("Not authenticated"))
            return@flow
        }
        chats.forEach { chat ->
            firestoreService.setDocument("${userChatsPath(uid)}/${chat.id}", chat.toFields(), token)
        }
        emit(Result.Success(Unit))
    }

    override fun deleteRemoteUser(): Flow<Result<Unit>> = flow {
        val token = idToken()
        val uid = uid()
        if (token.isEmpty() || uid.isEmpty()) {
            emit(Result.Failure("Not authenticated"))
            return@flow
        }
        val chats = firestoreService.listDocuments(userChatsPath(uid), token)
        chats.forEach { doc ->
            doc.name?.substringAfterLast('/')?.let { id ->
                firestoreService.deleteDocument("${userChatsPath(uid)}/$id", token)
            }
        }
        val messages = firestoreService.listDocuments(userMessagesPath(uid), token)
        messages.forEach { doc ->
            doc.name?.substringAfterLast('/')?.let { id ->
                firestoreService.deleteDocument("${userMessagesPath(uid)}/$id", token)
            }
        }
        emit(Result.Success(Unit))
    }

    override fun addRemoteChatListener() = Unit
    override fun addRemoteMessageListener() = Unit
    override fun removeRemoteChatListener() = Unit
    override fun removeRemoteMessageListener() = Unit

    override fun insertChat(chat: Chat): Flow<Result<Unit>> = flow {
        val token = idToken()
        val uid = uid()
        if (token.isEmpty() || uid.isEmpty()) {
            emit(Result.Failure("Not authenticated"))
            return@flow
        }
        println("firestoreTAG insertChat uid=$uid chatId=${chat.id}")
        val ok = firestoreService.setDocument("${userChatsPath(uid)}/${chat.id}", chat.toFields(), token)
        println("firestoreTAG insertChat result=$ok")
        if (ok) emit(Result.Success(Unit)) else emit(Result.Failure("Firestore write failed"))
    }

    override fun updateChat(chat: Chat): Flow<Result<Unit>> = flow {
        val token = idToken()
        val uid = uid()
        if (token.isEmpty() || uid.isEmpty()) {
            emit(Result.Failure("Not authenticated"))
            return@flow
        }
        val ok = firestoreService.setDocument("${userChatsPath(uid)}/${chat.id}", chat.toFields(), token)
        if (ok) emit(Result.Success(Unit)) else emit(Result.Failure("Firestore write failed"))
    }

    override fun deleteChat(chat: Chat): Flow<Result<Unit>> = flow {
        val token = idToken()
        val uid = uid()
        if (token.isEmpty() || uid.isEmpty()) {
            emit(Result.Failure("Not authenticated"))
            return@flow
        }
        firestoreService.deleteDocument("${userChatsPath(uid)}/${chat.id}", token)
        val query = FirestoreStructuredQuery(
            structuredQuery = FirestoreQuery(
                from = listOf(FirestoreCollectionSelector(MESSAGES)),
                where = FirestoreFilter(
                    fieldFilter = FirestoreFieldFilter(
                        field = FirestoreFieldReference("chatId"),
                        op = "EQUAL",
                        value = FirestoreValue(integerValue = chat.id.toString()),
                    )
                )
            )
        )
        val msgDocs = firestoreService.runQuery("$USERS/$uid", query, token)
        msgDocs.forEach { doc ->
            doc.name?.substringAfterLast('/')?.let { id ->
                firestoreService.deleteDocument("${userMessagesPath(uid)}/$id", token)
            }
        }
        emit(Result.Success(Unit))
    }

    override fun insertMessage(message: Message): Flow<Result<Unit>> = flow {
        val token = idToken()
        val uid = uid()
        if (token.isEmpty() || uid.isEmpty()) {
            emit(Result.Failure("Not authenticated"))
            return@flow
        }
        println("firestoreTAG insertMessage uid=$uid msgId=${message.id}")
        val ok = firestoreService.setDocument(
            "${userMessagesPath(uid)}/${message.id}",
            message.toFields(),
            token
        )
        println("firestoreTAG insertMessage result=$ok")
        if (ok) emit(Result.Success(Unit)) else emit(Result.Failure("Firestore write failed"))
    }

    override fun deleteMessages(messageIds: List<String>): Flow<Result<Unit>> = flow {
        val token = idToken()
        val uid = uid()
        if (token.isEmpty() || uid.isEmpty()) {
            emit(Result.Failure("Not authenticated"))
            return@flow
        }
        messageIds.forEach { id ->
            firestoreService.deleteDocument("${userMessagesPath(uid)}/$id", token)
        }
        emit(Result.Success(Unit))
    }

    override fun deleteMessagesByChatId(chatId: Long): Flow<Result<Unit>> = flow {
        val token = idToken()
        val uid = uid()
        if (token.isEmpty() || uid.isEmpty()) {
            emit(Result.Failure("Not authenticated"))
            return@flow
        }
        val query = FirestoreStructuredQuery(
            structuredQuery = FirestoreQuery(
                from = listOf(FirestoreCollectionSelector(MESSAGES)),
                where = FirestoreFilter(
                    fieldFilter = FirestoreFieldFilter(
                        field = FirestoreFieldReference("chatId"),
                        op = "EQUAL",
                        value = FirestoreValue(integerValue = chatId.toString()),
                    )
                )
            )
        )
        val docs = firestoreService.runQuery("$USERS/$uid", query, token)
        docs.forEach { doc ->
            doc.name?.substringAfterLast('/')?.let { id ->
                firestoreService.deleteDocument("${userMessagesPath(uid)}/$id", token)
            }
        }
        emit(Result.Success(Unit))
    }

    override fun setReviewVoted(): Flow<Unit> = flow {
        val token = idToken()
        val uid = uid()
        if (token.isEmpty() || uid.isEmpty()) return@flow
        firestoreService.setDocument(
            "$USERS/$uid/meta/review",
            mapOf("voted" to firestoreBool(true)),
            token
        )
        emit(Unit)
    }

    override fun getPrivacyPolicy(appLang: String): Flow<String> = flow {
        val doc = firestoreService.getDocument("$PRIVACY_POLICY/$appLang", "")
        val text = doc?.fields?.get("text")?.stringValue.orEmpty()
        emit(text)
    }

    override fun insertFeedback(feedback: Feedback): Flow<Unit> = flow {
        val token = idToken()
        val fields = mapOf(
            "user" to firestoreString(feedback.user),
            "message" to firestoreString(feedback.message),
            "time" to firestoreInt(feedback.time),
        )
        firestoreService.setDocument("$FEEDBACK/${feedback.time}", fields, token)
        emit(Unit)
    }

    override fun getFeedbacks(): Flow<List<Feedback>> = flow {
        val token = idToken()
        val docs = firestoreService.listDocuments(FEEDBACK, token)
        val feedbacks = docs.mapNotNull { doc ->
            val f = doc.fields ?: return@mapNotNull null
            Feedback(
                user = f["user"]?.stringValue.orEmpty(),
                message = f["message"]?.stringValue.orEmpty(),
                time = f["time"]?.integerValue?.toLongOrNull() ?: 0L,
            )
        }
        emit(feedbacks)
    }
}
