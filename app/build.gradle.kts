plugins {
    alias(libs.plugins.habittracker.android.application.plugin.convention)
    alias(libs.plugins.habittracker.hilt.plugin.convention)
    alias(libs.plugins.habittracker.firebase.base.plugin.convention)
    alias(libs.plugins.habittracker.datastore.plugin.convention)

    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.jetbrainsKotlinSerialization)
    kotlin("kapt")
    alias(libs.plugins.ksp)
}

android {
    namespace = "aeb.proyecto.habittracker"

    defaultConfig {
        applicationId = "aeb.proyecto.habittracker"
    }
}

dependencies {
    implementation(projects.core.datastore)

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

    //Datastore
    implementation(libs.androidx.datastore.preferences)
}
