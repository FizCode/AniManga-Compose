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

        val properties = Properties()
        properties.load(project.rootProject.file("local.properties").inputStream())

        debug {
            buildConfigField(
                "String",
                "X_MAL_CLIENT_ID",
                "\"${properties.getProperty("X_MAL_CLIENT_ID")}\""
            )
        }
        release {
            buildConfigField(
                "String",
                "X_MAL_CLIENT_ID",
                "\"${properties.getProperty("X_MAL_CLIENT_ID")}\""
            )
        }
    }
}

dependencies {

    api(libs.ktor.client.core)
    api(libs.ktor.client.android)
    api(libs.ktor.client.okhttp)
    api(libs.ktor.serialization.kotlinx.json)
    api(libs.ktor.client.content.negotiation)

    implementation(platform(libs.koin.bom))
    implementation(libs.koin.core)

    // Chucker
    debugImplementation(libs.chucker.library)
    releaseImplementation(libs.chucker.library.no.op)
}