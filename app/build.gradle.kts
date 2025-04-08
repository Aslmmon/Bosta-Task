plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    kotlin("kapt")
    id("com.google.dagger.hilt.android")


}

android {
    namespace = "com.aslmmovic.bosta_task"
    compileSdk = 35
    android.buildFeatures.buildConfig =true

    defaultConfig {
        applicationId = "com.aslmmovic.bosta_task"
        minSdk = 28
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {

        getByName("release") {
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            buildConfigField("String", "BASE_URL", "\"https://production-app.bosta.co/api/v2/\"")
        }
        getByName("debug") {
            buildConfigField("String", "BASE_URL", "\"https://stg-app.bosta.co/api/v2/\"")
        }

    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    kapt {
        correctErrorTypes = true
    }

}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.benchmark.common)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // Retrofit
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.logging.interceptor)

    // Coroutines
    implementation(libs.kotlinx.coroutines.android)

    // Dagger 2 / Hilt
    implementation(libs.hilt.android.v2511) // Or the latest Hilt version
    kapt(libs.hilt.android.compiler.v2511) // Or the latest Hilt version

    // ViewModel and LiveData
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.livedata.ktx)

    // RecyclerView
    implementation(libs.androidx.recyclerview)

    // Gson
    implementation(libs.gson)


    // Unit Testing
    testImplementation(libs.junit)
    testImplementation(libs.mockito.core)
    testImplementation(libs.mockito.kotlin)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.androidx.core.testing)
    testImplementation(libs.junit)
    testImplementation(libs.mockito.kotlin.v400)
    testImplementation(libs.kotlinx.coroutines.test.v173)
    testImplementation(libs.truth) // Optional, but recommended
    // Hilt Testing
    androidTestImplementation(libs.hilt.android.testing) // Or the latest Hilt version
    kaptAndroidTest(libs.hilt.android.compiler.v2511) // Or the latest Hilt version


}