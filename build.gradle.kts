buildscript {
    extra.apply {
        set("hiltVersion", "2.46.1")
        set("navigationVersion", "2.7.0")
        set("roomVersion", "2.5.2")
    }

    dependencies {
        classpath("com.google.dagger:hilt-android-gradle-plugin:${rootProject.extra.get("hiltVersion")}")
        classpath("com.google.gms:google-services:4.3.15")
        classpath("com.google.firebase:firebase-crashlytics-gradle:2.9.9")
        classpath("org.jetbrains.kotlin:kotlin-serialization:1.8.0")
    }
}

plugins {
    id("org.jetbrains.kotlin.android") version "1.8.0" apply false
    id("com.android.application") version "7.4.1" apply false
    id("com.android.library") version "7.4.1" apply false
}