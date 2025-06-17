plugins {
    alias(libs.plugins.animanga.android.library)
}

android {
    namespace = "dev.fizcode.datastore"
}

dependencies {

    api(libs.androidx.datastore)
    implementation(platform(libs.koin.bom))
    implementation(libs.koin.core)

}
