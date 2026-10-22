plugins {
    alias(libs.plugins.animanga.android.library)
    alias(libs.plugins.animanga.android.library.compose)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "dev.fizcode.common"
}

dependencies {

    implementation(libs.kotlinx.datetime)
    implementation(libs.kotlinx.collections.immutable)

    testImplementation(libs.junit)
}
