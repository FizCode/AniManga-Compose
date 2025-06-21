import dev.fizcode.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class KoinConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("com.google.devtools.ksp")

            dependencies {
                val bom = libs.findLibrary("koin-bom").get()
                "implementation"(platform(bom).get())
                "implementation"(libs.findLibrary("koin.compose").get())
//                "implementation"(libs.findLibrary("koin.androidx.viewmodel").get())
                "implementation"(libs.findLibrary("koin.compose.navigation").get())
                "implementation"(libs.findLibrary("koin.core").get())
            }
        }
    }
}
