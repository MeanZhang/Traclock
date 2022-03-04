//buildscript {
//    val composeVersion by extra("1.2.0-alpha04")
//}

plugins {
    id("com.android.application") version "7.2.0-alpha06" apply false
    id("com.android.library") version "7.2.0-alpha06" apply false
    id("org.jetbrains.kotlin.android") version "1.6.10" apply false
}

tasks.register("clean", Delete::class.java) {

    delete(rootProject.buildDir)

}