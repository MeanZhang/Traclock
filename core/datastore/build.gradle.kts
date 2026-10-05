import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidMultiplatformLibrary)
}

kotlin {
    jvm()
    android {
        namespace = "com.mean.traclock.datastore"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()

        compilerOptions {
            jvmTarget = JvmTarget.JVM_17
        }
    }
    sourceSets {
        commonMain.dependencies {
            api(projects.core.model)
            // DateStore
            implementation(libs.datastore.preferences.core)
            // DataTime
            implementation(libs.kotlinx.datetime)
            // Koin
            implementation(libs.koin.core)
        }
        androidMain.dependencies {
            // Koin
            implementation(libs.koin.android)
        }
    }
}


