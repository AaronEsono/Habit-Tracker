import aeb.proyecto.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies


/**
 * A shared Gradle convention plugin that configures core Firebase infrastructure and
 * modern authentication libraries for a module.
 *
 * This plugin applies the Google Services and Firebase Crashlytics Gradle plugins,
 * configures the Firebase Bill of Materials (BoM), and provisions essential services
 * such as Analytics, Auth, Firestore, and Crashlytics. Additionally, it integrates
 * the modern Android Credentials Manager and Google ID libraries for seamless user authentication.
 *
 * **Usage:**
 * Apply this plugin to the app module or specific core/data modules that interact directly
 * with Firebase services and authentication flows.
 * ```
 * plugins {
 * alias(libs.plugins.habittracker.firebase.base.plugin.convention)
 * }
 * ```
 */
class FirebaseBasePluginConvention: Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager){
                apply("com.google.gms.google-services")
                apply("com.google.firebase.crashlytics")
            }

            dependencies {
                add("implementation", platform(libs.findLibrary("firebase-bom").get()))
                add("implementation", libs.findLibrary("firebase-analytics").get())
                add("implementation", libs.findLibrary("firebase-auth-ktx").get())
                add("implementation", libs.findLibrary("firebase-firestore-ktx").get())
                add("implementation", libs.findLibrary("firebase-crashlytics").get())
                add("implementation", libs.findLibrary("androidx-credentials").get())
                add("implementation", libs.findLibrary("androidx-credentials-play-services-auth").get())
                add("implementation", libs.findLibrary("googleid").get())
            }
        }
    }
}