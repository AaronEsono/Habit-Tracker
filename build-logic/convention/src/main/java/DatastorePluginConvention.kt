import aeb.proyecto.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.add
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.dependencies


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