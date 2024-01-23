import de.fayard.refreshVersions.core.versionFor

plugins {
    id(libs.plugins.com.android.library)
    id(libs.plugins.org.jetbrains.kotlin.android)
    `maven-publish`
    id(libs.plugins.net.nemerosa.versioning)
}

val group = "fe.android.preference.helper.compose.mock"

android {
    namespace = group
    compileSdk = Version.COMPILE_SDK

    defaultConfig {
        minSdk = Version.MIN_SDK
    }

    kotlin {
        jvmToolchain(Version.JVM)
        explicitApi()
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = versionFor(AndroidX.compose.compiler)
    }

    publishing {
        multipleVariants {
            allVariants()
            withSourcesJar()
        }
    }
}

dependencies {
    // doesn't work for some bizarre reason?
//    api(AndroidX.compose.runtime)
    api("androidx.compose.runtime:runtime:1.5.4")
    api(project(":core"))
    api(project(":compose"))
}

publishing.publish(project, group, versioning.info.tag ?: versioning.info.full, PublicationComponent.RELEASE)
