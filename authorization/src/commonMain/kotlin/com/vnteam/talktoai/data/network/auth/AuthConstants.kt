package com.vnteam.talktoai.data.network.auth

object AuthConstants {
    object OAuth {
        const val GRANT_TYPE = "grant_type"
        // GRANT_TYPE_REFRESH_TOKEN is the grant_type value; REFRESH_TOKEN_KEY is the form param name — both happen to equal "refresh_token" per RFC 6749.
        const val GRANT_TYPE_REFRESH_TOKEN = "refresh_token"
        const val REFRESH_TOKEN_KEY = "refresh_token"
    }

    object Endpoints {
        const val SECURE_TOKEN_URL = "https://securetoken.googleapis.com/v1/token"
        const val ACCOUNT_CREATE_AUTH_URI = "/v1/accounts:createAuthUri"
        const val ACCOUNT_SIGN_IN_WITH_PASSWORD = "/v1/accounts:signInWithPassword"
        const val ACCOUNT_SEND_OOB_CODE = "/v1/accounts:sendOobCode"
        const val ACCOUNT_SIGN_UP = "/v1/accounts:signUp"
        const val ACCOUNT_UPDATE = "/v1/accounts:update"
        const val ACCOUNT_DELETE = "/v1/accounts:delete"
    }

    object Firebase {
        const val CONTINUE_URI_LOCALHOST = "https://localhost"
        const val REQUEST_TYPE_PASSWORD_RESET = "PASSWORD_RESET"
    }
}
