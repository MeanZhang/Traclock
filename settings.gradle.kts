pluginManagement {
    repositories {
        maven(url = "https://repo.nju.edu.cn/repository/maven-public/")
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        maven(url = "https://repo.nju.edu.cn/repository/maven-public/")
        google()
        mavenCentral()
    }
}
rootProject.name = "Traclock"
include(":app")
