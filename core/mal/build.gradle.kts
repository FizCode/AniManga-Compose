plugins {
    alias(libs.plugins.animanga.android.library)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "dev.fizcode.mal"
}

dependencies {

    implementation(platform(libs.koin.bom))
    implementation(libs.koin.core)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.kotlinx.datetime)
    implementation(projects.core.network)
    implementation(projects.core.common)

    testImplementation(libs.junit)
}
