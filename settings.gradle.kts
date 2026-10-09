pluginManagement {
    includeBuild("build-logic")
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

rootProject.name = "AniManga"
include(":app")
include(":feature:dashboard:anime")
include(":feature:dashboard:bookmark")
include(":feature:dashboard:dashboard")
include(":feature:mediadetails")
include(":feature:onboarding")

include(":core:common")
include(":core:datasource")
include(":core:datastore")
include(":core:designsystem")
include(":core:network")
include(":core:navigation")
include(":core:data")
