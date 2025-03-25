plugins {
    // this is necessary to avoid the plugins to be loaded multiple times
    // in each subproject's classloader
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.androidLibrary) apply false
    alias(libs.plugins.jetbrainsCompose) apply false
    alias(libs.plugins.compose.compiler) apply false
    alias(libs.plugins.kotlinMultiplatform) apply false
    alias(libs.plugins.dotenv)
    alias(libs.plugins.spotless)
    alias(libs.plugins.aboutlibraries) apply false
}

spotless {
    val ktlintVersion = libs.versions.ktlint.get()

    kotlin {
        target("**/*.kt")
        targetExclude("${project.layout.buildDirectory}/**/*.kt", "bin/**/*.kt", "core/timepicker/**/*.kt")
        ktlint(ktlintVersion).setEditorConfigPath("$projectDir/.editorconfig").customRuleSets(
            listOf(
//                "io.nlopez.compose.rules:ktlint:${libs.versions.composeRules.get()}",
            ),
        )
    }
    kotlinGradle {
        target("**.gradle.kts")
        ktlint(ktlintVersion)
    }
}
