pluginManagement {
    repositories {
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
    // no repositories set in modules
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    // for all modules
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "R3"
include(":app")
include(":core_ui")
include(":core")
include(":core_api")
include(":feature_auth")
include(":feature_auth_nav")
include(":feature_inventory")
include(":feature_inventory_nav")
include(":feature_root")
