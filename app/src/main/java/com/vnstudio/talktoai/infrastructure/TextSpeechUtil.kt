package com.vnstudio.talktoai.infrastructure

import android.content.Context
import android.speech.tts.TextToSpeech
import android.util.Log
import java.util.Locale

object TextSpeechUtil {

    //TODO
    fun Context.iniTextToSpeech(successInit: (TextToSpeech) -> Unit) {
        var textToSpeech: TextToSpeech? = null
        val textToSpeechInitListener = TextToSpeech.OnInitListener { status ->
            if (status == TextToSpeech.SUCCESS) {
                val result = textToSpeech?.setLanguage(Locale(Constants.APP_LANG_RU))

                if (result == TextToSpeech.LANG_MISSING_DATA ||
                    result == TextToSpeech.LANG_NOT_SUPPORTED
                ) {
                    Log.e(
                        "textToSpeechTAG",
                        "ChatComposable iniTextToSpeech language not supported error"
                    )
                    // Handle language not supported error
                } else {
                    Log.e(
                        "textToSpeechTAG",
                        "ChatComposable iniTextToSpeech initialization success"
                    )
                    textToSpeech?.let { successInit.invoke(it) }
                }
            } else {
                Log.e(
                    "textToSpeechTAG",
                    "ChatComposable iniTextToSpeech TTS initialization failure"
                )
                // Handle TTS initialization failure
            }
        }
        textToSpeech = TextToSpeech(this, textToSpeechInitListener)
    }
}