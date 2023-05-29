plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    `maven-publish`
    id("net.nemerosa.versioning") version "3.0.0"
}

android {
    namespace = "fe.android.preference.helper"
    compileSdk = 33

    defaultConfig {
        minSdk = 21
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

publishing {
    publications {
        register<MavenPublication>("release") {
            groupId = "android-pref-helper"
            version = versioning.info.tag ?: versioning.info.full

            afterEvaluate {
                from(components["release"])
            }
        }
    }
}