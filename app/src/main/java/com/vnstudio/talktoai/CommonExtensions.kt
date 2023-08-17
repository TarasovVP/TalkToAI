package com.vnstudio.talktoai

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import retrofit2.Response
import com.vnstudio.talktoai.data.network.Result

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

    fun <T>  Response<T>.apiCall(): Result<T> {
        try {
            if (isSuccessful) {
                body()?.let { body ->
                    return Result.Success(body)
                }
            }
            return Result.Failure(message())
        } catch (e: Exception) {
            return Result.Failure(e.localizedMessage)
        }
    }

    private fun <T> LiveData<T>.safeObserve(owner: LifecycleOwner, observer: (t: T) -> Unit) {
        this.observe(owner) {
            it?.let(observer)
        }
    }

    fun <T> MutableLiveData<T>.safeSingleObserve(owner: LifecycleOwner, observer: (t: T) -> Unit) {
        safeObserve(owner, observer)
        value = null
    }

    val String.Companion.EMPTY: String
        get() = ""
}