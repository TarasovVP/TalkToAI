package com.vnteam.talktoai.utils

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.alloc
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.ptr
import kotlinx.cinterop.value
import platform.CoreFoundation.CFRelease
import platform.SystemConfiguration.SCNetworkReachabilityCreateWithName
import platform.SystemConfiguration.SCNetworkReachabilityFlagsVar
import platform.SystemConfiguration.SCNetworkReachabilityGetFlags

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual class NetworkState {
    @OptIn(ExperimentalForeignApi::class)
    actual fun isNetworkAvailable(): Boolean {
        val reachability = SCNetworkReachabilityCreateWithName(null, "google.com")
            ?: return false
        return try {
            memScoped {
                val flags = alloc<SCNetworkReachabilityFlagsVar>()
                val isReachable = SCNetworkReachabilityGetFlags(reachability, flags.ptr)
                isReachable && (flags.value.toInt() and 2) != 0
            }
        } finally {
            CFRelease(reachability)
        }
    }
}
