plugins {
    alias(libs.plugins.habittracker.android.library.plugin.convention)
    alias(libs.plugins.habittracker.hilt.plugin.convention)
}

android {
    namespace = "aeb.proyecto.language"
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
}