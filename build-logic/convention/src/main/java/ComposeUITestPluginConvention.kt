import aeb.proyecto.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

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