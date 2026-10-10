plugins {
    alias(libs.plugins.animanga.android.library)
    alias(libs.plugins.animanga.android.library.compose)
    alias(libs.plugins.animanga.android.feature)
}

android {
    namespace = "dev.fizcode.anime"
}

dependencies {
    implementation(projects.feature.dashboard.anime.api)
    implementation(projects.feature.mediadetails.api)
    implementation(projects.feature.search.api)

    implementation(projects.core.common)
    implementation(projects.core.designsystem)
    implementation(projects.core.datasource)
    implementation(projects.core.navigation)

}
