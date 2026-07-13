plugins {
    alias(libs.plugins.habittracker.android.library.plugin.convention)
    alias(libs.plugins.habittracker.jetpack.compose.plugin.convention)
    alias(libs.plugins.habittracker.hilt.plugin.convention)
    alias(libs.plugins.habittracker.testing.plugin.convention)
    alias(libs.plugins.ksp)
}

android {
    namespace = "aeb.proyecto.room"

    ksp {
        arg("room.schemaLocation", "$projectDir/schemas")
    }

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

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)

    //Room
    implementation(libs.androidx.room.runtime)
    ksp(libs.androidx.room.compiler)
    implementation(libs.androidx.room.ktx)

    //Gson
    implementation(libs.gson)

    //Mas iconos
    implementation(libs.material.icons.extended)

    debugImplementation(libs.androidx.ui.test.manifest)
}