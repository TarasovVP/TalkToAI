package com.vnteam.talktoai.presentation.uistates

data class LoginUIState(
    val isEmailAccountExist: Boolean? = null,
    val successPasswordReset: Boolean? = null,
    val userLogin: String? = null,
    val anonymousSignInSuccess: Boolean? = null,
    val emailSignInSuccess: Boolean? = null,
)
