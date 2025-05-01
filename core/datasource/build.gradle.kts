plugins {
    alias(libs.plugins.animanga.android.library)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.animanga.hilt)
}

android {
    namespace = "dev.fizcode.datasource"
}

dependencies {

    implementation(libs.kotlinx.serialization.json)

    implementation(projects.core.network)
    implementation(projects.core.common)
}
