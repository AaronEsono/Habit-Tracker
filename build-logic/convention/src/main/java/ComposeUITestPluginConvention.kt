import aeb.proyecto.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies


/**
 * A shared Gradle convention plugin that configures Jetpack Compose UI testing environment
 * for instrumented tests.
 *
 * This plugin provisions the required Compose UI JUnit4 integration and the test manifest
 * dependency from the version catalog, enabling automated testing of composable components
 * and user interactions.
 *
 * **Usage:**
 * Apply this plugin to UI or feature modules that require automated instrumented tests for
 * Jetpack Compose screens or components.
 * ```
 * plugins {
 * alias(libs.plugins.habittracker.compose.ui.test.plugin.convention)
 * }
 * ```
 */
class ComposeUITestPluginConvention: Plugin<Project> {

    override fun apply(target: Project) {
        with(target){
            dependencies {
                add(
                    "androidTestImplementation",
                    libs.findLibrary("androidx-ui-test-manifest").get()
                )
                add(
                    "androidTestImplementation",
                    libs.findLibrary("androidx-ui-test-junit4").get()
                )
            }
        }
    }
}