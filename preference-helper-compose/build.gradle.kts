plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    `maven-publish`
}

android {
    namespace = "fe.android.preference.helper.compose"
    compileSdk = 33

    defaultConfig {
        minSdk = 21
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.7"
    }
}

dependencies {
    api("androidx.compose.runtime:runtime:1.4.3")
    api(project(":preference-helper"))
}

publishing {
    publications {
        register<MavenPublication>("release") {
            groupId = "com.github.1fexd"
            artifactId = "android-pref-helper-compose"
            version = "0.0.6"

            afterEvaluate {
                from(components["release"])
            }
        }
    }
}