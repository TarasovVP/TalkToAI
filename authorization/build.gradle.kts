import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.kotlinSerialization)
}

kotlin {
    androidTarget {
        tasks.withType<KotlinJvmCompile>().configureEach {
            compilerOptions {
                jvmTarget.set(JvmTarget.JVM_21)
            }
        }
    }
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "shared"
            isStatic = true
        }
    }
    jvm()
    js(IR) {
        browser()
    }
    sourceSets {
        commonMain.dependencies {
            implementation(libs.kotlinx.serialization)
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.androidx.viewmodel.compose)
            // Compose
            implementation(libs.compose.runtime)
            implementation(libs.compose.ui)
            implementation(libs.compose.foundation)
            implementation(libs.compose.material3)
            implementation(libs.compose.components.resources)
            // Ktor
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.serialization.kotlinx.json)
            implementation(libs.ktor.client.logging)
            // Koin
            implementation(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel)
            // SQLDelight
            implementation(libs.sqldelight.coroutines.extensions)
        }
        androidMain.dependencies {
            implementation(libs.androidx.multidex)
            implementation(libs.ktor.client.android)
            implementation(libs.sqldelight.android.driver)
            // Koin
            implementation(libs.koin.android)
            implementation(libs.koin.androidx.compose)
            // Datastore
            implementation(libs.androidx.datastore.preferences)
            // Lottie
            implementation(libs.lottie.compose)
            // Google Auth
            implementation(libs.play.services.auth)
        }
        iosMain.dependencies {
            implementation(libs.ktor.client.darwin)
            implementation(libs.sqldelight.native.driver)
            // Datastore
            implementation(libs.androidx.datastore.preferences)
            // Lottie
            implementation(libs.lottie.compose)
        }
        nativeMain.dependencies {
            implementation(libs.ktor.client.darwin)
            implementation(libs.sqldelight.native.driver)
            // Lottie
            implementation(libs.lottie.compose)
        }
        jvmMain.dependencies {
            implementation(libs.koin.core)
            implementation(libs.ktor.client.java)
            implementation(libs.kotlinx.coroutines.swing)
        }
        jsMain.dependencies {
            implementation(libs.ktor.client.js)
        }
    }
}

android {
    namespace = "com.vnteam.talktoai"
    compileSdk = 36
    defaultConfig {
        minSdk = 28
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
}


