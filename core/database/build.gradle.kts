plugins {
    alias(libs.plugins.animanga.android.library)
    alias(libs.plugins.animanga.room)
}

android {
    namespace = "dev.fizcode.database"
}

dependencies {
    implementation(platform(libs.koin.bom))
    implementation(libs.koin.core)

    implementation(projects.core.common)

    testImplementation(libs.junit)
}
