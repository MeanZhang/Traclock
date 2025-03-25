import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.compose.compiler)
}

kotlin {
    jvm("desktop")
    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
        }
    }
    sourceSets {
        commonMain.dependencies {
            implementation(compose.ui)
            implementation(projects.core.common)
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

android {
    namespace = "com.mean.traclock.notifications"
    compileSdk = libs.versions.android.compileSdk.get().toInt()
    sourceSets["main"].res.srcDirs("src/androidMain/res")

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    buildFeatures {
        buildConfig = true
    }
}
