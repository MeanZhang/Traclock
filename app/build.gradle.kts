val composeVersion by extra("1.2.0-beta03")

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.devtools.ksp") version "1.6.21-1.0.5"
}

android {
    compileSdk = 32

    defaultConfig {
        applicationId = "com.mean.traclock"
        minSdk = 21
        targetSdk = 32
        versionCode = 1
        versionName = "0.6.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    splits {
        abi {
            isEnable = true
            reset()
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
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
        freeCompilerArgs = freeCompilerArgs + "-opt-in=kotlin.RequiresOptIn"
    }
    lint {
        checkDependencies = true
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = composeVersion
    }
    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    val roomVersion = "2.4.2"
    val accompanistVersion = "0.24.10-beta"
    val workVersion = "2.7.1"
    val navVersion = "2.4.1"

    // MPAndroidChart
    implementation("com.github.PhilJay:MPAndroidChart:3.1.0")
    // 查看内存泄漏
    debugImplementation("com.squareup.leakcanary:leakcanary-android:2.9.1")
    // lottie动画
    implementation("com.airbnb.android:lottie-compose:5.2.0")
    // 日期时间选择器
    implementation("com.google.android.material:material:1.5.0")
    implementation("com.github.loperSeven:DateTimePicker:0.5.7")
    // accompanist
    implementation("com.google.accompanist:accompanist-systemuicontroller:$accompanistVersion")
    implementation("com.google.accompanist:accompanist-navigation-animation:$accompanistVersion")
    // Room数据库
    implementation("androidx.room:room-runtime:$roomVersion")
    implementation("androidx.room:room-ktx:$roomVersion")
    ksp("androidx.room:room-compiler:$roomVersion")
    // WorkManager
    implementation("androidx.work:work-runtime-ktx:$workVersion")
    // Navigation
    implementation("androidx.navigation:navigation-compose:$navVersion")
    // Compose
    implementation("androidx.compose.material:material-icons-extended:$composeVersion")
    implementation("androidx.compose.ui:ui:$composeVersion")
    implementation("androidx.compose.material:material:$composeVersion")
    implementation("androidx.compose.material3:material3:1.0.0-alpha13")
    implementation("androidx.compose.ui:ui-tooling-preview:$composeVersion")
    // ThreeTenABP
    implementation("com.jakewharton.threetenabp:threetenabp:1.4.0")
    // MMKV
    implementation("com.tencent:mmkv:1.2.13")
    // Coil（Compose的Image会缺角）
    implementation("io.coil-kt:coil-compose:2.1.0")
    // Logger
    implementation("com.orhanobut:logger:2.2.0")
    // 友盟
    // debugImplementation("com.tencent.bugly:crashreport:4.0.4")
    debugImplementation("com.umeng.umsdk:common:9.5.0")
    debugImplementation("com.umeng.umsdk:asms:1.6.3")
    debugImplementation("com.umeng.umsdk:apm:1.6.2")

    implementation("androidx.appcompat:appcompat:1.4.1")

    implementation("androidx.core:core-ktx:1.7.0")
    implementation("androidx.fragment:fragment-ktx:1.4.1")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.4.1")
    implementation("androidx.activity:activity-compose:1.4.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.3")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.4.0")
    androidTestImplementation("androidx.compose.ui:ui-test-junit4:$composeVersion")
    debugImplementation("androidx.compose.ui:ui-tooling:$composeVersion")
    debugImplementation("androidx.compose.ui:ui-test-manifest:$composeVersion")
}