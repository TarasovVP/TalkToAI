package com.vnstudio.talktoai.data.repositoryimpl

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.vnstudio.talktoai.domain.models.Feedback
import com.vnstudio.talktoai.CommonExtensions.isTrue
import com.vnstudio.talktoai.data.database.db_entities.Chat
import com.vnstudio.talktoai.data.database.db_entities.Message
import com.vnstudio.talktoai.domain.models.CurrentUser
import com.vnstudio.talktoai.domain.repositories.RealDataBaseRepository
import com.vnstudio.talktoai.data.network.Result
import com.vnstudio.talktoai.infrastructure.Constants.CHATS
import com.vnstudio.talktoai.infrastructure.Constants.MESSAGES
import com.vnstudio.talktoai.infrastructure.Constants.REVIEW_VOTE
import com.vnstudio.talktoai.infrastructure.Constants.USERS
import com.vnstudio.talktoai.infrastructure.Constants.FEEDBACK
import com.vnstudio.talktoai.infrastructure.Constants.PRIVACY_POLICY
import javax.inject.Inject

class RealDataBaseRepositoryImpl @Inject constructor(private val firebaseDatabase: FirebaseDatabase, private val firebaseAuth: FirebaseAuth) :
    RealDataBaseRepository {

    override fun createCurrentUser(currentUser: CurrentUser, result: (Result<Unit>) -> Unit) {
        val currentUserMap = hashMapOf<String, Any>()
        currentUser.chatList.forEach { chat ->
            currentUserMap["$CHATS/${chat.chatId}"] = chat
        }
        currentUser.messageList.forEach { message ->
            currentUserMap["$MESSAGES/${message.messageId}"] = message
        }
        firebaseDatabase.reference.child(USERS).child(firebaseAuth.currentUser?.uid.orEmpty()).setValue(currentUser)
            .addOnSuccessListener {
                result.invoke(Result.Success())
            }.addOnFailureListener { exception ->
                result.invoke(Result.Failure(exception.localizedMessage))
            }
    }

    override fun updateCurrentUser(currentUser: CurrentUser, result: (Result<Unit>) -> Unit) {
        val updatesMap = hashMapOf<String, Any>()
        currentUser.chatList.forEach { chat ->
            updatesMap["$CHATS/${chat.chatId}"] = chat
        }
        currentUser.messageList.forEach { message ->
            updatesMap["$MESSAGES/${message.messageId}"] = message
        }
        firebaseDatabase.reference.child(USERS).child(firebaseAuth.currentUser?.uid.orEmpty()).updateChildren(updatesMap)
            .addOnSuccessListener {
                result.invoke(Result.Success())
            }.addOnFailureListener { exception ->
                result.invoke(Result.Failure(exception.localizedMessage))
            }
    }

    override fun getCurrentUser(result: (Result<CurrentUser>) -> Unit) {
        var currentUserDatabase = firebaseDatabase.reference.child(USERS).child(firebaseAuth.currentUser?.uid.orEmpty())
        if (currentUserDatabase.key != firebaseAuth.currentUser?.uid.orEmpty()) currentUserDatabase =
            firebaseDatabase.reference.child(USERS).child(firebaseAuth.currentUser?.uid.orEmpty())
        currentUserDatabase.get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val currentUser = CurrentUser()
                    task.result.children.forEach { snapshot ->
                        when (snapshot.key) {
                            CHATS -> snapshot.children.forEach { child ->
                                child.getValue(Chat::class.java)?.let { currentUser.chatList.add(it) }
                            }
                            MESSAGES -> snapshot.children.forEach { child ->
                                child.getValue(Message::class.java)?.let { currentUser.messageList.add(it) }
                            }
                            REVIEW_VOTE -> currentUser.isReviewVoted = snapshot.getValue(Boolean::class.java).isTrue()
                        }
                    }
                    result.invoke(Result.Success(currentUser))
                }
            }.addOnFailureListener { exception ->
                result.invoke(Result.Failure(exception.localizedMessage))
            }
    }

    override fun deleteCurrentUser(result: (Result<Unit>) -> Unit) {
        firebaseDatabase.reference.child(USERS).child(firebaseAuth.currentUser?.uid.orEmpty()).removeValue()
            .addOnSuccessListener {
                result.invoke(Result.Success())
            }.addOnFailureListener { exception ->
                result.invoke(Result.Failure(exception.localizedMessage))
            }
    }

    override fun insertChat(chat: Chat, result: (Result<Unit>) -> Unit) {
        firebaseDatabase.reference.child(USERS).child(firebaseAuth.currentUser?.uid.orEmpty()).child(
            CHATS).child(chat.chatId.toString()).setValue(chat)
            .addOnSuccessListener {
                result.invoke(Result.Success())
            }.addOnFailureListener { exception ->
                result.invoke(Result.Failure(exception.localizedMessage))
            }
    }

    override fun updateChat(chat: Chat, result: (Result<Unit>) -> Unit) {
        firebaseDatabase.reference.child(USERS).child(firebaseAuth.currentUser?.uid.orEmpty()).child(CHATS).setValue(chat)
            .addOnSuccessListener {
                result.invoke(Result.Success())
            }.addOnFailureListener { exception ->
                result.invoke(Result.Failure(exception.localizedMessage))
            }
    }

    override fun deleteChat(chat: Chat, result: (Result<Unit>) -> Unit) {
        firebaseDatabase.reference.child(USERS).child(firebaseAuth.currentUser?.uid.orEmpty()).child(CHATS).child(chat.chatId.toString()).removeValue()
            .addOnCompleteListener {
                result.invoke(Result.Success())
            }.addOnFailureListener { exception ->
                result.invoke(Result.Failure(exception.localizedMessage))
            }
    }

    override fun insertMessage(message: Message, result: (Result<Unit>) -> Unit) {
        firebaseDatabase.reference.child(USERS).child(firebaseAuth.currentUser?.uid.orEmpty()).child(MESSAGES).child(message.messageId.toString()).setValue(message)
            .addOnSuccessListener {
                result.invoke(Result.Success())
            }.addOnFailureListener { exception ->
                result.invoke(Result.Failure(exception.localizedMessage))
            }
    }

    override fun deleteMessageList(messageIdList: List<String>, result: (Result<Unit>) -> Unit) {
        firebaseDatabase.reference.child(USERS).child(firebaseAuth.currentUser?.uid.orEmpty()).child(MESSAGES).get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    task.result.children.forEach { snapshot ->
                        if (messageIdList.contains(snapshot.key)) snapshot.ref.removeValue()
                    }
                    result.invoke(Result.Success())
                }
            }.addOnFailureListener { exception ->
                result.invoke(Result.Failure(exception.localizedMessage))
            }
    }

    override fun setReviewVoted(result: (Result<Unit>) -> Unit) {
        firebaseDatabase.reference.child(USERS).child(firebaseAuth.currentUser?.uid.orEmpty()).child(REVIEW_VOTE).setValue(true)
            .addOnSuccessListener {
                result.invoke(Result.Success())
            }.addOnFailureListener { exception ->
                result.invoke(Result.Failure(exception.localizedMessage))
            }
    }

    override fun getPrivacyPolicy(appLang: String, result: (Result<String>) -> Unit) {
        firebaseDatabase.reference.child(PRIVACY_POLICY).child(appLang).get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) result.invoke(Result.Success(task.result.value as? String))
            }.addOnFailureListener { exception ->
                result.invoke(Result.Failure(exception.localizedMessage))
            }
    }

    override fun insertFeedback(feedback: Feedback, result: (Result<Unit>) -> Unit) {
        firebaseDatabase.reference.child(FEEDBACK).child(feedback.time.toString()).setValue(feedback)
            .addOnSuccessListener {
                result.invoke(Result.Success())
            }.addOnFailureListener { exception ->
                result.invoke(Result.Failure(exception.localizedMessage))
            }
    }
}