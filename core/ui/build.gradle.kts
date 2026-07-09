plugins {
    alias(libs.plugins.habittracker.android.library.plugin.convention)
    alias(libs.plugins.habittracker.testing.plugin.convention)
    alias(libs.plugins.habittracker.hilt.plugin.convention)
    alias(libs.plugins.habittracker.jetpack.compose.plugin.convention)
}

android {
    namespace = "aeb.proyecto.ui"

    packaging {
        resources {
            excludes += "/META-INF/LICENSE.md"
            excludes += "/META-INF/LICENSE-notice.md"
            // excludes += "/META-INF/LICENSE.txt"
            // excludes += "/META-INF/NOTICE.txt"
        }
    }
}

dependencies {
    implementation(projects.core.room)
    implementation(projects.core.language)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)

    //Mas iconos
    implementation(libs.material.icons.extended)

    debugImplementation(libs.androidx.ui.test.manifest)
}