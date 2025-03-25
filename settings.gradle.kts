rootProject.name = "Traclock"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
        maven("https://jitpack.io")
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev/")
    }
}

include(":composeApp")
include(":core:timepicker")
include(":core:utils")
include(":core:model")
include(":core:database")
include(":core:datastore")
include(":core:timer")
include(":core:data")
include(":core:common")
include(":core:ui")
include(":feature:backup")
include(":feature:statistic")
include(":feature:settings")
