package com.vnteam.talktoai.domain.enums

enum class AuthState {
    UNAUTHORISED,
    AUTHORISED_ANONYMOUSLY,
    AUTHORISED_EMAIL,
    AUTHORISED_GOOGLE
}

fun AuthState?.isAuthorisedUser(): Boolean {
    return this == AuthState.AUTHORISED_EMAIL || this == AuthState.AUTHORISED_GOOGLE
}