import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidMultiplatformLibrary)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
}

kotlin {
    jvm()

    android {
        namespace = "com.mean.traclock.shared"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()

        compilerOptions {
            jvmTarget = JvmTarget.JVM_17
        }
    }

    sourceSets {
        androidMain.dependencies {
            implementation(libs.compose.uiToolingPreview)
            implementation(libs.androidx.activity.compose)
            // Accompanist Permissions
            implementation(libs.accompanist.permissions)
            // Koin
            implementation(libs.koin.android)
            implementation(libs.koin.androidx.startup)
        }
        commonMain.dependencies {
            implementation(libs.compose.runtime)
            implementation(libs.compose.foundation)
            implementation(libs.compose.material3)
            implementation(libs.compose.ui)
            implementation(libs.compose.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            // 其他模块
            implementation(projects.core.ui)
            implementation(projects.core.designsystem)
            implementation(projects.core.timer)
            implementation(projects.core.data)
            implementation(projects.feature.backup)
            implementation(projects.feature.home)
            implementation(projects.feature.project)
            implementation(projects.feature.record)
            implementation(projects.feature.settings)
            implementation(projects.feature.statistic)
            // Navigation
            implementation(libs.navigation3.runtime)
            implementation(libs.navigation3.ui)
            implementation(libs.lifecycle.viewmodel.navigation3)
            // Datetime
            implementation(libs.kotlinx.datetime)
            // Kermit日志
            implementation(libs.kermit)
            // Koin
            implementation(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel)
        }
    }
}
