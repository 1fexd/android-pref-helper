plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    `maven-publish`
    id("net.nemerosa.versioning") version "3.0.0"
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
        kotlinCompilerExtensionVersion = "1.5.1"
    }
}

dependencies {
    api("androidx.compose.runtime:runtime:1.5.0")
    api(project(":preference-helper"))
}

publishing {
    publications {
        register<MavenPublication>("release") {
            groupId = "fe.android-pref-helper-compose"
            version = versioning.info.tag ?: versioning.info.full

            afterEvaluate {
                from(components["release"])
            }
        }
    }
}