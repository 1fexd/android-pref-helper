import com.android.build.gradle.LibraryExtension
import fe.buildlogic.Version
import fe.buildlogic.extension.asProvider
import fe.buildlogic.publishing.PublicationComponent
import fe.buildlogic.publishing.publish
import fe.buildlogic.version.AndroidVersionStrategy
import net.nemerosa.versioning.VersioningExtension
import okhttp3.internal.platform.android.AndroidLogHandler.publish
import org.jetbrains.kotlin.gradle.dsl.KotlinAndroidProjectExtension

plugins {
    id("org.jetbrains.kotlin.android") apply false
    id("com.android.library") version "8.7.0" apply false
    id("org.jetbrains.kotlin.plugin.compose") apply false
    id("net.nemerosa.versioning") apply false
    id("com.gitlab.grrfe.build-logic-plugin")
    `maven-publish`
}

val baseGroup = "com.github.fexd.android.pref.helper"

subprojects {
    val isPlatform = name == "platform"
    val isTestApp = name.endsWith("test-app")

    if (!isTestApp && !isPlatform) {
        apply(plugin = "com.android.library")
        apply(plugin = "org.jetbrains.kotlin.android")
    }

    apply(plugin = "net.nemerosa.versioning")
    apply(plugin = "org.gradle.maven-publish")
    apply(plugin = "com.gitlab.grrfe.build-logic-plugin")

    val now = System.currentTimeMillis()
    val provider = AndroidVersionStrategy(now)

    val versionProvider = with(extensions["versioning"] as VersioningExtension) {
        asProvider(this@subprojects, provider)
    }

    group = baseGroup
    version = versionProvider.get()

    if (!isPlatform && !isTestApp) {
        with(extensions["kotlin"] as KotlinAndroidProjectExtension) {
            jvmToolchain(Version.JVM)
            explicitApi()
        }

        with(extensions["android"] as LibraryExtension) {
            namespace = group.toString()
            compileSdk = Version.COMPILE_SDK

            defaultConfig {
                minSdk = Version.MIN_SDK
            }

            publishing {
                multipleVariants {
                    allVariants()
                    withSourcesJar()
                }
            }
        }

        this@subprojects.dependencies {

        }
    }

    if (!isTestApp) {
        publishing.publish(
            this@subprojects,
            group.toString(),
            versionProvider,
            if (isPlatform) PublicationComponent.JavaPlatform else PublicationComponent.Android
        )
    }
}
