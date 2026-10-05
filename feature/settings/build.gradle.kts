import dev.icerock.gradle.MRVisibility
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidMultiplatformLibrary)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.moko.resources)
    alias(libs.plugins.aboutlibraries)
}

kotlin {
    jvm()
    android {
        namespace = "com.mean.traclock.settings"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()

        compilerOptions {
            jvmTarget = JvmTarget.JVM_17
        }
    }
    sourceSets {
        val jvmMain by getting
        commonMain.dependencies {
            implementation(libs.compose.runtime)
            implementation(libs.compose.foundation)
            implementation(libs.compose.material3)
            implementation(libs.compose.ui)
            implementation(libs.compose.uiToolingPreview)
            implementation(libs.androidx.lifecycle.runtimeCompose)
            // 其他模块
            implementation(projects.core.resources)
            implementation(projects.core.model)
            implementation(projects.core.utils)
            implementation(projects.core.ui)
            implementation(projects.core.designsystem)
            implementation(projects.feature.backup)
            // moko-resources
            implementation((libs.moko.resources))
            implementation(libs.moko.resources.compose)
            // Material Icons扩展
            implementation(libs.material.icons.extended)
            // Coil（Compose的Image会缺角）
            implementation(libs.coil.compose)
            implementation(libs.coil.svg)
            // Navigation
            implementation(libs.navigation3.runtime)
            // Koin
            implementation(libs.koin.core)
            // AboutLibraries
            implementation(libs.aboutlibraries.core)
        }
        jvmMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutinesSwing)
        }
        androidMain.dependencies {
            implementation(libs.compose.uiToolingPreview)
            implementation(libs.androidx.activity.compose)
        }
    }
}

multiplatformResources {
    resourcesPackage.set("com.mean.traclock.settings")
    resourcesClassName.set("Res")
    resourcesVisibility.set(MRVisibility.Internal)
}
