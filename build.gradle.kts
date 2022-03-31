// buildscript {
//    val composeVersion by extra("1.2.0-alpha04")
// }

plugins {
    val agpVersion = "7.2.0-beta02"
    id("com.android.application") version agpVersion apply false
    id("com.android.library") version agpVersion apply false
    id("org.jetbrains.kotlin.android") version "1.6.10" apply false
    id("com.diffplug.spotless") version "6.4.1"
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
        target("**.gradle.kts")
        ktlint(ktlintVersion)
    }
}
