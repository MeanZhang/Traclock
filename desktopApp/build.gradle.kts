import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
}

dependencies {
    implementation(project(":shared"))
    implementation(project(":core:timer"))
    implementation(project(":core:resources"))
    implementation(project(":core:ui"))
    implementation(project(":core:designsystem"))

    implementation(compose.desktop.currentOs)
    implementation(libs.compose.components.resources)
    implementation(libs.compose.material3)
    implementation(libs.kotlinx.coroutinesSwing)
    implementation(libs.moko.resources.compose)
    implementation(libs.koin.core)
    implementation(libs.koin.compose)
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
        }
    }
}
