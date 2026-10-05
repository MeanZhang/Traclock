import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidMultiplatformLibrary)
    alias(libs.plugins.room)
    alias(libs.plugins.ksp)
}

kotlin {
    jvm()
    android {
        namespace = "com.mean.traclock.database"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()

        compilerOptions {
            jvmTarget = JvmTarget.JVM_17
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

room3 {
    schemaDirectory("$projectDir/schemas")
}

dependencies {
    add("kspCommonMainMetadata", libs.room.compiler)
    add("kspAndroid", libs.room.compiler)
    add("kspJvm", libs.room.compiler)
}

