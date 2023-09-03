package com.vnstudio.talktoai.data.repositoryimpl

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.vnstudio.talktoai.CommonExtensions.isTrue
import com.vnstudio.talktoai.data.database.db_entities.Chat
import com.vnstudio.talktoai.data.database.db_entities.Message
import com.vnstudio.talktoai.domain.models.Feedback
import com.vnstudio.talktoai.domain.models.RemoteUser
import com.vnstudio.talktoai.domain.repositories.RealDataBaseRepository
import com.vnstudio.talktoai.domain.sealed_classes.Result
import com.vnstudio.talktoai.infrastructure.Constants.CHATS
import com.vnstudio.talktoai.infrastructure.Constants.FEEDBACK
import com.vnstudio.talktoai.infrastructure.Constants.MESSAGES
import com.vnstudio.talktoai.infrastructure.Constants.PRIVACY_POLICY
import com.vnstudio.talktoai.infrastructure.Constants.REVIEW_VOTE
import com.vnstudio.talktoai.infrastructure.Constants.USERS
import javax.inject.Inject


class RealDataBaseRepositoryImpl @Inject constructor(
    private val firebaseDatabase: FirebaseDatabase,
    private val firebaseAuth: FirebaseAuth,
) :
    RealDataBaseRepository {

    override fun insertRemoteUser(remoteUser: RemoteUser, result: (Result<Unit>) -> Unit) {
        val currentUserMap = hashMapOf<String, Any>()
        remoteUser.chats.forEach { chat ->
            currentUserMap["$CHATS/${chat.id}"] = chat
        }
        remoteUser.messages.forEach { message ->
            currentUserMap["$MESSAGES/${message.id}"] = message
        }
        firebaseDatabase.reference.child(USERS).child(firebaseAuth.currentUser?.uid.orEmpty())
            .setValue(remoteUser)
            .addOnSuccessListener {
                result.invoke(Result.Success())
            }.addOnFailureListener { exception ->
                result.invoke(Result.Failure(exception.localizedMessage))
            }
    }

    override fun updateRemoteUser(remoteUser: RemoteUser, result: (Result<Unit>) -> Unit) {
        val updatesMap = hashMapOf<String, Any>()
        remoteUser.chats.forEach { chat ->
            updatesMap["$CHATS/${chat.id}"] = chat
        }
        remoteUser.messages.forEach { message ->
            updatesMap["$MESSAGES/${message.id}"] = message
        }
        firebaseDatabase.reference.child(USERS).child(firebaseAuth.currentUser?.uid.orEmpty())
            .updateChildren(updatesMap)
            .addOnSuccessListener {
                result.invoke(Result.Success())
            }.addOnFailureListener { exception ->
                result.invoke(Result.Failure(exception.localizedMessage))
            }
    }

    override fun getRemoteUser(result: (Result<RemoteUser>) -> Unit) {
        var currentUserDatabase =
            firebaseDatabase.reference.child(USERS).child(firebaseAuth.currentUser?.uid.orEmpty())
        if (currentUserDatabase.key != firebaseAuth.currentUser?.uid.orEmpty()) currentUserDatabase =
            firebaseDatabase.reference.child(USERS).child(firebaseAuth.currentUser?.uid.orEmpty())
        currentUserDatabase.get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val remoteUser = RemoteUser()
                    task.result.children.forEach { snapshot ->
                        when (snapshot.key) {
                            CHATS -> snapshot.children.forEach { child ->
                                child.getValue(Chat::class.java)
                                    ?.let { remoteUser.chats.add(it) }
                            }
                            MESSAGES -> snapshot.children.forEach { child ->
                                child.getValue(Message::class.java)
                                    ?.let { remoteUser.messages.add(it) }
                            }
                            REVIEW_VOTE -> remoteUser.isReviewVoted =
                                snapshot.getValue(Boolean::class.java).isTrue()
                        }
                    }
                    result.invoke(Result.Success(remoteUser))
                }
            }.addOnFailureListener { exception ->
                result.invoke(Result.Failure(exception.localizedMessage))
            }
    }

    override fun deleteRemoteUser(result: (Result<Unit>) -> Unit) {
        firebaseDatabase.reference.child(USERS).child(firebaseAuth.currentUser?.uid.orEmpty())
            .removeValue()
            .addOnSuccessListener {
                result.invoke(Result.Success())
            }.addOnFailureListener { exception ->
                result.invoke(Result.Failure(exception.localizedMessage))
            }
    }

    override fun addRemoteChatListener(remoteChatListener: ValueEventListener) {
        firebaseDatabase.reference.child(USERS).child(firebaseAuth.currentUser?.uid.orEmpty()).child(CHATS).addValueEventListener(remoteChatListener)
    }

    override fun addRemoteMessageListener(remoteMessageListener: ValueEventListener) {
        firebaseDatabase.reference.child(USERS).child(firebaseAuth.currentUser?.uid.orEmpty()).child(MESSAGES).addValueEventListener(remoteMessageListener)
    }

    override fun removeRemoteChatListener(remoteChatListener: ValueEventListener) {
        firebaseDatabase.reference.child(USERS).child(firebaseAuth.currentUser?.uid.orEmpty()).child(CHATS).removeEventListener(remoteChatListener)
    }

    override fun removeRemoteMessageListener(remoteMessageListener: ValueEventListener) {
        firebaseDatabase.reference.child(USERS).child(firebaseAuth.currentUser?.uid.orEmpty()).child(MESSAGES).removeEventListener(remoteMessageListener)
    }

    override fun insertChat(chat: Chat, result: (Result<Unit>) -> Unit) {
        firebaseDatabase.reference.child(USERS).child(firebaseAuth.currentUser?.uid.orEmpty())
            .child(
                CHATS
            ).child(chat.id.toString()).setValue(chat)
            .addOnSuccessListener {
                result.invoke(Result.Success())
            }.addOnFailureListener { exception ->
                result.invoke(Result.Failure(exception.localizedMessage))
            }
    }

    override fun updateChat(chat: Chat, result: (Result<Unit>) -> Unit) {
        firebaseDatabase.reference.child(USERS).child(firebaseAuth.currentUser?.uid.orEmpty())
            .child(CHATS).setValue(chat)
            .addOnSuccessListener {
                result.invoke(Result.Success())
            }.addOnFailureListener { exception ->
                result.invoke(Result.Failure(exception.localizedMessage))
            }
    }

    override fun deleteChat(chat: Chat, result: (Result<Unit>) -> Unit) {
        firebaseDatabase.reference.child(USERS).child(firebaseAuth.currentUser?.uid.orEmpty())
            .child(CHATS).child(chat.id.toString()).removeValue()
            .addOnCompleteListener {
                result.invoke(Result.Success())
            }.addOnFailureListener { exception ->
                result.invoke(Result.Failure(exception.localizedMessage))
            }
    }

    override fun insertMessage(message: Message, result: (Result<Unit>) -> Unit) {
        firebaseDatabase.reference.child(USERS).child(firebaseAuth.currentUser?.uid.orEmpty())
            .child(MESSAGES).child(message.id.toString()).setValue(message)
            .addOnSuccessListener {
                result.invoke(Result.Success())
            }.addOnFailureListener { exception ->
                result.invoke(Result.Failure(exception.localizedMessage))
            }
    }

    override fun deleteMessageList(messageIdList: List<String>, result: (Result<Unit>) -> Unit) {
        firebaseDatabase.reference.child(USERS).child(firebaseAuth.currentUser?.uid.orEmpty())
            .child(MESSAGES).get()
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
        firebaseDatabase.reference.child(USERS).child(firebaseAuth.currentUser?.uid.orEmpty())
            .child(REVIEW_VOTE).setValue(true)
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
        firebaseDatabase.reference.child(FEEDBACK).child(feedback.time.toString())
            .setValue(feedback)
            .addOnSuccessListener {
                result.invoke(Result.Success())
            }.addOnFailureListener { exception ->
                result.invoke(Result.Failure(exception.localizedMessage))
            }
    }
}