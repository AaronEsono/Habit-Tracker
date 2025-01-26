plugins {
    alias(libs.plugins.habittracker.android.library.plugin.convention)
    alias(libs.plugins.habittracker.hilt.plugin.convention)
    alias(libs.plugins.habittracker.datastore.plugin.convention)
}

android {
    namespace = "aeb.proyecto.datastore"
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.core.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    testImplementation (libs.mockk.mockk)
    testImplementation (libs.kotlinx.coroutines.test)
    testImplementation (libs.mockito.mockito.core)
    testImplementation (libs.mockito.kotlin)
}