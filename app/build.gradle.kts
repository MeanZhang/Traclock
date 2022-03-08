val composeVersion by extra("1.2.0-alpha04")

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.devtools.ksp") version "1.6.10-1.0.4"
}

android {
    compileSdk = 32

    defaultConfig {
        applicationId = "com.mean.traclock"
        minSdk = 24
        targetSdk = 32
        versionCode = 1
        versionName = "dev.0.5.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
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
    val accompanistVersion = "0.24.3-alpha"
    val workVersion = "2.7.1"

    implementation("androidx.compose.material:material:$composeVersion")

    implementation("com.github.PhilJay:MPAndroidChart:v3.1.0")

    //查看数据库
    debugImplementation("com.guolindev.glance:glance:1.1.0")
    //查看内存泄漏
    debugImplementation("com.squareup.leakcanary:leakcanary-android:2.8.1")
    //lottie动画
    implementation("com.airbnb.android:lottie-compose:5.0.3")
    //日期时间选择器
    implementation("com.google.android.material:material:1.5.0")
    implementation("com.github.loperSeven:DateTimePicker:0.5.4")
    //accompanist
    implementation("com.google.accompanist:accompanist-systemuicontroller:$accompanistVersion")
    //Room数据库
    implementation("androidx.room:room-runtime:$roomVersion")
    implementation("androidx.room:room-ktx:$roomVersion")
    ksp("androidx.room:room-compiler:$roomVersion")
    //WorkManager
    implementation("androidx.work:work-runtime-ktx:$workVersion")

    implementation("androidx.fragment:fragment-ktx:1.4.1")

    implementation("androidx.compose.material:material-icons-extended:$composeVersion")

    implementation("androidx.appcompat:appcompat:1.4.1")

    implementation("androidx.core:core-ktx:1.7.0")
    implementation("androidx.compose.ui:ui:$composeVersion")
    implementation("androidx.compose.material3:material3:1.0.0-alpha06")
    implementation("androidx.compose.ui:ui-tooling-preview:$composeVersion")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.4.1")
    implementation("androidx.activity:activity-compose:1.4.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.3")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.4.0")
    androidTestImplementation("androidx.compose.ui:ui-test-junit4:$composeVersion")
    debugImplementation("androidx.compose.ui:ui-tooling:$composeVersion")
    debugImplementation("androidx.compose.ui:ui-test-manifest:$composeVersion")
}