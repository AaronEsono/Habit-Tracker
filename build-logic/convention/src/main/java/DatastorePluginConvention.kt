import aeb.proyecto.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.add
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.dependencies


/**
 * A shared Gradle convention plugin that sets up Jetpack DataStore Preferences for a module.
 *
 * This plugin applies the Kotlin Kapt plugin and provisions the required AndroidX DataStore
 * Preferences dependency from the version catalog to handle asynchronous, reactive key-value storage.
 *
 * **Usage:**
 * Apply this plugin to core, preference, or data modules that require local persistent key-value storage.
 * ```
 * plugins {
 * alias(libs.plugins.habittracker.datastore.plugin.convention)
 * }
 * ```
 */
class DatastorePluginConvention: Plugin<Project> {
    override fun apply(target: Project) {
        with(target){
            apply(plugin = "org.jetbrains.kotlin.kapt")

            dependencies {
                add("implementation", libs.findLibrary("androidx-datastore-preferences").get())
            }
        }
    }
}