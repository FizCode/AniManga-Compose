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
    implementation(projects.feature.bookmark.api)
    implementation(projects.feature.mediadetails.api)

    implementation(projects.core.common)
    implementation(projects.core.designsystem)
    implementation(projects.core.database)
    implementation(projects.core.navigation)

    implementation(libs.androidx.room.paging)

}
