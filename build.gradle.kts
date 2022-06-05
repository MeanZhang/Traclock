// buildscript {
//    val composeVersion by extra("1.2.0-alpha04")
// }

plugins {
    val agpVersion = "7.2.0"
    id("com.android.application") version agpVersion apply false
    id("com.android.library") version agpVersion apply false
    id("org.jetbrains.kotlin.android") version "1.6.21" apply false
    id("com.diffplug.spotless") version "6.7.0"
}

// tasks.register("clean", Delete::class.java) {
//    delete(rootProject.buildDir)
// }

configure<com.diffplug.gradle.spotless.SpotlessExtension> {
    val ktlintVersion = "0.45.2"

    kotlin {
        target("**/*.kt")
        targetExclude("$buildDir/**/*.kt", "bin/**/*.kt")
        ktlint(ktlintVersion).setUseExperimental(true)
    }
    kotlinGradle {
        target("**.gradle.kts")
        ktlint(ktlintVersion)
    }
}
