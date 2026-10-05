import dev.icerock.gradle.MRVisibility
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
        namespace = "com.mean.traclock.statistic"
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
            implementation(projects.core.data)
            implementation(projects.core.utils)
            implementation(projects.core.ui)
            implementation(projects.core.designsystem)
            // Navigation
            implementation(libs.navigation3.runtime)
            // Datetime
            implementation(libs.kotlinx.datetime)
            // Koin
            implementation(libs.koin.core)
            implementation(libs.koin.compose.viewmodel)
            // Kermit
            implementation(libs.kermit)
            // moko-resources
            implementation((libs.moko.resources))
            implementation(libs.moko.resources.compose)
            // vico
            implementation(libs.vico.compose)
            implementation(libs.vico.compose.m3)
            // Material Icons扩展
            implementation(libs.material.icons.extended)
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
    resourcesPackage.set("com.mean.traclock.statistic")
    resourcesClassName.set("Res")
    resourcesVisibility.set(MRVisibility.Internal)
}


