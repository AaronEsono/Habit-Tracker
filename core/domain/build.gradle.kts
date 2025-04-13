plugins {
    alias(libs.plugins.habittracker.android.library.plugin.convention)
    alias(libs.plugins.habittracker.jetpack.compose.plugin.convention)
    alias(libs.plugins.habittracker.hilt.plugin.convention)
    alias(libs.plugins.habittracker.testing.plugin.convention)
    alias(libs.plugins.ksp)
}

android {
    namespace = "aeb.proyecto.domain"
}

dependencies {

    implementation(projects.core.room)
    implementation(projects.core.datastore)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    //Gson
    implementation(libs.gson)
}