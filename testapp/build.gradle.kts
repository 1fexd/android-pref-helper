import fe.buildsrc.Version

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.compose")
    id("net.nemerosa.versioning")
}

android {
    namespace = "fe.android.preference.helper.testapp"
    compileSdk = Version.COMPILE_SDK

    defaultConfig {
        applicationId = "fe.linksheet.testapp"
        minSdk = 24
        compileSdk = Version.COMPILE_SDK
        targetSdk = Version.COMPILE_SDK
        versionCode = (System.currentTimeMillis() / 1000).toInt()
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    kotlin {
        jvmToolchain(Version.JVM)
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(AndroidX.core.ktx)
    implementation(AndroidX.lifecycle.runtime.ktx)
    implementation(AndroidX.activity.compose)
    implementation(platform(AndroidX.compose.bom))
    implementation(AndroidX.compose.ui)
    implementation(AndroidX.compose.ui.graphics)
    implementation(AndroidX.compose.ui.toolingPreview)
    implementation(AndroidX.compose.material3)

    implementation(project(":core"))
    implementation(project(":compose"))
}
