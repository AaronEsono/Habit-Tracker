import aeb.proyecto.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class JetpackComposePluginConvention: Plugin<Project> {

    override fun apply(target: Project) {
        with(target){
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