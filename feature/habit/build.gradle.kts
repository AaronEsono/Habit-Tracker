plugins {
    alias(libs.plugins.habittracker.android.library.plugin.convention)
    alias(libs.plugins.habittracker.jetpack.compose.plugin.convention)
    alias(libs.plugins.habittracker.hilt.plugin.convention)
    alias(libs.plugins.jetbrainsKotlinSerialization)
    alias(libs.plugins.habittracker.testing.plugin.convention)
    alias(libs.plugins.habittracker.compose.ui.test.plugin.contention)
}

android {
    namespace = "aeb.proyecto.habit"

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

    implementation(projects.core.ui)
    implementation(projects.core.room)
    implementation(projects.core.alarmManager)
    implementation(projects.core.datastore)
    implementation(projects.core.domain)
    implementation(projects.core.language)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)

    //Serializable
    implementation (libs.kotlinx.serialization.json)

    //Mas iconos
    implementation(libs.material.icons.extended)

    debugImplementation(libs.androidx.ui.test.manifest)
}