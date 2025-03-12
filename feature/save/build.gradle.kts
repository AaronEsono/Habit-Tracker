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
    implementation(projects.core.authentication)
    implementation(projects.core.firestore)
    implementation(projects.core.room)
    implementation(projects.core.ui)
    implementation(projects.core.alarmManager)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)

    //Serializable
    implementation (libs.kotlinx.serialization.json)

    //Mas iconos
    implementation(libs.material.icons.extended)
}