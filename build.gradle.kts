buildscript {

    dependencies {
        classpath("com.google.gms:google-services:4.4.1")
        classpath("com.google.firebase:firebase-crashlytics-gradle:2.9.9")
        classpath("org.jetbrains.kotlin:kotlin-serialization:1.8.0")
        classpath("com.squareup.sqldelight:gradle-plugin:1.5.5")
    }
}

plugins {
    id("org.jetbrains.kotlin.android") version "1.8.0" apply false
    id("com.android.application") version "7.4.1" apply false
    id("com.android.library") version "7.4.1" apply false
}