package com.vnteam.talktoai.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import com.vnteam.talktoai.CommonExtensions.isTrue

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual class NetworkState(context: Context) {

    private var isNetworkAvailable: Boolean? = null

    actual fun isNetworkAvailable(): Boolean {
        return isNetworkAvailable.isTrue()
    }

    init {
        registerForNetworkUpdates(context)
    }

    private fun registerForNetworkUpdates(context: Context) {
        val networkRequest = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
            .build()
        val networkCallback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                isNetworkAvailable = true
            }

            override fun onLost(network: Network) {
                super.onLost(network)
                isNetworkAvailable = false
            }
        }
        val connectivityManager =
            context.getSystemService(ConnectivityManager::class.java) as ConnectivityManager
        connectivityManager.requestNetwork(networkRequest, networkCallback)
    }
}