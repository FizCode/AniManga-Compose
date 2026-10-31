import java.util.Properties

plugins {
    alias(libs.plugins.animanga.android.library)
}

android {
    namespace = "dev.fizcode.network"

    buildFeatures {
        buildConfig = true
    }

    buildTypes {

        // The key comes from local.properties, or from the environment on CI. Debug builds work
        // without it (the API answers with an error), a release build refuses to be made without.
        val properties = Properties().apply {
            rootProject.file("local.properties").takeIf { it.exists() }?.inputStream()?.use(::load)
        }
        val malClientId = (properties.getProperty("X_MAL_CLIENT_ID") ?: System.getenv("X_MAL_CLIENT_ID"))
            .orEmpty()
        val buildsRelease = gradle.startParameter.taskNames.any {
            it.contains("release", ignoreCase = true) || it.contains("bundle", ignoreCase = true)
        }
        if (buildsRelease && malClientId.isBlank()) {
            throw GradleException(
                "X_MAL_CLIENT_ID is missing. Add it to local.properties or set it as an " +
                    "environment variable before making a release build."
            )
        }

        debug {
            buildConfigField("String", "X_MAL_CLIENT_ID", "\"$malClientId\"")
        }
        release {
            buildConfigField("String", "X_MAL_CLIENT_ID", "\"$malClientId\"")
        }
    }
}

dependencies {

    implementation(projects.core.common)

    api(libs.ktor.client.core)
    implementation(libs.ktor.client.okhttp)
    implementation(libs.ktor.serialization.kotlinx.json)
    implementation(libs.ktor.client.content.negotiation)

    implementation(platform(libs.koin.bom))
    implementation(libs.koin.core)

    testImplementation(libs.junit)

    // Chucker
    debugImplementation(libs.chucker.library)
    releaseImplementation(libs.chucker.library.no.op)
}