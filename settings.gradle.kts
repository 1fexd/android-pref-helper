pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

rootProject.name = "android-pref-helper"

include("preference-helper")
include("preference-helper-compose")

if (System.getenv("DISABLE_TESTING_MODULE")?.toBooleanStrictOrNull() != false) {
    include(":testapp")
}