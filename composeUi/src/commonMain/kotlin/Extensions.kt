package com.vnstudio.talktoai.infrastructure
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.intl.LocaleList
import com.vnteam.talktoai.Constants

fun LocaleList.flagDrawable(): String {
    return when (if (isEmpty()) Locale.current.language else get(0).language) {
        Constants.APP_LANG_UK -> "ic_flag_ua"
        Constants.APP_LANG_RU -> "ic_flag_ru"
        else -> "ic_flag_en"
    }
}

