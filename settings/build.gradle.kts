import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
}

kotlin {
    androidTarget {
        tasks.withType<KotlinJvmCompile>().configureEach {
            compilerOptions {
                jvmTarget.set(JvmTarget.JVM_17)
            }
        }
    }
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "settings"
            isStatic = true
        }
    }
    jvm()
    js(IR) {
        browser()
    }
    sourceSets {
        commonMain.dependencies {
            implementation(projects.network)
            implementation(libs.kotlinx.coroutines.core)
            // Koin
            implementation(libs.koin.core)
        }
        androidMain.dependencies {
            // Koin
            implementation(libs.koin.android)
            // Datastore
            implementation(libs.androidx.datastore.preferences)
        }
        iosMain.dependencies {
            // Datastore
            implementation(libs.androidx.datastore.preferences)
        }
        jvmMain.dependencies {
            implementation(libs.kotlinx.coroutines.swing)
            // Datastore
            implementation(libs.androidx.datastore.preferences)
        }
    }
}

android {
    namespace = "com.vnteam.talktoai.settings"
    compileSdk = libs.versions.compileSdk.get().toInt()
    defaultConfig {
        minSdk = libs.versions.minSdk.get().toInt()
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}
