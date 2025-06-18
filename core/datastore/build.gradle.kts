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

android {
    namespace = "com.mean.traclock.datastore"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    // FIXME
    lint {
        disable += "NullSafeMutableLiveData"
    }
}
