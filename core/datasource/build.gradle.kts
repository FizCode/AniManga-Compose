plugins {
    alias(libs.plugins.animanga.android.library)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.animanga.room)
}

android {
    namespace = "dev.fizcode.datasource"
}

dependencies {

    implementation(platform(libs.koin.bom))
    implementation(libs.koin.core)
    implementation(libs.kotlinx.serialization.json)
    implementation(projects.core.network)
    implementation(projects.core.common)
}
