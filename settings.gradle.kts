pluginManagement {
    repositories {
        if (System.getenv("CI") != "true") {
            maven("https://repo.nju.edu.cn/repository/maven-public/")
        }
        maven(url = "https://repo.nju.edu.cn/repository/maven-public/")
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
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
        maven(url = "https://repo.nju.edu.cn/repository/maven-public/")
        google()
        mavenCentral()
    }
}
rootProject.name = "Traclock"
include(":app")
