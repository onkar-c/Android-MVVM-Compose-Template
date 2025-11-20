plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)


    // Hilt plugin for DI
    id("com.google.dagger.hilt.android")

    // For annotation processing (Hilt, Room)
    id("kotlin-kapt")
}

android {
    namespace = "com.example.androidmvvmcomposetemplate"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.androidmvvmcomposetemplate"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }


    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"
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
        buildConfig = true
    }
    kapt {
        correctErrorTypes = true
    }
    packaging {
        resources {
            excludes += "META-INF/versions/9/OSGI-INF/MANIFEST.MF"
        }
    }

}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)


    // --- Kotlin Coroutines (for async, Flow) ---
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)

// --- Lifecycle (ViewModel, runtime) ---
    implementation(libs.androidx.lifecycle.viewmodel.ktx)


// --- Navigation Compose (for multi-screen nav) ---
    implementation(libs.androidx.navigation.compose)

// --- Hilt (core + compiler) ---
    implementation(libs.hilt.android)
    implementation(libs.androidx.hilt.navigation.compose)
    kapt(libs.hilt.android.compiler)

// --- Room (DB + Kotlin extensions + compiler) ---
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    kapt(libs.androidx.room.compiler)

// --- Networking (Retrofit + OkHttp logging) ---
    implementation(libs.retrofit)
    implementation(libs.converter.moshi)
    implementation(libs.logging.interceptor)

    implementation(libs.moshi.kotlin)


// --- Logging (Timber) ---
    implementation(libs.timber)

    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.material.icons.extended)
}