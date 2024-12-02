package com.vnteam.talktoai.utils

import kotlinx.browser.window

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual class NetworkState {
    actual fun isNetworkAvailable(): Boolean {
        return window.navigator.onLine
    }
}