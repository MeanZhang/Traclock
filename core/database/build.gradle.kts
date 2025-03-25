import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.room)
    alias(libs.plugins.ksp)
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
            implementation(projects.core.utils)
            // Datetime
            implementation(libs.kotlinx.datetime)
            // Room
            implementation(libs.room.runtime)
            implementation(libs.sqlite.bundled)
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
    namespace = "com.mean.traclock.database"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

room {
    schemaDirectory("$projectDir/schemas")
}

dependencies {
    ksp(libs.room.compiler)
}

