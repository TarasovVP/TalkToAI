package com.vnteam.talktoai

object CommonExtensions {

    val String.Companion.EMPTY: String
        get() = ""

    fun Any?.isNull() = this == null

    fun Any?.isNotNull() = this != null

    fun Boolean?.isTrue() = this == true

    fun Boolean?.isNotTrue() = this != true

    fun Long?.orZero() = this ?: 0

}