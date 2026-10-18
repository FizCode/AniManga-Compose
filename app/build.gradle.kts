import dev.fizcode.convention.AniMangaBuildType
import java.util.Properties

plugins {
    alias(libs.plugins.animanga.android.application)
    alias(libs.plugins.animanga.android.application.compose)
    alias(libs.plugins.animanga.android.application.flavors)
    alias(libs.plugins.android.application)
}

// The upload key of the release build. It is read from `keystore.properties` (not committed, see
// `keystore.properties.example`) or from the environment, so it never ends up in the repository.
// Without it the release build is left unsigned, which is fine for local builds.
val keystoreProperties = Properties().apply {
    rootProject.file("keystore.properties").takeIf { it.exists() }?.inputStream()?.use(::load)
}

fun signingValue(property: String, environment: String): String? =
    (keystoreProperties.getProperty(property) ?: System.getenv(environment))
        ?.takeIf { it.isNotBlank() }

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

    signingConfigs {
        val storeFile = signingValue("storeFile", "ANIMANGA_KEYSTORE_FILE")
        val storePassword = signingValue("storePassword", "ANIMANGA_KEYSTORE_PASSWORD")
        val keyAlias = signingValue("keyAlias", "ANIMANGA_KEY_ALIAS")
        val keyPassword = signingValue("keyPassword", "ANIMANGA_KEY_PASSWORD")

        if (storeFile != null && storePassword != null && keyAlias != null && keyPassword != null) {
            create("release") {
                this.storeFile = rootProject.file(storeFile)
                this.storePassword = storePassword
                this.keyAlias = keyAlias
                this.keyPassword = keyPassword
            }
        }
    }

    buildTypes {
        debug {
            applicationIdSuffix = AniMangaBuildType.DEBUG.applicationIdSuffix
        }
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            signingConfig = signingConfigs.findByName("release")
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
