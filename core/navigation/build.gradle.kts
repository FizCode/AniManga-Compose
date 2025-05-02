plugins {
    alias(libs.plugins.animanga.android.library)
    alias(libs.plugins.kotlin.serialization)
}


android {
    namespace = "dev.fizcode.navigation"

    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
}

dependencies {
    implementation(libs.kotlinx.serialization.json)
}
