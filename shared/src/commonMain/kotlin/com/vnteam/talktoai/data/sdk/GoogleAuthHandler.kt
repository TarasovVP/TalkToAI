package com.vnteam.talktoai.data.sdk

expect class GoogleAuthHandler {
    fun signIn()
    fun signOut()
    fun getToken(): String?
}