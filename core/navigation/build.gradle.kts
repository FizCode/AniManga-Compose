plugins {
    alias(libs.plugins.animanga.android.library)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.animanga.koin)
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
