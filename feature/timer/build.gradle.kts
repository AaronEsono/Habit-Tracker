plugins {
    alias(libs.plugins.habittracker.android.library.plugin.convention)
    alias(libs.plugins.habittracker.jetpack.compose.plugin.convention)
    alias(libs.plugins.habittracker.hilt.plugin.convention)
    alias(libs.plugins.jetbrainsKotlinSerialization)
    alias(libs.plugins.habittracker.testing.plugin.convention)
    alias(libs.plugins.habittracker.compose.ui.test.plugin.contention)
}

android {
    namespace = "aeb.proyecto.timer"

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

    implementation(projects.core.domain)
    implementation(projects.core.room)
    implementation(projects.core.ui)
    implementation(projects.core.datastore)
    implementation(projects.core.stopWatch)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    //Serializable
    implementation (libs.kotlinx.serialization.json)

    //Mas iconos
    implementation(libs.material.icons.extended)

    //Adaptative
    implementation(libs.material3.adaptive.navigation.suite)

    debugImplementation(libs.androidx.ui.test.manifest)
}