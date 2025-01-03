plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.jetbrainsKotlinSerialization)
    alias(libs.plugins.hilt)
    kotlin("kapt")
    alias(libs.plugins.ksp)
    alias(libs.plugins.google.services)
}

android {
    namespace = "aeb.proyecto.habittracker"
    compileSdk = 35

    defaultConfig {
        applicationId = "aeb.proyecto.habittracker"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        debug {
            isDebuggable = false
        }

        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("debug")
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
        kotlinCompilerExtensionVersion = "1.5.3"
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
    implementation(libs.androidx.runtime.livedata)
    implementation(libs.play.services.tflite.support)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    //NavController
    implementation(libs.androidx.navigation.compose)

    //Serializable
    implementation (libs.kotlinx.serialization.json)

    implementation(libs.material3)
    implementation(libs.androidx.material3.adaptive.navigation.suite)

    //Mas iconos
    implementation(libs.material.icons.extended)


    //Hilt y Dagger
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)

    // Integración de Hilt con Compose
    implementation(libs.androidx.hilt.navigation.compose)

    //Room
    implementation(libs.androidx.room.runtime)
    ksp (libs.androidx.room.compiler)
    implementation(libs.androidx.room.ktx)

    //Gson
    implementation(libs.gson)

    //Permissions
    implementation (libs.androidx.activity.compose.v172)

    //Status Bar
    implementation (libs.accompanist.systemuicontroller.v0270)

    //DataStore
    implementation(libs.androidx.datastore.preferences)

    //Firebase
    implementation(libs.firebase.analytics)
    implementation(platform(libs.firebase.bom))

    //Auth
    implementation(libs.firebase.auth.ktx)

    implementation(libs.androidx.credentials)
    implementation(libs.androidx.credentials.play.services.auth)
    implementation (libs.googleid)
}
