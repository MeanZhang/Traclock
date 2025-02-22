import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.ksp)
    alias(libs.plugins.room)
}

kotlin {

    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
        }
    }

//    jvm("desktop")

    sourceSets {
//        val desktopMain by getting

        androidMain.dependencies {
            implementation(compose.preview)
            implementation(libs.androidx.activity.compose)
            // Accompanist Permissions
            implementation(libs.accompanist.permissions)
            // material-components
            implementation(libs.androidx.material)
        }
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodel)
            implementation(libs.androidx.lifecycle.runtime.compose)
            // Material Icons扩展
            implementation(compose.materialIconsExtended)
            // Navigation
            implementation(libs.navigation)
            // Datetime
            implementation(libs.kotlinx.datetime)
            // Room
            implementation(libs.room.runtime)
            implementation(libs.sqlite.bundled)
            // Coil（Compose的Image会缺角）
            implementation(libs.coil.compose)
            implementation(libs.coil.svg)
            // DataStore
            implementation(libs.datastore.preferences.core)
            // Kermit日志
            implementation(libs.kermit)
            // TimePicker
            implementation(project(":timepicker"))
            // FileKit
            implementation(libs.filekit.compose)
            // Koala Plot
            implementation(libs.koalaplot.core)
            // vico
            implementation(libs.vico)
        }
//        desktopMain.dependencies {
//            implementation(compose.desktop.currentOs)
//            implementation(libs.kotlinx.coroutines.swing)
//        }
    }
}

android {
    namespace = "com.mean.traclock"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    sourceSets["main"].res.srcDirs("src/androidMain/res")
    sourceSets["main"].resources.srcDirs("src/commonMain/resources")

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
    applicationVariants.configureEach {
        outputs.configureEach {
            val newFileName = "${rootProject.name.replace(" ", "_")}-${name}-${versionName}.apk"
            (this as com.android.build.gradle.internal.api.BaseVariantOutputImpl).outputFileName =
                newFileName
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
    dependencies {
        debugImplementation(compose.uiTooling)
    }
}

room {
    schemaDirectory("$projectDir/schemas")
}

compose.desktop {
    application {
        mainClass = "com.mean.traclock.MainKt"

        jvmArgs += listOf("-Dfile.encoding=GBK")

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "Traclock"
            packageVersion = "1.0.0"
            // FIXME Exception in thread "main" java.lang.NoClassDefFoundError: sun/misc/Unsafe
            modules("jdk.unsupported")

            windows {
                iconFile.set(project.file("src/commonMain/composeResources/drawable/logo.ico"))
            }
        }
    }
}


dependencies {
    ksp(libs.room.compiler)
}
