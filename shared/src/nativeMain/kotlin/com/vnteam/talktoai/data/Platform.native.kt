package com.vnteam.talktoai.data

import platform.Foundation.NSUUID

actual fun generateUUID(): String = NSUUID().UUIDString()