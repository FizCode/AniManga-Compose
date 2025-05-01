plugins {
    alias(libs.plugins.animanga.android.library)
    alias(libs.plugins.animanga.android.library.compose)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "dev.fizcode.common"
}

dependencies {

    implementation(projects.core.network)

    implementation(libs.kotlinx.serialization.json)
}
