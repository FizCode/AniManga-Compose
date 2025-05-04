plugins {
    alias(libs.plugins.animanga.android.library)
    alias(libs.plugins.animanga.android.library.compose)
    alias(libs.plugins.animanga.android.feature)
}

android {
    namespace = "dev.fizcode.dashboard"
}

dependencies {

    implementation(projects.feature.dashboard.anime)

    implementation(projects.core.common)
    implementation(projects.core.designsystem)
    implementation(projects.core.navigation)

}
