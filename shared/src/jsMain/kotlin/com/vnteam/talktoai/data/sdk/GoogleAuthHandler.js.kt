package com.vnteam.talktoai.data.sdk

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual class GoogleAuthHandler {
    init {
        js("""
            google.accounts.id.initialize({
                client_id: 'YOUR_CLIENT_ID',
                callback: handleCredentialResponse
            });
        """)
    }

    actual fun signIn() {
        js("google.accounts.id.prompt()")
    }

    actual fun signOut() {
        js("localStorage.removeItem('google_token')")
    }

    actual fun getToken(): String? {
        return js("localStorage.getItem('google_token')") as? String
    }
}

private external fun handleCredentialResponse(response: dynamic)

@JsName("handleCredentialResponse")
fun handleCredentialResponseImpl(response: dynamic) {
    js("localStorage.setItem('google_token', response.credential)")
    println("Token received: ${response.credential}")
}