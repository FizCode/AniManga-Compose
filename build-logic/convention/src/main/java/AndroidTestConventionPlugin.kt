import com.android.build.api.dsl.TestExtension
import dev.fizcode.convention.configureGradleManagedDevices
import dev.fizcode.convention.AndroidSdk
import dev.fizcode.convention.configureKotlinAndroid
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class AndroidTestConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.test")
            }

            extensions.configure<TestExtension> {
                configureKotlinAndroid(this)
                defaultConfig.targetSdk = AndroidSdk.TARGET
                configureGradleManagedDevices(this)
            }
        }
    }
}
