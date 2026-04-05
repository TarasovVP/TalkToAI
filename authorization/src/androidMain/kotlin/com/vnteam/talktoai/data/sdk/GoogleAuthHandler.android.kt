package com.vnteam.talktoai.data.sdk

import android.app.Activity
import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import kotlinx.coroutines.CancellableContinuation
import kotlinx.coroutines.suspendCancellableCoroutine
import secrets.Secrets
import kotlin.coroutines.resume

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual class GoogleAuthHandler {

    private lateinit var googleSignInLauncher: ActivityResultLauncher<Intent>
    private var activity: ComponentActivity? = null
    private var signInContinuation: CancellableContinuation<String?>? = null

    private val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestEmail()
        .requestIdToken(Secrets.ANDROID_CLIENT_ID)
        .build()

    private val googleSignInClient by lazy {
        activity?.let { GoogleSignIn.getClient(it, gso) }
    }

    actual suspend fun signIn(): String? = suspendCancellableCoroutine { continuation ->
        signInContinuation = continuation
        googleSignInClient?.signOut()?.addOnCompleteListener {
            googleSignInClient?.signInIntent?.let { intent ->
                googleSignInLauncher.launch(intent)
            } ?: continuation.resume(null)
        } ?: continuation.resume(null)
        continuation.invokeOnCancellation { signInContinuation = null }
    }

    actual fun signOut() {
        googleSignInClient?.signOut()
    }

    actual fun getToken(): String? {
        val account = activity?.let { GoogleSignIn.getLastSignedInAccount(it) }
        return account?.idToken
    }

    fun setActivity(activity: ComponentActivity) {
        println("googleTAG GoogleAuthHandler setActivity")
        googleSignInLauncher = activity.registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            val token = if (result.resultCode == Activity.RESULT_OK) {
                try {
                    val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                    val account = task.getResult(ApiException::class.java)
                    println("googleTAG GoogleAuthHandler Success! ID Token: ${account?.idToken}")
                    account?.idToken
                } catch (e: ApiException) {
                    println("googleTAG GoogleAuthHandler Failed: statusCode=${e.statusCode} message=${e.message}")
                    null
                }
            } else {
                println("googleTAG GoogleAuthHandler cancelled, resultCode: ${result.resultCode}")
                null
            }
            signInContinuation?.resume(token)
            signInContinuation = null
        }
        this.activity = activity
    }
}
