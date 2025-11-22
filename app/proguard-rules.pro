# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile



# Keep application class for Hilt
-keep class com.example.androidmvvmcomposetemplate.App { *; }

# Hilt / Dagger - keep generated components and injectors
-keep class dagger.hilt.** { *; }
-dontwarn dagger.hilt.**
-keep class * extends dagger.hilt.internal.GeneratedComponent { *; }

# Retrofit / Moshi models - keep annotated classes if needed
# (Moshi is pretty good with reflection + KotlinJsonAdapterFactory)
-dontwarn okhttp3.**
-dontwarn okio.**
-dontwarn retrofit2.**

# Keep Kotlin coroutine debug metadata (optional, usually safe)
-dontwarn kotlinx.coroutines.**

# Room - keep schema and entities (AGP ships rules, but extra safe)
-dontwarn androidx.room.**
