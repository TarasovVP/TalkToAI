
plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("kotlin-parcelize")
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
    id("kotlinx-serialization")
    id("com.squareup.sqldelight")
}

android {
    namespace = "com.vnstudio.talktoai"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.vnstudio.talktoai"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
        buildConfigField("String", "OPENAI_API_KEY", "${rootProject.properties["OPENAI_API_KEY"]}")
        buildConfigField("String", "ORGANIZATION_ID", "${rootProject.properties["ORGANIZATION_ID"]}")
        buildConfigField("String", "BASE_URL", "${rootProject.properties["BASE_URL"]}")
        buildConfigField("String", "SERVER_CLIENT_ID", "${rootProject.properties["SERVER_CLIENT_ID"]}")
        buildConfigField("String", "REALTIME_DATABASE", "${rootProject.properties["REALTIME_DATABASE"]}")
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                    getDefaultProguardFile("proguard-android-optimize.txt"),
                    "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.0"
    }
    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

sqldelight {
    database("AppDatabase") {
        packageName = "com.vnstudio.talktoai"
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("androidx.legacy:legacy-support-v4:1.0.0")
    implementation("androidx.activity:activity-compose:1.8.2")
    implementation("com.google.accompanist:accompanist-insets:0.10.0")

    //Testing
    implementation("com.google.ar:core:1.41.0")
    implementation("androidx.test:runner:1.5.2")
    debugImplementation("androidx.test:monitor:1.6.1")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.compose.ui:ui-test-junit4:1.6.1")
    debugImplementation("androidx.compose.ui:ui-test-manifest:1.6.1")
    implementation("androidx.navigation:navigation-testing:2.7.7")
    implementation("androidx.arch.core:core-testing:2.2.0")
    testImplementation("io.mockk:mockk:1.13.7")
    // Koin Tests
    testImplementation("io.insert-koin:koin-test:3.5.3")
    testImplementation("io.insert-koin:koin-test-junit4:3.5.3")
    testImplementation("io.insert-koin:koin-android-test:3.5.3")

    //Lifecycle
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0")

    //Compose
    implementation(platform("androidx.compose:compose-bom:2023.06.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material:material")
    implementation("androidx.compose.runtime:runtime-livedata")
    implementation("androidx.compose.runtime:runtime")

    //Firebase
    implementation(platform("com.google.firebase:firebase-bom:32.0.0"))
    implementation("com.google.firebase:firebase-analytics-ktx")
    implementation("com.google.firebase:firebase-crashlytics-ktx")
    implementation("com.google.firebase:firebase-auth-ktx")
    implementation("com.google.firebase:firebase-database-ktx")
    implementation("com.firebaseui:firebase-ui-auth:8.0.2")

    //GoogleServices
    implementation("com.google.android.gms:play-services-ads:22.6.0")
    implementation("com.google.android.gms:play-services-auth:20.7.0")

    //Ktor
    implementation("io.ktor:ktor-client-android:2.3.7")
    implementation("io.ktor:ktor-serialization-kotlinx-json:2.3.7")
    implementation("io.ktor:ktor-client-logging-jvm:2.3.7")
    implementation("io.ktor:ktor-client-content-negotiation:2.3.7")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.1")

    // Koin
    implementation("io.insert-koin:koin-android:3.5.3")
    implementation("io.insert-koin:koin-androidx-compose:3.5.3")

    //SQLDelight
    implementation("com.squareup.sqldelight:android-driver:1.5.4")
    implementation("com.squareup.sqldelight:coroutines-extensions:1.5.4")

    //Coil
    implementation("io.coil-kt:coil-compose:2.2.2")

    //Lottie
    implementation("com.airbnb.android:lottie-compose:4.0.0")

    //DataStore
    implementation("androidx.datastore:datastore-preferences:1.0.0")

    //DateTime
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.5.0")

    //Voyager
    implementation("cafe.adriel.voyager:voyager-navigator:1.0.0")
    implementation("cafe.adriel.voyager:voyager-transitions:1.0.0")
    implementation("cafe.adriel.voyager:voyager-koin:1.0.0")
}