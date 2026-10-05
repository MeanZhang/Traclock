import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeCompiler)
}

kotlin {
    compilerOptions {
        jvmTarget = JvmTarget.JVM_17
    }
}

dependencies {
    implementation(project(":shared"))
    implementation(project(":core:timer"))
    implementation(project(":core:utils"))
    implementation(project(":core:ui"))
    implementation(project(":core:designsystem"))

    implementation(libs.androidx.activity.compose)
    implementation(libs.compose.runtime)
    implementation(libs.compose.ui)
    implementation(libs.compose.foundation)
    implementation(libs.compose.material3)
    implementation(libs.compose.uiToolingPreview)
    debugImplementation(libs.compose.uiTooling)
    // Accompanist Permissions
    implementation(libs.accompanist.permissions)
    // Koin
    implementation(libs.koin.android)
    implementation(libs.koin.androidx.startup)
    implementation(libs.koin.compose)
    // FileKit（文件选择）
    implementation(libs.filekit.dialogs.compose)
    // Kermit 日志
    implementation(libs.kermit)
}

android {
    namespace = "com.mean.traclock"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "com.mean.traclock"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 4
        versionName = "1.3.0"
        vectorDrawables {
            useSupportLibrary = true
        }
        manifestPlaceholders["APP_NAME"] = "@string/app_name"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    signingConfigs {
        create("release") {
            storeFile = file("../traclock.jks")
            storePassword = env.fetch("KEYSTORE_PASSWORD")
            keyPassword = env.fetch("KEY_PASSWORD")
            keyAlias = env.fetch("KEY_ALIAS")
        }
        create("debug-mean") {
            storeFile = file("../debug-mean.jks")
            storePassword = "debug-mean"
            keyPassword = "debug-mean"
            keyAlias = "debug-mean"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("release")
        }
        getByName("debug") {
            applicationIdSuffix = ".debug"
            versionNameSuffix = "-debug"
            manifestPlaceholders["APP_NAME"] = "时迹（debug）"
            signingConfig = signingConfigs.getByName("debug-mean")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
}
