package dev.fizcode.convention

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinAndroidProjectExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinBaseExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension

/**
 * Single source of truth for the SDK levels used by every convention plugin.
 */
internal object AndroidSdk {
    const val COMPILE = 37
    const val MIN = 24
    const val TARGET = COMPILE
}

/**
 * Configure base Kotlin with Android options
 */
internal fun Project.configureKotlinAndroid(
    commonExtension: CommonExtension
) {
    commonExtension.apply {
        compileSdk = AndroidSdk.COMPILE

        defaultConfig.minSdk = AndroidSdk.MIN

        compileOptions.sourceCompatibility = JavaVersion.VERSION_11
        compileOptions.targetCompatibility = JavaVersion.VERSION_11
    }

    configureKotlin<KotlinAndroidProjectExtension>()
}

/**
 * Configure base Kotlin options for JVM (non-Android)
 */
internal fun Project.configureKotlinJvm() {
    extensions.configure<JavaPluginExtension> {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    configureKotlin<KotlinJvmProjectExtension>()
}

private inline fun <reified T : KotlinBaseExtension> Project.configureKotlin() = configure<T> {
    // Treat all Kotlin warnings as errors (disabled by default)
    // Override by setting warningsAsErrors=true in your ~/.gradle/gradle.properties
    val warningsAsErrors = providers.gradleProperty("warningsAsErrors")
        .map { it.toBoolean() }
        .orElse(false)
    when (this) {
        is KotlinAndroidProjectExtension -> compilerOptions
        is KotlinJvmProjectExtension -> compilerOptions
        else -> error(
            "Unsupported project extension: ${this::class.simpleName}. " +
                    "Please add support in configureKotlin()."
        )
    }.apply {
        jvmTarget.set(JvmTarget.JVM_11)
        allWarningsAsErrors.set(warningsAsErrors)
        freeCompilerArgs.add(
            // Enable experimental coroutines APIs, including Flow
            "-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
        )
    }
}
