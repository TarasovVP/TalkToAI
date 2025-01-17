package com.vnteam.talktoai.data.repositoryimpl

import com.vnteam.talktoai.data.ANONYMOUS_USER
import com.vnteam.talktoai.domain.repositories.AuthRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch

class AuthRepositoryImpl :
    AuthRepository {

    override fun addAuthStateListener(/*authStateListener: AuthStateListener*/) {
        //firebaseAuth.addAuthStateListener(authStateListener)
    }

    override fun removeAuthStateListener(/*authStateListener: AuthStateListener*/) {
        //firebaseAuth.removeAuthStateListener(authStateListener)
    }

    override fun isGoogleAuthUser(): Boolean {
        /*firebaseAuth.currentUser?.providerData?.forEach {
            if (it.providerId == GoogleAuthProvider.PROVIDER_ID) return true
        }
        return false*/
        return false
    }

    override fun resetPassword(email: String): Flow<Unit> = callbackFlow {
        /*firebaseAuth.sendPasswordResetEmail(email)
            .addOnSuccessListener {
                result.invoke(Result.Success())
            }.addOnFailureListener { exception ->
                result.invoke(Result.Failure(exception.localizedMessage))
            }*/
    }

    override fun fetchSignInMethodsForEmail(email: String): Flow<List<String>> =
        callbackFlow {
            /*firebaseAuth.fetchSignInMethodsForEmail(email)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        result.invoke(Result.Success(task.result?.signInMethods))
                    }
                }.addOnFailureListener { exception ->
                    result.invoke(Result.Failure(exception.localizedMessage))
                }*/
        }

    override fun signInWithEmailAndPassword(
        email: String,
        password: String
    ): Flow<String> = callbackFlow {
        /*firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                result.invoke(Result.Success())
            }.addOnFailureListener { exception ->
                result.invoke(Result.Failure(exception.localizedMessage))
            }*/
    }

    override fun signInWithGoogle(idToken: String): Flow<String> = callbackFlow {
        /*val credential = GoogleAuthProvider.getCredential(idToken, null)
        firebaseAuth.signInWithCredential(credential)
            .addOnSuccessListener {
                result.invoke(Result.Success())
            }.addOnFailureListener { exception ->
                result.invoke(Result.Failure(exception.localizedMessage))
            }*/
    }

    override fun signInAnonymously(): Flow<String> = callbackFlow {
        // TODO remove mock, uncomment below code

        CoroutineScope(coroutineContext).launch {
            trySend(ANONYMOUS_USER)
            close()
        }

        awaitClose {}
        /*firebaseAuth.signInAnonymously()
            .addOnSuccessListener {
                trySend(NetworkResult.Success(Unit))
                close()
            }.addOnFailureListener { exception ->
                trySend(NetworkResult.Failure(exception.localizedMessage))
                close()
            }*/
    }

    override fun createUserWithEmailAndPassword(
        email: String,
        password: String
    ): Flow<List<String>> = callbackFlow {
        /*firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) result.invoke(Result.Success(task.result.user?.uid.orEmpty()))
            }.addOnFailureListener { exception ->
                result.invoke(Result.Failure(exception.localizedMessage))
            }*/
    }

    override fun changePassword(
        currentPassword: String,
        newPassword: String): Flow<Unit> = callbackFlow {
        /*val user = firebaseAuth.currentUser
        val credential = EmailAuthProvider.getCredential(user?.email.orEmpty(), currentPassword)
        user?.reauthenticateAndRetrieveData(credential)
            ?.addOnSuccessListener {
                user.updatePassword(newPassword).addOnSuccessListener {
                    result.invoke(Result.Success())
                }.addOnFailureListener { exception ->
                    result.invoke(Result.Failure(exception.localizedMessage))
                }
            }?.addOnFailureListener { exception ->
                result.invoke(Result.Failure(exception.localizedMessage))
            }*/
    }

    override fun reAuthenticate(/*authCredential: AuthCredential*/): Flow<Unit> = callbackFlow {
        /*firebaseAuth.currentUser?.reauthenticate(authCredential)
            ?.addOnSuccessListener {
                result.invoke(Result.Success())
            }?.addOnFailureListener { exception ->
                result.invoke(Result.Failure(exception.localizedMessage))
            }*/
    }

    override fun deleteUser(): Flow<Unit> = callbackFlow {
        /*firebaseAuth.currentUser?.delete()
            ?.addOnSuccessListener {
                signOut(result)
            }?.addOnFailureListener { exception ->
                result.invoke(Result.Failure(exception.localizedMessage))
            }*/
    }

    override fun signOut(): Flow<Unit> = callbackFlow {
        /*googleSignInClient.signOut().addOnSuccessListener {
            firebaseAuth.signOut()
            result.invoke(Result.Success())
        }.addOnFailureListener { exception ->
            result.invoke(Result.Failure(exception.localizedMessage))
        }*/
    }

    override fun googleSignOut() {
        // TODO implement
    }

    override fun googleSignIn() {
        // TODO implement
    }
}