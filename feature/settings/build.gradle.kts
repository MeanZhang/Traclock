import dev.icerock.gradle.MRVisibility
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.moko.resources)
    alias(libs.plugins.aboutlibraries)
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
            implementation(projects.core.utils)
            implementation(projects.core.ui)
            implementation(projects.feature.backup)
            // moko-resources
            implementation((libs.moko.resources))
            implementation(libs.moko.resources.compose)
            // Material Icons扩展
            implementation(compose.materialIconsExtended)
            // Coil（Compose的Image会缺角）
            implementation(libs.coil.compose)
            implementation(libs.coil.svg)
            // Navigation
            implementation(libs.navigation.compose)
            // Koin
            implementation(libs.koin.core)
            // AboutLibraries
            implementation(libs.aboutlibraries.core)
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
    resourcesPackage.set("com.mean.traclock.settings")
    resourcesClassName.set("Res")
    resourcesVisibility.set(MRVisibility.Internal)
}

android {
    namespace = "com.mean.traclock.settings"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

aboutLibraries {
    duplicationMode = com.mikepenz.aboutlibraries.plugin.DuplicateMode.MERGE
}