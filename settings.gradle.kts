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
include(":feature:anime:api")
include(":feature:anime:impl")
include(":feature:bookmark:api")
include(":feature:bookmark:impl")
include(":feature:manga:api")
include(":feature:manga:impl")
include(":feature:ranking:api")
include(":feature:ranking:impl")
include(":feature:seasonal:api")
include(":feature:seasonal:impl")
include(":feature:dashboard")
include(":feature:mediadetails:api")
include(":feature:mediadetails:impl")
include(":feature:onboarding:api")
include(":feature:onboarding:impl")
include(":feature:settings:api")
include(":feature:settings:impl")
include(":feature:search:api")
include(":feature:search:impl")

include(":core:common")
include(":core:database")
include(":core:datasource")
include(":core:datastore")
include(":core:designsystem")
include(":core:network")
include(":core:navigation")
