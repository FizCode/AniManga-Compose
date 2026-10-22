plugins {
    alias(libs.plugins.animanga.android.library)
    alias(libs.plugins.animanga.android.library.compose)
    alias(libs.plugins.animanga.android.feature)
}

android {
    namespace = "dev.fizcode.ranking"
}

dependencies {
    implementation(projects.feature.ranking.api)
    implementation(projects.feature.mediadetails.api)

    implementation(projects.core.common)
    implementation(projects.core.designsystem)
    implementation(projects.core.mal)
    implementation(projects.core.network)
    implementation(projects.core.navigation)

}
