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

include("core")
include("compose")

if (System.getenv("JITPACK")?.toBooleanStrictOrNull() != false) {
    include("testapp")
}
