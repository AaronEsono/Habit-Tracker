plugins {
    alias(libs.plugins.habittracker.android.library.plugin.convention)
    alias(libs.plugins.habittracker.testing.plugin.convention)
    alias(libs.plugins.habittracker.hilt.plugin.convention)
}

android {
    namespace = "aeb.proyecto.alarmmanager"
}

dependencies {
    implementation(projects.core.room)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)

    //Gson
    implementation(libs.gson)

    implementation(libs.androidx.activity.compose)
}