import fe.buildsrc.Version
import fe.buildsrc.publishing.PublicationComponent
import fe.buildsrc.publishing.asProvider
import fe.buildsrc.publishing.publish

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.compose")
    id("net.nemerosa.versioning")
    `maven-publish`
}

val group = "fe.android.preference.helper.compose"

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

    publishing {
        multipleVariants {
            allVariants()
            withSourcesJar()
        }
    }
}

dependencies {
    api(project(":core"))
    implementation(platform(AndroidX.compose.bom))
    implementation(AndroidX.compose.runtime)
}

publishing.publish(project, group, versioning.asProvider(project), PublicationComponent.RELEASE)
