import aeb.proyecto.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class TestingPluginConvention: Plugin<Project> {
    override fun apply(target: Project) {
        with(target){
            dependencies {
                add("testImplementation", libs.findLibrary("junit").get())
                add("androidTestImplementation", libs.findLibrary("androidx-junit").get())
                add("androidTestImplementation", libs.findLibrary("androidx-espresso-core").get())
                add("testImplementation", libs.findLibrary("mockk-mockk").get())
                add("testImplementation", libs.findLibrary("kotlinx-coroutines-test").get())
                add("testImplementation", libs.findLibrary("mockito-mockito-core").get())
                add("testImplementation", libs.findLibrary("mockito-kotlin").get())
            }
        }
    }
}