import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidMultiplatformLibrary)
}

kotlin {
    jvm()
    android {
        namespace = "com.mean.traclock.data"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()

        compilerOptions {
            jvmTarget = JvmTarget.JVM_17
        }
    }
    sourceSets {
        commonMain.dependencies {
            implementation(projects.core.utils)
            api(projects.core.model)
            implementation(projects.core.database)
            implementation(projects.core.datastore)
            // DateTime
            implementation(libs.kotlinx.datetime)
            // 协程
            implementation(libs.kotlinx.coroutines)
            // Koin
            implementation(libs.koin.core)
        }
    }
}
