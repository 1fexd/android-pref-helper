pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }

    plugins {
        id("de.fayard.refreshVersions") version "0.60.3"
    }
}

@Suppress("UnstableApiUsage")
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

plugins {
    id("de.fayard.refreshVersions")
}

rootProject.name = "android-pref-helper"

include("preference-helper")
include("preference-helper-compose")

if (System.getenv("DISABLE_TESTING_MODULE")?.toBooleanStrictOrNull() != false) {
    include("testapp")
}
