plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    `maven-publish`
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
            groupId = "com.github.1fexd"
            artifactId = "android-pref-helper"
            version = "0.0.7"

            afterEvaluate {
                from(components["release"])
            }
        }
    }
}