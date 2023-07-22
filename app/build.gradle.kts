import com.android.build.gradle.internal.cxx.configure.abiOf

plugins {
    alias(libs.plugins.com.android.application)
    alias(libs.plugins.org.jetbrains.kotlin.android)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
    // TODO hilt支持ksp后，移除下面这行
    kotlin("kapt")
}

android {
    namespace = "com.mean.traclock"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.mean.traclock"
        minSdk = 26
        targetSdk = 34
        versionCode = 2
        versionName = "0.7.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
        manifestPlaceholders["APP_NAME"] = "@string/app_name"
    }

    splits {
        abi {
            isEnable = true
            reset()
            //noinspection ChromeOsAbiSupport
            include("arm64-v8a")
            isUniversalApk = false
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        debug {
            applicationIdSuffix = ".debug"
            versionNameSuffix = "-debug"
            manifestPlaceholders["APP_NAME"] = "时迹（debug）"
            splits {
                abi {
                    include("arm64-v8a", "x86_64")
                }
            }
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
        freeCompilerArgs = freeCompilerArgs + "-opt-in=kotlin.RequiresOptIn"
    }
    lint {
        checkDependencies = true
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.8"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(libs.core.ktx)
    implementation(libs.lifecycle.runtime.ktx)
    implementation(libs.activity.compose)
    implementation(platform(libs.compose.bom))
    implementation(libs.ui)
    implementation(libs.ui.graphics)
    implementation(libs.ui.tooling.preview)
    implementation(libs.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.espresso.core)
    androidTestImplementation(platform(libs.compose.bom))
    androidTestImplementation(libs.ui.test.junit4)
    debugImplementation(libs.ui.tooling)
    debugImplementation(libs.ui.test.manifest)

    // MPAndroidChart
    implementation(libs.mpandroidchart)
    // 查看内存泄漏
    debugImplementation(libs.leakcanary)
    // lottie动画
    implementation(libs.lottie.compose)
    // 日期时间选择器
    implementation(libs.material)
    implementation(libs.datetimepicker)
    // Accompanist Navigation Animation
    implementation(libs.accompanist.navigation.animation)
    // Accompanist Permissions
    implementation(libs.accompanist.permissions)
    // Room数据库
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    ksp(libs.room.compiler)
    debugImplementation(libs.glance)
    // Navigation
    implementation(libs.androidx.navigation.compose)
    // Coil（Compose的Image会缺角）
    implementation(libs.coil.compose)
    // XLog
    implementation(libs.xlog)
    // Hilt
    implementation(libs.hilt.android)
    // ksp(libs.hilt.android.compiler)
    // TODO hilt支持ksp后，移除下面这行
    kapt(libs.hilt.android.compiler)
    implementation(libs.androidx.appcompat)
    // Material Icons Extended
    implementation(libs.material.icons.extended)
    // DataStore
    implementation(libs.datastore.preferences)
    // Compose Material 2
    implementation(libs.compose.material)
}