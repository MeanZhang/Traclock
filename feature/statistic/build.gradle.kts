import dev.icerock.gradle.MRVisibility
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.compose.compiler)
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
        val desktopMain by getting
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.androidx.lifecycle.runtime.compose)
            // 其他模块
            implementation(projects.core.common)
            implementation(projects.core.model)
            implementation(projects.core.data)
            implementation(projects.core.utils)
            implementation(projects.core.ui)
            // Datetime
            implementation(libs.kotlinx.datetime)
            // Koin
            implementation(libs.koin.core)
            implementation(libs.koin.compose.viewmodel.navigation)
            // Kermit
            implementation(libs.kermit)
            // moko-resources
            implementation((libs.moko.resources))
            implementation(libs.moko.resources.compose)
            // Koala Plot
            implementation(libs.koalaplot.core)
            // vico
            implementation(libs.vico)
            // Material Icons扩展
            implementation(compose.materialIconsExtended)
        }
        desktopMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutines.swing)
        }
        androidMain.dependencies {
            implementation(compose.preview)
            implementation(libs.androidx.activity.compose)
        }
    }
}

multiplatformResources {
    resourcesPackage.set("com.mean.traclock.statistic")
    resourcesClassName.set("Res")
    resourcesVisibility.set(MRVisibility.Internal)
}

android {
    namespace = "com.mean.traclock.statistic"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}
