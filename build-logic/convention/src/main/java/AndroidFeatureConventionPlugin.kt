import com.android.build.gradle.LibraryExtension
import dev.fizcode.convention.configureGradleManagedDevices
import dev.fizcode.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

class AndroidFeatureConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply {
                apply("animanga.android.library")
            }

            extensions.configure<LibraryExtension> {
                testOptions.animationsDisabled = true
                configureGradleManagedDevices(this)
            }

            dependencies {
                add("implementation", libs.findLibrary("androidx.lifecycle.runtime.compose").get())
                add("implementation", libs.findLibrary("androidx.lifecycle.viewModel.compose").get())
                add("implementation", libs.findLibrary("androidx.tracing.ktx").get())

                add("androidTestImplementation", libs.findLibrary("androidx.lifecycle.runtime.testing").get())
            }
        }
    }
}
