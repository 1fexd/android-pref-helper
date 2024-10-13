import fe.buildsrc.Version
import fe.buildsrc.publishing.PublicationComponent
import fe.buildsrc.publishing.asProvider
import fe.buildsrc.publishing.publish

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("net.nemerosa.versioning")
    `maven-publish`
}

val group = "fe.android.preference.helper"

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

    dependencies {
        implementation(AndroidX.preference)
        implementation(AndroidX.preference.ktx)
    }

    publishing {
        multipleVariants {
            allVariants()
            withSourcesJar()
        }
    }
}

publishing.publish(project, group, versioning.asProvider(project), PublicationComponent.RELEASE)
