plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.jetbrainsKotlinSerialization)
    kotlin("kapt")
    alias(libs.plugins.hilt)
    alias(libs.plugins.ksp)
    alias(libs.plugins.firebase.crashlytics)
    alias(libs.plugins.google.services)
}

android {
    namespace = "aeb.proyecto.habittracker"
    compileSdk = 35

    defaultConfig {
        applicationId = "aeb.proyecto.habittracker"
    }

    defaultConfig {
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    buildTypes {
        debug {
            isDebuggable = false
        }

        release {
            isMinifyEnabled = false
            isShrinkResources = false
            isDebuggable = false

            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )

            signingConfig = signingConfigs.findByName("debug")
        }
    }

    buildFeatures{
        compose = true
    }

    kotlinOptions{
        jvmTarget = "11"
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.3"
    }


}

dependencies {
    implementation(projects.core.datastore)
    implementation(projects.core.room)
    implementation(projects.core.firestore)
    implementation(projects.core.authentication)
    implementation(projects.core.analytics)
    implementation(projects.core.alarmManager)

    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.runtime.livedata)
    implementation(libs.play.services.tflite.support)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.ui.test.junit4)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    //NavController
    implementation(libs.androidx.navigation.compose)

    //Serializable
    implementation (libs.kotlinx.serialization.json)

    //Material 3
    implementation(libs.material3)
    implementation(libs.androidx.material3.adaptive.navigation.suite)

    //Mas iconos
    implementation(libs.material.icons.extended)

    // Integración de Hilt con Compose
    implementation(libs.androidx.hilt.navigation.compose)

    //Room
    implementation(libs.androidx.room.runtime)
    ksp (libs.androidx.room.compiler)
    implementation(libs.androidx.room.ktx)

    //Gson
    implementation(libs.gson)

    //Permissions
    implementation (libs.androidx.activity.compose)

    //Datastore
    implementation(libs.androidx.datastore.preferences)

    //Firebase
    implementation(platform(libs.firebase.bom))

    //Analytics
    implementation(libs.firebase.analytics)

    //Authentication
    implementation(libs.firebase.auth.ktx)

    //Crashlytics
    implementation(libs.firebase.crashlytics)

    //Firestore
    implementation(libs.firebase.firestore.ktx)

    //Android credentials
    implementation(libs.androidx.credentials)
    implementation(libs.androidx.credentials.play.services.auth)

    //Google id
    implementation(libs.googleid)

    //Hilt
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)

    //Datastore
    implementation(libs.androidx.datastore.preferences)
}
