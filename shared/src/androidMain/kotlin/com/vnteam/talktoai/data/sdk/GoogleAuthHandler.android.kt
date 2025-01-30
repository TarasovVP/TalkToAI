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
    private var activity: Activity? = null

    private val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestEmail()
        .requestIdToken(Config.ANDROID_CLIENT_ID)
        .build()

    private val googleSignInClient by lazy {
        activity?.let { GoogleSignIn.getClient(it, gso) }
    }

    actual fun signIn() {
        println("googleTAG GoogleAuthHandler googleSignInClient.signInIntent: ${googleSignInClient?.signInIntent}")
        googleSignInClient?.signOut()?.addOnCompleteListener {
            googleSignInClient?.signInIntent?.let { googleSignInLauncher.launch(it) }
        }
    }

    actual fun signOut() {
        googleSignInClient?.signOut()
    }

    actual fun getToken(): String? {
        val account = activity?.let { GoogleSignIn.getLastSignedInAccount(it) }
        return account?.idToken
    }

    fun setActivity(activity: ComponentActivity) {
        println("googleTAG MainActivity onCreate lifecycle: ${activity.lifecycle.currentState}")
        googleSignInLauncher = activity.registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            val extras = result.data?.extras
            if (extras != null) {
                val extrasMap = extras.keySet().associateWith { extras.get(it) }
                println("googleTAG GoogleAuthHandler extras: $extrasMap")
            }
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
        this.activity = activity
    }
}