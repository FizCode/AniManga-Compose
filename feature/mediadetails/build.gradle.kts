plugins {
    alias(libs.plugins.animanga.android.library)
    alias(libs.plugins.animanga.android.library.compose)
    alias(libs.plugins.animanga.android.feature)
}

android {
    namespace = "dev.fizcode.mediadetails"
}

dependencies {

    implementation(projects.feature.mediadetailheader)
    implementation(projects.feature.mediadetailinfo)

    implementation(projects.core.common)
    implementation(projects.core.designsystem)
    implementation(projects.core.datasource)
    implementation(projects.core.datastore)
    implementation(projects.core.navigation)

}
