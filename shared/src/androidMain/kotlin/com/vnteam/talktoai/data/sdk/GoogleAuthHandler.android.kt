package com.vnteam.talktoai.data.sdk

import android.app.Activity
import android.content.Context
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.vnteam.talktoai.secrets.Config.AUTH_API_KEY

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual class GoogleAuthHandler(private val context: Context) {
    private val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestEmail()
        .requestIdToken(AUTH_API_KEY)
        .build()

    private val googleSignInClient = GoogleSignIn.getClient(context, gso)

    actual fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        (context as Activity).startActivityForResult(signInIntent, RC_SIGN_IN)
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
}