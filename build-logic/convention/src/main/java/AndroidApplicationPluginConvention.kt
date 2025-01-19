import aeb.proyecto.convention.libs
import com.android.build.api.dsl.ApplicationExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.jetbrains.kotlin.gradle.dsl.KotlinAndroidProjectExtension


class AndroidApplicationPluginConvention : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.application")
                apply("org.jetbrains.kotlin.android")
            }

            extensions.configure<ApplicationExtension> {
                configureAndroidBase()
                configureBuildTypes()
                configureCompose()
            }

            project.extensions.configure<KotlinAndroidProjectExtension> {
                compilerOptions.jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_11)
            }

            dependencies {
                add("implementation", platform(libs.findLibrary("androidx-compose-bom").get()))
                add("androidTestImplementation", platform(libs.findLibrary("androidx-compose-bom").get()))
            }
        }
    }
}

private fun ApplicationExtension.configureAndroidBase() {
    compileSdk = 35

    defaultConfig {
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

private fun ApplicationExtension.configureBuildTypes() {
    buildTypes {
        debug {
            isDebuggable = false
        }

        release {
            isMinifyEnabled = false
            isShrinkResources = false
            isDebuggable = false

            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )

            signingConfig = signingConfigs.findByName("debug")
        }
    }
}

private fun ApplicationExtension.configureCompose() {
    buildFeatures{
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.3"
    }
}