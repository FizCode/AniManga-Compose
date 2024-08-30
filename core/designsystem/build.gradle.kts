plugins {
    alias(libs.plugins.animanga.android.library)
    alias(libs.plugins.animanga.android.library.compose)
}

android {
    namespace = "dev.fizcode.designsystem"

    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
}

dependencies {
    api(libs.androidx.compose.material3)
    api(libs.androidx.compose.ui.graphics)
    api(libs.androidx.compose.ui.tooling)
    api(libs.androidx.compose.ui.tooling.preview)
}
