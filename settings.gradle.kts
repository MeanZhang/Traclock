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

include(":shared")
include(":androidApp")
include(":desktopApp")
include(":core:timepicker")
include(":core:utils")
include(":core:model")
include(":core:database")
include(":core:datastore")
include(":core:timer")
include(":core:data")
include(":core:resources")
include(":core:designsystem")
include(":core:ui")
include(":feature:backup")
include(":feature:statistic")
include(":feature:settings")
include(":feature:home")
include(":feature:record")
include(":feature:project")
