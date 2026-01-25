package com.vnteam.talktoai.presentation.uistates

import com.vnteam.talktoai.domain.models.RemoteUser

data class SettingsSignUpUIState(
    var accountExist: String? = null,
    val createEmailAccount: Boolean? = null,
    val createGoogleAccount: String? = null,
    val successAuthorisation: Boolean? = null,
    val createCurrentUser: Boolean? = null,
    val remoteUser: Pair<Boolean, RemoteUser?>? = null,
    val successRemoteUser: Boolean? = null,
)