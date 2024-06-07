pluginManagement {
    repositories {
        if (System.getenv("CI") != "true") {
            maven("https://repo.nju.edu.cn/repository/maven-public/")
        }
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        if (System.getenv("CI") != "true") {
            maven("https://repo.nju.edu.cn/repository/maven-public/")
        }
        google()
        mavenCentral()
        maven("https://jitpack.io")
    }
}
rootProject.name = "Traclock"
include(":app")
