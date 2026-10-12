import dev.fizcode.convention.AniMangaBuildType

plugins {
    alias(libs.plugins.animanga.android.application)
    alias(libs.plugins.animanga.android.application.compose)
    alias(libs.plugins.animanga.android.application.flavors)
    alias(libs.plugins.android.application)
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

    implementation(projects.feature.anime.api)
    implementation(projects.feature.anime.impl)
    implementation(projects.feature.bookmark.api)
    implementation(projects.feature.bookmark.impl)
    implementation(projects.feature.manga.api)
    implementation(projects.feature.manga.impl)
    implementation(projects.feature.ranking.api)
    implementation(projects.feature.ranking.impl)
    implementation(projects.feature.seasonal.api)
    implementation(projects.feature.seasonal.impl)
    implementation(projects.feature.dashboard)
    implementation(projects.feature.mediadetails.api)
    implementation(projects.feature.mediadetails.impl)
    implementation(projects.feature.onboarding.api)
    implementation(projects.feature.onboarding.impl)
    implementation(projects.feature.search.impl)
    implementation(projects.feature.settings.api)
    implementation(projects.feature.settings.impl)

    implementation(projects.core.common)
    implementation(projects.core.designsystem)
    implementation(projects.core.database)
    implementation(projects.core.mal)
    implementation(projects.core.datastore)
    implementation(projects.core.network)
    implementation(projects.core.navigation)

    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.ui)

    testImplementation(platform(libs.koin.bom))
    testImplementation(libs.koin.test)
    testImplementation(libs.junit)
}
