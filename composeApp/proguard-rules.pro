# kotlinx.serialization
-keepattributes *Annotation*, InnerClasses
-dontnote kotlinx.serialization.AnnotationsKt
-keepclassmembers class kotlinx.serialization.json.** {
    *** Companion;
}
-keepclasseswithmembers class kotlinx.serialization.json.** {
    kotlinx.serialization.KSerializer serializer(...);
}
-keep,includedescriptorclasses class com.vnteam.talktoai.**$$serializer { *; }
-keepclassmembers class com.vnteam.talktoai.** {
    *** Companion;
}
-keepclasseswithmembers class com.vnteam.talktoai.** {
    kotlinx.serialization.KSerializer serializer(...);
}

# Koin
-keep class org.koin.** { *; }
-keep class com.vnteam.talktoai.di.** { *; }
-keepclassmembers class * {
    @org.koin.core.annotation.* <methods>;
}

# SQLDelight
-keep class app.cash.sqldelight.** { *; }
-keep class com.vnteam.talktoai.AppDatabase { *; }
-keep class com.vnteam.talktoai.AppDatabase$Companion { *; }

# Ktor
-keep class io.ktor.** { *; }
-dontwarn io.ktor.**
-dontwarn kotlinx.atomicfu.**

# Kotlin coroutines
-keepnames class kotlinx.coroutines.internal.MainDispatcherFactory {}
-keepnames class kotlinx.coroutines.CoroutineExceptionHandler {}
-keepclassmembers class kotlinx.coroutines.** {
    volatile <fields>;
}

# Keep line numbers for crash reports
-keepattributes SourceFile,LineNumberTable
-renamesourcefileattribute SourceFile
