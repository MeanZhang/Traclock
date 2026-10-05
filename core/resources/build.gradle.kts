import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidMultiplatformLibrary)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.moko.resources)
}

kotlin {
    jvm()
    android {
        namespace = "com.mean.traclock.resources"
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
            api(libs.compose.components.resources)
            api(libs.moko.resources)
            api(libs.moko.resources.compose)
        }
    }
}

multiplatformResources {
    resourcesPackage.set("com.mean.traclock")
    resourcesClassName.set("CommonRes")
}

compose.resources {
    publicResClass = true
    packageOfResClass = "com.mean.traclock"
    generateResClass = always
}