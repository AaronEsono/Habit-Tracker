import aeb.proyecto.convention.libs
import com.android.build.gradle.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies


/**
 * A shared Gradle convention plugin that centralizes Jetpack Compose configuration
 * for Android library modules.
 *
 * This plugin applies the core Kotlin Android and the modern Kotlin Compose compiler plugins,
 * enables the Compose build feature flag, sets the compiler extension version, and configures
 * the Compose Bill of Materials (BoM) alongside standard Material 3, UI, and Navigation dependencies.
 *
 * **Usage:**
 * Apply this plugin to any feature or UI-related module that requires Jetpack Compose support.
 * ```
 * plugins {
 * alias(libs.plugins.habittracker.jetpack.compose.plugin.convention)
 * }
 * ```
 */
class JetpackComposePluginConvention: Plugin<Project> {

    override fun apply(target: Project) {
        with(target){
            with(pluginManager){
                apply("org.jetbrains.kotlin.android")
                apply("org.jetbrains.kotlin.plugin.compose")
            }


            extensions.configure<LibraryExtension>{
                buildFeatures {
                    compose = true
                }

                composeOptions {
                    kotlinCompilerExtensionVersion = "1.5.3"
                }
            }

            dependencies {
                val bom = libs.findLibrary("androidx-compose-bom").get()

                add("implementation", platform(bom))
                add("androidTestImplementation", platform(bom))

                add("implementation", libs.findLibrary("androidx-core-ktx").get())
                add("implementation", libs.findLibrary("androidx-lifecycle-runtime-ktx").get())
                add("implementation", libs.findLibrary("androidx-activity-compose").get())
                add("implementation", libs.findLibrary("androidx-material3").get())
                add("implementation", libs.findLibrary("androidx-runtime-livedata").get())
                add("implementation", libs.findLibrary("androidx-ui").get())
                add("implementation", libs.findLibrary("material3").get())
                add("implementation", libs.findLibrary("androidx-ui-graphics").get())

                add("implementation", libs.findLibrary("androidx-navigation-compose").get())
            }
        }
    }
}