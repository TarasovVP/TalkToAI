package com.vnteam.talktoai

import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.intl.LocaleList
import com.vnteam.talktoai.data.ANONYMOUS_USER
import com.vnteam.talktoai.data.GOOGLE_USER
import com.vnteam.talktoai.domain.enums.AuthState
import org.jetbrains.compose.resources.DrawableResource

object CommonExtensions {

    val String.Companion.EMPTY: String
        get() = ""

    fun Any?.isNull() = this == null

    fun Any?.isNotNull() = this != null

    fun Boolean?.isTrue() = this == true

    fun Boolean?.isNotTrue() = this != true

    fun Long?.orZero() = this ?: 0

    fun LocaleList.flagDrawable(): DrawableResource {
        return when (if (isEmpty()) Locale.current.language else get(0).language) {
            Constants.APP_LANG_UK -> Res.drawable.ic_flag_ua
            else -> Res.drawable.ic_flag_en
        }
    }

    fun String?.getUserAuth(): AuthState {
        return when {
            this.isNullOrEmpty() -> AuthState.UNAUTHORISED
            this == ANONYMOUS_USER -> AuthState.AUTHORISED_ANONYMOUSLY
            this.contains(GOOGLE_USER) -> AuthState.AUTHORISED_GOOGLE
            else -> AuthState.AUTHORISED_EMAIL
        }
    }
}