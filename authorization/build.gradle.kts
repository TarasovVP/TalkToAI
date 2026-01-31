plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.kmpAndroidLibrary)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.kmpSecrets)
}

kotlin {
    android {
        namespace = "com.vnteam.talktoai"
        compileSdk = libs.versions.compileSdk.get().toInt()
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
            // Ktor
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.serialization.kotlinx.json)
            implementation(libs.ktor.client.logging)
            // Koin
            implementation(libs.koin.core)
        }
        androidMain.dependencies {
            // Ktor
            implementation(libs.ktor.client.android)
            // Koin
            implementation(libs.koin.android)
            // Google Auth
            implementation(libs.play.services.auth)
        }
        iosMain.dependencies {
            // Ktor
            implementation(libs.ktor.client.darwin)
        }
        jvmMain.dependencies {
            implementation(libs.kotlinx.coroutines.swing)
            // Ktor
            implementation(libs.ktor.client.java)
        }
        jsMain.dependencies {
            // Ktor
            implementation(libs.ktor.client.js)
        }
    }
}




