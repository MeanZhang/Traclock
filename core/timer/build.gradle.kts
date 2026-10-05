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
        namespace = "com.mean.traclock.notifications"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()

        androidResources {
            enable = true
        }

        compilerOptions {
            jvmTarget = JvmTarget.JVM_17
        }
    }
    sourceSets {
        commonMain.dependencies {
            implementation(libs.compose.ui)
            implementation(projects.core.resources)
            implementation(projects.core.model)
            implementation(projects.core.data)
            //DateTime
            implementation(libs.kotlinx.datetime)
            // 协程
            implementation(libs.kotlinx.coroutines)
            // Kermit
            implementation(libs.kermit)
            // Koin
            implementation(libs.koin.core)
        }
        androidMain.dependencies {
            implementation(libs.androidx.activity.compose)
            // Koin
            implementation(libs.koin.android)
        }
    }
}


