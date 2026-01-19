plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.example.practicaltest02v3"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.practicaltest02v3"
        minSdk = 26
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
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
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)

    // For local libraries
    implementation(files("G:\\EIM_LAB\\httpclient-4.4.1.2.jar"))
    implementation(files("G:\\EIM_LAB\\jsoup-1.10.2.jar"))

    // For OKHTTP
    implementation("androidx.appcompat:appcompat:1.7.1") // Use AndroidX
    implementation("androidx.core:core-ktx:1.13.0") // For WindowCompat and ViewCompat
    implementation(platform("com.squareup.okhttp3:okhttp-bom:5.3.2"))
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("com.squareup.okhttp3:logging-interceptor") // Optional, but useful for debugging

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}