package com.vnteam.talktoai.data.sdk

import android.app.Activity
import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.vnteam.talktoai.secrets.Config

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual class GoogleAuthHandler {

    private lateinit var googleSignInLauncher: ActivityResultLauncher<Intent>

    private var activityProvider: () -> ComponentActivity = {
        throw IllegalArgumentException(
            "You need to implement the 'activityProvider' to provide the required Activity. " +
                    "Just make sure to set a valid activity using " +
                    "the 'setActivityProvider()' method."
        )
    }

    fun setActivityProvider(provider: () -> ComponentActivity) {
        activityProvider = provider
    }

    private val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestEmail()
        .requestIdToken(Config.ANDROID_CLIENT_ID)
        .build()

    private val googleSignInClient by lazy {
        GoogleSignIn.getClient(activityProvider.invoke(), gso)
    }

    actual fun signIn() {
        val activity = activityProvider.invoke()
        googleSignInLauncher = activity.registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                try {
                    val account = task.getResult(ApiException::class.java)
                    val idToken = account?.idToken
                    println("googleTAG GoogleAuthHandler Success! ID Token: $idToken")
                } catch (e: ApiException) {
                    println("googleTAG GoogleAuthHandler Failed: ${e.message}")
                }
            }
        }
        googleSignInLauncher.launch(googleSignInClient.signInIntent)
    }

    actual fun signOut() {
        googleSignInClient.signOut()
    }

    actual fun getToken(): String? {
        val account = GoogleSignIn.getLastSignedInAccount(activityProvider.invoke())
        return account?.idToken
    }

    private companion object {
        const val RC_SIGN_IN = 9001
    }
}