package com.vnteam.talktoai.data.repositoryimpl

import com.vnteam.talktoai.Constants.CHATS
import com.vnteam.talktoai.Constants.MESSAGES
import com.vnteam.talktoai.data.network.Result
import com.vnteam.talktoai.domain.models.Chat
import com.vnteam.talktoai.domain.models.Feedback
import com.vnteam.talktoai.domain.models.Message
import com.vnteam.talktoai.domain.models.RemoteUser
import com.vnteam.talktoai.domain.repositories.RealDataBaseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class RealDataBaseRepositoryImpl :
    RealDataBaseRepository {

    override fun insertRemoteUser(remoteUser: RemoteUser): Flow<Result<Unit>> = callbackFlow {
        val currentUserMap = hashMapOf<String, Any>()
        remoteUser.chats.forEach { chat ->
            currentUserMap["$CHATS/${chat.id}"] = chat
        }
        remoteUser.messages.forEach { message ->
            currentUserMap["$MESSAGES/${message.id}"] = message
        }

        /*firebaseDatabase.reference.child(USERS).child(firebaseAuth.currentUser?.uid.orEmpty())
            .setValue(remoteUser)
            .addOnSuccessListener {
                result.invoke(Result.Success())
            }.addOnFailureListener { exception ->
                result.invoke(Result.Failure(exception.localizedMessage))
            }*/
    }

    override fun updateRemoteUser(remoteUser: RemoteUser): Flow<Result<Unit>> = callbackFlow {
        val updatesMap = hashMapOf<String, Any>()
        remoteUser.chats.forEach { chat ->
            updatesMap["$CHATS/${chat.id}"] = chat
        }
        remoteUser.messages.forEach { message ->
            updatesMap["$MESSAGES/${message.id}"] = message
        }
        /*firebaseDatabase.reference.child(USERS).child(firebaseAuth.currentUser?.uid.orEmpty())
            .updateChildren(updatesMap)
            .addOnSuccessListener {
                result.invoke(Result.Success())
            }.addOnFailureListener { exception ->
                result.invoke(Result.Failure(exception.localizedMessage))
            }*/
    }

    override fun updateRemoteChats(chats: List<Chat>): Flow<Result<Unit>> = callbackFlow {
        val updatesMap = hashMapOf<String, Any>()
        chats.forEach { chat ->
            updatesMap["$CHATS/${chat.id}"] = chat
        }
        /*firebaseDatabase.reference.child(USERS).child(firebaseAuth.currentUser?.uid.orEmpty())
            .updateChildren(updatesMap)
            .addOnSuccessListener {
                result.invoke(Result.Success())
            }.addOnFailureListener { exception ->
                result.invoke(Result.Failure(exception.localizedMessage))
            }*/
    }

    override fun deleteRemoteUser(): Flow<Result<Unit>> = callbackFlow {
        /*firebaseDatabase.reference.child(USERS).child(firebaseAuth.currentUser?.uid.orEmpty())
            .removeValue()
            .addOnSuccessListener {
                result.invoke(Result.Success())
            }.addOnFailureListener { exception ->
                result.invoke(Result.Failure(exception.localizedMessage))
            }*/
    }

    override fun addRemoteChatListener(/*remoteChatListener: ValueEventListener*/) {
        //firebaseDatabase.reference.child(USERS).child(firebaseAuth.currentUser?.uid.orEmpty()).child(CHATS).addValueEventListener(remoteChatListener)
    }

    override fun addRemoteMessageListener(/*remoteMessageListener: ValueEventListener*/) {
        //firebaseDatabase.reference.child(USERS).child(firebaseAuth.currentUser?.uid.orEmpty()).child(MESSAGES).addValueEventListener(remoteMessageListener)
    }

    override fun removeRemoteChatListener(/*remoteChatListener: ValueEventListener*/) {
        //firebaseDatabase.reference.child(USERS).child(firebaseAuth.currentUser?.uid.orEmpty()).child(CHATS).removeEventListener(remoteChatListener)
    }

    override fun removeRemoteMessageListener(/*remoteMessageListener: ValueEventListener*/) {
        //firebaseDatabase.reference.child(USERS).child(firebaseAuth.currentUser?.uid.orEmpty()).child(MESSAGES).removeEventListener(remoteMessageListener)
    }

    override fun insertChat(chat: Chat): Flow<Result<Unit>> = callbackFlow {
        /*firebaseDatabase.reference.child(USERS).child(firebaseAuth.currentUser?.uid.orEmpty())
            .child(CHATS).child(chat.id.toString()).setValue(chat)
            .addOnSuccessListener {
                result.invoke(Result.Success())
            }.addOnFailureListener { exception ->
                result.invoke(Result.Failure(exception.localizedMessage))
            }*/
    }

    override fun updateChat(chat: Chat): Flow<Result<Unit>> = callbackFlow {
        /*firebaseDatabase.reference.child(USERS).child(firebaseAuth.currentUser?.uid.orEmpty())
            .child(CHATS).child(chat.id.toString()).updateChildren(mapOf("name" to chat.name, "updated" to chat.updated))
            .addOnSuccessListener {
                result.invoke(Result.Success())
            }.addOnFailureListener { exception ->
                result.invoke(Result.Failure(exception.localizedMessage))
            }*/
    }

    override fun deleteChat(chat: Chat): Flow<Result<Unit>> = callbackFlow {
        /*firebaseDatabase.reference.child(USERS).child(firebaseAuth.currentUser?.uid.orEmpty())
            .child(CHATS).child(chat.id.toString()).removeValue()
            .addOnCompleteListener {
                result.invoke(Result.Success())
            }.addOnFailureListener { exception ->
                result.invoke(Result.Failure(exception.localizedMessage))
            }*/
    }

    override fun insertMessage(message: Message): Flow<Result<Unit>> = callbackFlow {
        /*firebaseDatabase.reference.child(USERS).child(firebaseAuth.currentUser?.uid.orEmpty())
            .child(MESSAGES).child(message.id.toString()).setValue(message)
            .addOnSuccessListener {
                result.invoke(Result.Success())
            }.addOnFailureListener { exception ->
                result.invoke(Result.Failure(exception.localizedMessage))
            }*/
    }

    override fun deleteMessages(messageIds: List<String>): Flow<Result<Unit>> = callbackFlow {
        /*firebaseDatabase.reference.child(USERS).child(firebaseAuth.currentUser?.uid.orEmpty())
            .child(MESSAGES).get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    task.result.children.forEach { snapshot ->
                        if (messageIds.contains(snapshot.key)) snapshot.ref.removeValue()
                    }
                    result.invoke(Result.Success())
                }
            }.addOnFailureListener { exception ->
                result.invoke(Result.Failure(exception.localizedMessage))
            }*/
    }

    override fun deleteMessagesByChatId(chatId: Long): Flow<Result<Unit>> = callbackFlow {
        /*firebaseDatabase.reference.child(USERS).child(firebaseAuth.currentUser?.uid.orEmpty())
            .child(MESSAGES).get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    task.result.children.forEach { snapshot ->
                        if (chatId == snapshot.getValue(Message::class.java)?.chatId) snapshot.ref.removeValue()
                    }
                    result.invoke(Result.Success())
                }
            }.addOnFailureListener { exception ->
                result.invoke(Result.Failure(exception.localizedMessage))
            }*/
    }

    override fun setReviewVoted(): Flow<Result<Unit>> = callbackFlow {
        /*firebaseDatabase.reference.child(USERS).child(firebaseAuth.currentUser?.uid.orEmpty())
            .child(REVIEW_VOTE).setValue(true)
            .addOnSuccessListener {
                result.invoke(Result.Success())
            }.addOnFailureListener { exception ->
                result.invoke(Result.Failure(exception.localizedMessage))
            }*/
    }

    override fun getPrivacyPolicy(appLang: String): Flow<Result<String>> = callbackFlow {
        /*firebaseDatabase.reference.child(PRIVACY_POLICY).child(appLang).get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) result.invoke(Result.Success(task.result.value as? String))
            }.addOnFailureListener { exception ->
                result.invoke(Result.Failure(exception.localizedMessage))
            }*/
    }

    override fun insertFeedback(feedback: Feedback): Flow<Result<Unit>> = callbackFlow {
        /*firebaseDatabase.reference.child(FEEDBACK).child(feedback.time.toString())
            .setValue(feedback)
            .addOnSuccessListener {
                result.invoke(Result.Success())
            }.addOnFailureListener { exception ->
                result.invoke(Result.Failure(exception.localizedMessage))
            }*/
    }
}