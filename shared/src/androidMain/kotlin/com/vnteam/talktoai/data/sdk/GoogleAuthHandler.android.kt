package com.vnteam.talktoai.data.sdk

import android.content.Context
import android.content.IntentSender
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.vnteam.talktoai.secrets.Config.ANDROID_CLIENT_ID

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual class GoogleAuthHandler(private val context: Context) {
    private val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestEmail()
        .requestIdToken("919230888719-p0p7fa4tpen4lblghtm50sp4iu2ve5c2.apps.googleusercontent.com")
        .build()

    private val googleSignInClient = GoogleSignIn.getClient(context, gso)

    actual fun signIn() {
        /*val signInIntent = googleSignInClient.signInIntent
        (context as Activity).startActivityForResult(signInIntent, RC_SIGN_IN)*/
        googleSignIn()
    }

    actual fun signOut() {
        googleSignInClient.signOut()
    }

    actual fun getToken(): String? {
        val account = GoogleSignIn.getLastSignedInAccount(context)
        return account?.idToken
    }

    private companion object {
        const val RC_SIGN_IN = 9001
    }

    fun googleSignIn() {
        val oneTapClient = Identity.getSignInClient(context)
        val signInRequest = BeginSignInRequest.builder()
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    .setServerClientId(ANDROID_CLIENT_ID)
                    .setFilterByAuthorizedAccounts(false)
                    .build()
            )
            .build()
        oneTapClient.signOut().addOnCompleteListener {
            println("GoogleSignIn Signed out successfully")
        }
        oneTapClient.beginSignIn(signInRequest)
            .addOnSuccessListener { result ->
                try {
                    //val intentSender = result.pendingIntent.intentSender
                    //startIntentSenderForResult(intentSender, RC_SIGN_IN, null, 0, 0, 0, null)
                    println("GoogleSignIn Success: ${result.pendingIntent.intentSender}")
                } catch (e: IntentSender.SendIntentException) {
                    println("GoogleSignIn SuccessListener Error: ${e.message}")
                }
            }
            .addOnFailureListener { e ->
                println("GoogleSignIn FailureListener Error: ${e.message}")
            }
    }
}