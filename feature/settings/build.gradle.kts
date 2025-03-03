plugins {
    alias(libs.plugins.habittracker.android.library.plugin.convention)
    alias(libs.plugins.habittracker.jetpack.compose.plugin.convention)
    alias(libs.plugins.habittracker.hilt.plugin.convention)
    alias(libs.plugins.jetbrainsKotlinSerialization)
    alias(libs.plugins.habittracker.testing.plugin.convention)
    alias(libs.plugins.habittracker.compose.ui.test.plugin.contention)
}

android {
    namespace = "aeb.proyecto.settings"
}

dependencies {
    implementation(projects.core.ui)
    implementation(projects.core.authentication)
    implementation(projects.core.datastore)
    implementation(projects.core.language)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    //Serializable
    implementation (libs.kotlinx.serialization.json)

    //Mas iconos
    implementation(libs.material.icons.extended)
}