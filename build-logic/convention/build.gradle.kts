plugins {
    `kotlin-dsl`
}

group = "aeb.proyecto.buildlogic"

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

dependencies {
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.kotlin.gradlePlugin)
}

gradlePlugin {
    plugins {
        register("androidApplicationPluginConvention"){
            id = libs.plugins.habittracker.android.application.plugin.convention.get().pluginId
            implementationClass = "AndroidApplicationPluginConvention"
        }
        register("hiltPluginConvention"){
            id = libs.plugins.habittracker.hilt.plugin.convention.get().pluginId
            implementationClass = "HiltPluginConvention"
        }
        register("firebaseBasePluginConvention"){
            id = libs.plugins.habittracker.firebase.base.plugin.convention.get().pluginId
            implementationClass = "FirebaseBasePluginConvention"
        }
        register("datastorePluginConvention"){
            id = libs.plugins.habittracker.datastore.plugin.convention.get().pluginId
            implementationClass = "DatastorePluginConvention"
        }
        register("androidLibraryPluginConvention"){
            id = libs.plugins.habittracker.android.library.plugin.convention.get().pluginId
            implementationClass = "AndroidLibraryPluginConvention"
        }
    }
}