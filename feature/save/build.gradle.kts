plugins {
    alias(libs.plugins.habittracker.android.library.plugin.convention)
    alias(libs.plugins.habittracker.jetpack.compose.plugin.convention)
    alias(libs.plugins.habittracker.hilt.plugin.convention)
    alias(libs.plugins.jetbrainsKotlinSerialization)
    alias(libs.plugins.habittracker.testing.plugin.convention)
    alias(libs.plugins.habittracker.compose.ui.test.plugin.contention)
}

android {
    namespace = "aeb.proyecto.save"
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)

    //Serializable
    implementation (libs.kotlinx.serialization.json)
}