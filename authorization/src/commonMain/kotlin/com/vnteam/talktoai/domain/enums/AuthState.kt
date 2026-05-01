package com.vnteam.talktoai.domain.enums

enum class AuthState {
    UNAUTHORISED,
    AUTHORISED_ANONYMOUSLY,
    AUTHORISED_EMAIL
}

fun AuthState?.isAuthorisedUser(): Boolean {
    return this == AuthState.AUTHORISED_EMAIL
}
