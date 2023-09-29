plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("dagger.hilt.android.plugin")
    id("kotlin-kapt")
    id("kotlin-parcelize")
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
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

        testInstrumentationRunner = "com.vnstudio.talktoai.HiltTestRunner"
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
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

dependencies {

    val composeVersion = rootProject.extra.get("composeVersion")
    val hiltVersion = rootProject.extra.get("hiltVersion")
    val navigationVersion = rootProject.extra.get("navigationVersion")
    val roomVersion = rootProject.extra.get("roomVersion")

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("androidx.legacy:legacy-support-v4:1.0.0")
    implementation("androidx.activity:activity-compose:1.7.2")
    implementation("com.google.accompanist:accompanist-insets:0.10.0")

    //Testing
    implementation("com.google.ar:core:1.39.0")
    implementation("androidx.test:runner:1.5.2")
    debugImplementation("androidx.test:monitor:1.6.1")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.compose.ui:ui-test-junit4:1.5.1")
    debugImplementation("androidx.compose.ui:ui-test-manifest:1.5.1")
    implementation("androidx.navigation:navigation-testing:2.7.3")

    //Lifecycle
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.1")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0-alpha01")

    //Compose
    implementation("androidx.compose.ui:ui:$composeVersion")
    implementation("androidx.compose.ui:ui-tooling-preview:$composeVersion")
    implementation("androidx.compose.material:material:$composeVersion")
    implementation("androidx.compose.runtime:runtime-livedata:$composeVersion")
    implementation("androidx.compose.runtime:runtime:$composeVersion")
    implementation("androidx.hilt:hilt-navigation-compose:1.1.0-alpha01")

    //Navigation
    implementation("androidx.navigation:navigation-compose:$navigationVersion")

    //Firebase
    implementation(platform("com.google.firebase:firebase-bom:32.0.0"))
    implementation("com.google.firebase:firebase-analytics-ktx")
    implementation("com.google.firebase:firebase-crashlytics-ktx")
    implementation("com.google.firebase:firebase-auth-ktx")
    implementation("com.google.firebase:firebase-database-ktx")
    implementation("com.firebaseui:firebase-ui-auth:8.0.2")

    //GoogleServices
    implementation("com.google.android.gms:play-services-ads:22.3.0")
    implementation("com.google.android.gms:play-services-auth:20.6.0")

    //Api
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.9.1")

    //Hilt
    implementation("com.google.dagger:hilt-android:$hiltVersion")
    kapt("com.google.dagger:hilt-compiler:$hiltVersion")
    kapt("com.google.dagger:hilt-android-compiler:$hiltVersion")
    kaptAndroidTest("com.google.dagger:hilt-android-compiler:$hiltVersion")
    androidTestAnnotationProcessor("com.google.dagger:hilt-android-compiler:$hiltVersion")
    implementation("com.google.dagger:hilt-android-testing:$hiltVersion")
    androidTestImplementation("com.google.dagger:hilt-android-testing:$hiltVersion")

    //Room
    implementation("androidx.room:room-runtime:$roomVersion")
    kapt("androidx.room:room-compiler:$roomVersion")
    implementation("androidx.room:room-ktx:$roomVersion")

    //Coil
    implementation("io.coil-kt:coil-compose:2.2.2")

    //Lottie
    implementation("com.airbnb.android:lottie-compose:4.0.0")

    //DataStore
    implementation("androidx.datastore:datastore-preferences:1.0.0")
}
