package com.vnteam.talktoai.presentation.uistates

data class LoginUIState(
    var isAccountExist: Boolean? = null,
    val isEmailAccountExist: Boolean? = null,
    val isGoogleAccountExist: String? = null,
    val successPasswordReset: Boolean? = null,
    val successSignIn: Boolean? = null
)