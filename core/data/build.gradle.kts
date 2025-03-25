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
            implementation(projects.core.utils)
            api(projects.core.database)
            api(projects.core.datastore)
            // DateTime
            implementation(libs.kotlinx.datetime)
            // 协程
            implementation(libs.kotlinx.coroutines)
            // Koin
            implementation(libs.koin.core)
        }
    }
}

android {
    namespace = "com.mean.traclock.data"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}
