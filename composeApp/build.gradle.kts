
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile
import java.util.Properties

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.compose.compiler)
}

kotlin {
    androidTarget()
    task("testClasses")
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            freeCompilerArgs += "-Xbinary=bundleId=com.vnteam.architecturetemplates.composeApp"
            linkerOpts.add("-lsqlite3")
            baseName = "composeApp"
            isStatic = true
        }
    }
    jvm("desktop") {
        tasks.withType<KotlinJvmCompile>().configureEach {
            compilerOptions {
                jvmTarget.set(JvmTarget.JVM_17)
            }
        }
    }
    js(IR) {
        browser {
            commonWebpackConfig {
                outputFileName = "webApp.js"
            }

        }
        binaries.executable()
    }
    sourceSets {
        val desktopMain by getting

        commonMain.dependencies {
            implementation(project(":shared"))
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.components.resources)
            implementation(libs.androidx.viewmodel.compose)
            // Koin
            implementation(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel)
            // Coil
            implementation(libs.coil.compose)
            implementation(libs.coil.network.ktor)
            // Navigation
            implementation(libs.navigation.compose)
            // DateTime
            implementation(libs.kotlinx.datetime)
        }
        androidMain.dependencies {
            implementation(libs.androidx.multidex)
            // Koin
            implementation(libs.koin.android)
            implementation(libs.koin.androidx.compose)

            // Compose
            implementation(compose.material3)
            implementation(libs.androidx.activity.compose)
            implementation(libs.material.ripple)
            // Google Auth
            implementation(libs.play.services.auth)
        }
        desktopMain.dependencies {
            implementation(libs.koin.core)
            implementation(compose.desktop.currentOs)
        }
        iosMain.dependencies {
            implementation(libs.koin.core)
        }
        jsMain.dependencies {
            //Compose
            implementation(compose.html.core)
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.components.resources)
            //Koin
            implementation(libs.koin.core)
            implementation(libs.koin.compose)
        }
    }
}

android {
    namespace = "com.vnteam.talktoai"
    compileSdk = libs.versions.compileSdk.get().toInt()

    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    sourceSets["main"].res.srcDirs("src/androidMain/res")

    defaultConfig {
        applicationId = "com.vnteam.talktoai"
        minSdk = libs.versions.minSdk.get().toInt()
        targetSdk = libs.versions.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"

        multiDexEnabled = true

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    signingConfigs {
        val localProperties = file(rootProject.file("local.properties"))
        val properties = Properties().apply {
            load(localProperties.inputStream())
        }
        create("release") {
            storeFile = file(project.rootProject.file(properties.getProperty("STORE_FILE", "")))
            storePassword = properties.getProperty("STORE_PASSWORD", "")
            keyAlias = properties.getProperty("KEY_ALIAS", "")
            keyPassword = properties.getProperty("KEY_PASSWORD", "")
        }
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("release")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.kotlinCompilerExtensionVersion.get()
    }
}

compose.desktop {
    application {
        mainClass = "MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "com.vnteam.talktoai"
            packageVersion = "1.0.0"
        }
    }
}