plugins {
    alias(libs.plugins.animanga.android.library)
    alias(libs.plugins.animanga.android.library.compose)
    alias(libs.plugins.animanga.android.feature)
}

android {
    namespace = "dev.fizcode.settings"
}

dependencies {
    implementation(projects.feature.settings.api)

    implementation(projects.core.common)
    implementation(projects.core.datastore)
    implementation(projects.core.designsystem)
    implementation(projects.core.navigation)

}
