import aeb.proyecto.convention.libs
import com.android.build.gradle.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies


/**
 * A shared Gradle convention plugin that centralizes testing configurations and
 * dependencies across Android library modules.
 *
 * This plugin sets up the default [AndroidJUnitRunner] for instrumented tests and
 * provisions standard testing frameworks for both local unit tests ([JUnit], [MockK],
 * [Mockito], [Kotlinx Coroutines Test]) and UI/Instrumented tests ([Espresso]).
 *
 * **Usage:**
 * Apply this plugin to any module that requires unit testing or Android instrumented testing infrastructure.
 * ```
 * plugins {
 * alias(libs.plugins.habittracker.testing.plugin.convention)
 * }
 * ```
 */
class TestingPluginConvention: Plugin<Project> {
    override fun apply(target: Project) {
        with(target){

            extensions.configure<LibraryExtension>{
                defaultConfig {
                    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
                }
            }
            dependencies {
                add("testImplementation", libs.findLibrary("junit").get())
                add("androidTestImplementation", libs.findLibrary("androidx-junit").get())
                add("androidTestImplementation", libs.findLibrary("androidx-ui-test-junit4").get())
                add("androidTestImplementation", libs.findLibrary("androidx-espresso-core").get())
                add("androidTestImplementation", libs.findLibrary("kotlinx-coroutines-test").get())
                add("androidTestImplementation", libs.findLibrary("mockk-mockk").get())
                add("androidTestImplementation", libs.findLibrary("turbine").get())
                add("androidTestImplementation", libs.findLibrary("mockito-mockito-core").get())
                add("androidTestImplementation", libs.findLibrary("mockito-kotlin").get())
                add("testImplementation", libs.findLibrary("mockk-mockk").get())
                add("testImplementation", libs.findLibrary("kotlinx-coroutines-test").get())
                add("testImplementation", libs.findLibrary("mockito-mockito-core").get())
                add("testImplementation", libs.findLibrary("mockito-kotlin").get())
                add("testImplementation", libs.findLibrary("turbine").get())
                add("testImplementation", libs.findLibrary("roboelectric").get())
            }
        }
    }
}