import aeb.proyecto.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

/**
 * A shared Gradle convention plugin that configures Dagger Hilt dependency injection
 * for a specific module.
 *
 * This plugin applies the official Hilt and Kotlin Kapt plugins, and provisions the
 * core Hilt Android and Navigation Compose dependencies from the version catalog.
 *
 * **Usage:**
 * Apply this plugin to any module that requires Hilt dependency injection and ViewModel scoping.
 * ```
 * plugins {
 *     alias(libs.plugins.habittracker.hilt.plugin.convention)
 * }
 */
class HiltPluginConvention: Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.google.dagger.hilt.android")
                apply("org.jetbrains.kotlin.kapt")
            }

            dependencies {
                add("implementation", libs.findLibrary("hilt-android").get())
                add("kapt", libs.findLibrary("hilt-compiler").get())
                add("implementation", libs.findLibrary("androidx-hilt-navigation-compose").get())
            }
        }
    }
}