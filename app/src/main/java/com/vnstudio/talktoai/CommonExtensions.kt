package com.vnstudio.talktoai

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.Color
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.LocaleList
import android.util.Log
import android.webkit.WebView
import android.webkit.WebViewClient
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.CommonStatusCodes.getStatusCodeString
import com.vnstudio.talktoai.domain.sealed_classes.Result
import com.vnstudio.talktoai.infrastructure.Constants
import com.vnstudio.talktoai.infrastructure.Constants.DARK_MODE_TEXT
import com.vnstudio.talktoai.infrastructure.Constants.ENCODING
import com.vnstudio.talktoai.infrastructure.Constants.MIME_TYPE
import com.vnstudio.talktoai.infrastructure.Constants.WHITE_MODE_TEXT
import com.vnstudio.talktoai.presentation.ui_models.MessageUIModel
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import java.text.SimpleDateFormat
import java.util.*

object CommonExtensions {

    fun Context.registerForNetworkUpdates(isNetworkAvailable: (Boolean) -> Unit) {
        val networkRequest = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
            .build()
        val networkCallback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                isNetworkAvailable.invoke(true)
            }

            override fun onLost(network: Network) {
                super.onLost(network)
                isNetworkAvailable.invoke(false)
            }
        }
        val connectivityManager =
            getSystemService(ConnectivityManager::class.java) as ConnectivityManager
        connectivityManager.requestNetwork(networkRequest, networkCallback)
    }

    suspend inline fun <reified T> HttpResponse?.handleResponse(): Result<T> {
        return when {
            this == null -> Result.Failure("Unknown error")
            status.value !in 200..299 -> {
                val error = bodyAsText()
                Result.Failure(error)
            }

            else -> {
                try {
                    val result = body<T>()
                    Result.Success(result)
                } catch (e: Exception) {
                    Result.Failure(e.localizedMessage)
                }
            }
        }
    }

    val String.Companion.EMPTY: String
        get() = ""

    fun Any?.isNull() = this == null

    fun Any?.isNotNull() = this != null

    fun Boolean?.isTrue() = this == true

    fun Boolean?.isNotTrue() = this != true

    fun Long?.orZero() = this ?: 0

    fun Context.setAppLocale(language: String): Context {
        val locale = Locale(language)
        Locale.setDefault(locale)
        val config = resources.configuration
        config.setLocale(locale)
        config.setLayoutDirection(locale)
        return createConfigurationContext(config)
    }

    fun Application.isNetworkAvailable(): Boolean {
        return when (this) {
            is TalkToAIApp -> isNetworkAvailable.isTrue()
            else -> false
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    fun WebView.initWebView(webUrl: String, onPageFinished: () -> Unit) {
        setBackgroundColor(Color.TRANSPARENT)
        settings.javaScriptEnabled = true
        webViewClient = object : WebViewClient() {
            override fun onPageStarted(view: WebView, url: String, favicon: Bitmap?) {
            }

            override fun onPageFinished(view: WebView, url: String) {
                loadUrl(
                    if (context.isDarkMode()
                            .isTrue()
                    ) DARK_MODE_TEXT else WHITE_MODE_TEXT
                )
                onPageFinished.invoke()
            }
        }
        loadData(webUrl, MIME_TYPE, ENCODING)
    }
}

fun Context.isDarkMode(): Boolean {
    return when (resources?.configuration?.uiMode?.and(Configuration.UI_MODE_NIGHT_MASK)) {
        Configuration.UI_MODE_NIGHT_YES -> true
        else -> false
    }
}

fun LocaleList.flagDrawable(): Int {
    return when (if (isEmpty) Locale.getDefault().language else get(0).language) {
        Constants.APP_LANG_UK -> R.drawable.ic_flag_ua
        Constants.APP_LANG_RU -> R.drawable.ic_flag_ru
        else -> R.drawable.ic_flag_en
    }
}

fun Date.isDefineSecondsLater(seconds: Int, updated: Long): Boolean {
    Log.e(
        "dateTAG",
        "CommonExtensions isDefineSecondsLater time + (seconds * 1000) ${(time + (seconds * 1000)).millsSecondsToDateTime()} (updated * 1000) ${(updated * 1000).millsSecondsToDateTime()}"
    )
    return time + (seconds * 1000) < (updated * 1000)
}

fun Long.millsSecondsToDateTime(): String {
    return SimpleDateFormat("dd-MM-yyyy, HH:mm:ss", Locale.getDefault()).format(Date(this))
}

fun Date.dateToMilliseconds(): Long {
    return time / 1000
}

fun ApiException.getStatusCodeText(): String {
    return if (statusCode in -1..22) getStatusCodeString(statusCode) else "Операцию не удалось выполнить"
}

fun List<MessageUIModel>?.clearCheckToAction() {
    this?.forEach { message ->
        message.isCheckedToDelete.value = false
    }
}

fun List<MessageUIModel>?.textToAction(): String {
    return this?.filter { it.isCheckedToDelete.value }
        ?.joinToString { "${it.author}: ${it.message} \n" }.orEmpty()
}