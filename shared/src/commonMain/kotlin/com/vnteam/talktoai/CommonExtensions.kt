package com.vnteam.talktoai

import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.intl.LocaleList
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

}