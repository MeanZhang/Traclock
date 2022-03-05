// buildscript {
//    val composeVersion by extra("1.2.0-alpha04")
// }

plugins {
    id("com.android.application") version "7.2.0-alpha06" apply false
    id("com.android.library") version "7.2.0-alpha06" apply false
    id("org.jetbrains.kotlin.android") version "1.6.10" apply false
    id("com.diffplug.spotless") version "5.7.0"
}

// tasks.register("clean", Delete::class.java) {
//    delete(rootProject.buildDir)
// }

configure<com.diffplug.gradle.spotless.SpotlessExtension> {
    kotlin {
        target("**/*.kt")
        targetExclude("$buildDir/**/*.kt", "bin/**/*.kt")
        ktlint("0.43.2")
//        licenseHeader("/* (C)\$YEAR */")
    }
    kotlinGradle {
        target("*.gradle.kts")
        ktlint("0.43.2")
    }
}
