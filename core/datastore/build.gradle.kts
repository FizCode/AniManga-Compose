plugins {
    alias(libs.plugins.animanga.android.library)
}

android {
    namespace = "dev.fizcode.datastore"
}

dependencies {
    implementation(projects.core.common)

    api(libs.androidx.datastore)
    implementation(platform(libs.koin.bom))
    implementation(libs.koin.core)

}
