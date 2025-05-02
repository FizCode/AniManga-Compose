plugins {
    alias(libs.plugins.animanga.android.library)
    alias(libs.plugins.animanga.hilt)
}

android {
    namespace = "dev.fizcode.datastore"
}

dependencies {

    api(libs.androidx.datastore)

}
