import aeb.proyecto.convention.libs
import com.android.build.gradle.LibraryExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.jetbrains.kotlin.gradle.dsl.KotlinAndroidProjectExtension


/**
 * The core Gradle convention plugin responsible for establishing base Android and Kotlin
 * configurations across all library modules in the project.
 *
 * This plugin applies the standard Android Library and Kotlin Android plugins, and enforces
 * unified build properties including SDK versions (compileSdk 36, minSdk 26), Java 11
 * compatibility options for both Java and Kotlin bytecode targets, and default release
 * build type obfuscation profiles.
 *
 * **Usage:**
 * Apply this plugin as the foundational build configuration for any new library or feature module.
 * ```
 * plugins {
 * alias(libs.plugins.habittracker.android.library.plugin.convention)
 * }
 * ```
 */
class AndroidLibraryPluginConvention : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            apply(plugin = "com.android.library")
            apply(plugin = "org.jetbrains.kotlin.android")

            extensions.configure<LibraryExtension> {
                compileSdk = 36
                defaultConfig {
                    minSdk = 26
                }
                compileOptions {
                    sourceCompatibility = JavaVersion.VERSION_11
                    targetCompatibility = JavaVersion.VERSION_11
                }
                project.extensions.configure<KotlinAndroidProjectExtension> {
                    compilerOptions.jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_11)
                }

                buildTypes {
                    release {
                        isMinifyEnabled = false
                        proguardFiles(
                            getDefaultProguardFile("proguard-android-optimize.txt"),
                            "proguard-rules.pro"
                        )
                    }
                }
            }
        }
    }
}