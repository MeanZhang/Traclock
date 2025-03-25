import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.moko.resources)
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
            api(libs.moko.resources)
        }
    }
}

multiplatformResources {
    resourcesPackage.set("com.mean.traclock")
    resourcesClassName.set("CommonRes")
}

android {
    namespace = "com.mean.traclock.common"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}
