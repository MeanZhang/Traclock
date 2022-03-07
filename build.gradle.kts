// buildscript {
//    val composeVersion by extra("1.2.0-alpha04")
// }

plugins {
    id("com.android.application") version "7.2.0-alpha06" apply false
    id("com.android.library") version "7.2.0-alpha06" apply false
    id("org.jetbrains.kotlin.android") version "1.6.10" apply false
    id("com.diffplug.spotless") version "6.3.0"
}

// tasks.register("clean", Delete::class.java) {
//    delete(rootProject.buildDir)
// }

configure<com.diffplug.gradle.spotless.SpotlessExtension> {
    val ktlintVersion = "0.44.0"

    kotlin {
        target("**/*.kt")
        targetExclude("$buildDir/**/*.kt", "bin/**/*.kt")
        ktlint(ktlintVersion)
    }
    kotlinGradle {
        target("*.gradle.kts")
        ktlint(ktlintVersion)
    }
}
