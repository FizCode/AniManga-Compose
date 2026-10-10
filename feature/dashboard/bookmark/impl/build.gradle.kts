plugins {
    alias(libs.plugins.animanga.android.library)
    alias(libs.plugins.animanga.android.library.compose)
    alias(libs.plugins.animanga.android.feature)
    alias(libs.plugins.animanga.room)
}

android {
    namespace = "dev.fizcode.bookmark"
}

dependencies {
    implementation(projects.feature.dashboard.bookmark.api)
    implementation(projects.feature.mediadetails.api)

    implementation(projects.core.common)
    implementation(projects.core.designsystem)
    implementation(projects.core.datasource)
    implementation(projects.core.navigation)

    implementation(libs.androidx.room.paging)

}
