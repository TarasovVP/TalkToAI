package com.vnteam.talktoai.data.repositoryimpl

import com.vnteam.talktoai.CommonExtensions.EMPTY
import com.vnteam.talktoai.domain.repositories.AuthRepository

class AuthRepositoryImpl(
    /*private val firebaseAuth: FirebaseAuth,
    private val googleSignInClient: GoogleSignInClient,*/
) :
    AuthRepository {

    override fun addAuthStateListener(/*authStateListener: AuthStateListener*/) {
        //firebaseAuth.addAuthStateListener(authStateListener)
    }

    override fun removeAuthStateListener(/*authStateListener: AuthStateListener*/) {
        //firebaseAuth.removeAuthStateListener(authStateListener)
    }

    override fun isLoggedInUser(): Boolean {
        return /*firebaseAuth.currentUser.isNotNull()*/true
    }

    override fun isAuthorisedUser(): Boolean {
        return /*firebaseAuth.currentUser.isNotNull() && firebaseAuth.currentUser?.isAnonymous.isNotTrue()*/ true
    }

    override fun isGoogleAuthUser(): Boolean {
        /*firebaseAuth.currentUser?.providerData?.forEach {
            if (it.providerId == GoogleAuthProvider.PROVIDER_ID) return true
        }
        return false*/
        return true
    }

    override fun currentUserEmail(): String {
        /*return try {
            firebaseAuth.currentUser?.email.takeIf { it.isNullOrEmpty().not() }
                ?: firebaseAuth.currentUser?.uid.orEmpty()
        } catch (e: AbstractMethodError) {
            String.EMPTY
        }*/
        return String.EMPTY
    }

    override fun sendPasswordResetEmail(email: String, result: (Result<Unit>) -> Unit) {
        /*firebaseAuth.sendPasswordResetEmail(email)
            .addOnSuccessListener {
                result.invoke(Result.Success())
            }.addOnFailureListener { exception ->
                result.invoke(Result.Failure(exception.localizedMessage))
            }*/
    }

    override fun fetchSignInMethodsForEmail(email: String, result: (Result<List<String>>) -> Unit) {
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
        password: String,
        result: (Result<Unit>) -> Unit,
    ) {
        /*firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                result.invoke(Result.Success())
            }.addOnFailureListener { exception ->
                result.invoke(Result.Failure(exception.localizedMessage))
            }*/
    }

    override fun signInWithGoogle(idToken: String, result: (Result<Unit>) -> Unit) {
        /*val credential = GoogleAuthProvider.getCredential(idToken, null)
        firebaseAuth.signInWithCredential(credential)
            .addOnSuccessListener {
                result.invoke(Result.Success())
            }.addOnFailureListener { exception ->
                result.invoke(Result.Failure(exception.localizedMessage))
            }*/
    }

    override fun signInAnonymously(result: (Result<Unit>) -> Unit) {
        /*firebaseAuth.signInAnonymously()
            .addOnSuccessListener {
                result.invoke(Result.Success())
            }.addOnFailureListener { exception ->
                result.invoke(Result.Failure(exception.localizedMessage))
            }*/
    }

    override fun createUserWithEmailAndPassword(
        email: String,
        password: String,
        result: (Result<String>) -> Unit,
    ) {
        /*firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) result.invoke(Result.Success(task.result.user?.uid.orEmpty()))
            }.addOnFailureListener { exception ->
                result.invoke(Result.Failure(exception.localizedMessage))
            }*/
    }

    override fun changePassword(
        currentPassword: String,
        newPassword: String,
        result: (Result<Unit>) -> Unit,
    ) {
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

    override fun reAuthenticate(/*authCredential: AuthCredential, result: (Result<Unit>) -> Unit*/) {
        /*firebaseAuth.currentUser?.reauthenticate(authCredential)
            ?.addOnSuccessListener {
                result.invoke(Result.Success())
            }?.addOnFailureListener { exception ->
                result.invoke(Result.Failure(exception.localizedMessage))
            }*/
    }

    override fun deleteUser(result: (Result<Unit>) -> Unit) {
        /*firebaseAuth.currentUser?.delete()
            ?.addOnSuccessListener {
                signOut(result)
            }?.addOnFailureListener { exception ->
                result.invoke(Result.Failure(exception.localizedMessage))
            }*/
    }

    override fun signOut(result: (Result<Unit>) -> Unit) {
        /*googleSignInClient.signOut().addOnSuccessListener {
            firebaseAuth.signOut()
            result.invoke(Result.Success())
        }.addOnFailureListener { exception ->
            result.invoke(Result.Failure(exception.localizedMessage))
        }*/
    }
}