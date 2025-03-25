import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
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
            // Datetime
            implementation(libs.kotlinx.datetime)
            // Koin
            implementation(libs.koin.core)
            // moko-resources
            implementation(libs.moko.resources)
        }
    }
}

android {
    namespace = "com.mean.traclock.utils"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}
