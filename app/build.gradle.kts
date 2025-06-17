import dev.fizcode.convention.AniMangaBuildType

plugins {
    alias(libs.plugins.animanga.android.application)
    alias(libs.plugins.animanga.android.application.compose)
    alias(libs.plugins.animanga.android.application.flavors)
    alias(libs.plugins.android.application)
    alias(libs.plugins.animanga.hilt)
    alias(libs.plugins.jetbrains.kotlin.android)
}

android {
    namespace = "dev.fizcode.animanga"

    defaultConfig {
        applicationId = "dev.fizcode.animanga"
        versionCode = 1
        versionName = "0.1.0" // X.Y.Z; X = Major, Y = minor, Z = Patch level

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        debug {
            applicationIdSuffix = AniMangaBuildType.DEBUG.applicationIdSuffix
        }
        release {
            isMinifyEnabled = true
            applicationIdSuffix = AniMangaBuildType.RELEASE.applicationIdSuffix
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation(projects.feature.dashboard.dashboard)
    implementation(projects.feature.mediadetails)
    implementation(projects.feature.onboarding)

    implementation(projects.core.designsystem)
    implementation(projects.core.datasource)
    implementation(projects.core.network)
    implementation(projects.core.navigation)

    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.ui)
    implementation(libs.androidx.navigation.compose)

}
