plugins {
    alias(libs.plugins.animanga.android.library)
}

android {
    namespace = "dev.fizcode.common"
}

dependencies {

    api(libs.androidx.lifecycle.viewmodel)
    implementation(libs.kotlinx.collections.immutable)

    testImplementation(libs.junit)
}
