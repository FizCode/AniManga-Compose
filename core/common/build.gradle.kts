plugins {
    alias(libs.plugins.animanga.android.library)
}

android {
    namespace = "dev.fizcode.common"
}

dependencies {

    implementation(libs.kotlinx.collections.immutable)

    testImplementation(libs.junit)
}
