plugins {
    alias(libs.plugins.animanga.android.library)
    alias(libs.plugins.animanga.android.library.compose)
    alias(libs.plugins.animanga.android.feature)
}

android {
    namespace = "dev.fizcode.dashboard"
}

dependencies {

    implementation(projects.feature.dashboard.anime.api)
    implementation(projects.feature.dashboard.bookmark.api)
    implementation(projects.feature.dashboard.seasonal.api)

    implementation(projects.core.common)
    implementation(projects.core.designsystem)
    implementation(projects.core.navigation)

}
