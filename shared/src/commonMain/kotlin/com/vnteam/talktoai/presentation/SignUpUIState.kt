package com.vnteam.talktoai.presentation

data class SignUpUIState(
    var accountExist: Boolean? = null,
    val createEmailAccount: Boolean? = null,
    val createGoogleAccount: String? = null,
    val successSignUp: Boolean? = null,
    val createCurrentUser: Boolean? = null
)