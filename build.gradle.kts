import com.android.build.api.dsl.LibraryExtension
import fe.buildlogic.ProjectInfo
import fe.buildlogic.Version
import fe.buildlogic.extension.asProvider
import fe.buildlogic.extension.getReleaseVersion
import fe.buildlogic.fixGroup
import fe.buildlogic.publishing.PublicationComponent
import fe.buildlogic.publishing.PublicationName
import fe.buildlogic.publishing.publish
import fe.buildlogic.version.AndroidVersionStrategy
import net.nemerosa.versioning.VersioningExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinAndroidProjectExtension

plugins {
    id("org.jetbrains.kotlin.android") apply false
    id("org.jetbrains.kotlin.plugin.compose") apply false
    id("com.android.library") apply false
    id("net.nemerosa.versioning") apply false
    id("com.gitlab.grrfe.build-logic-plugin")
    `maven-publish`
}

val baseGroup = "com.github.fexd.androidprefhelper"

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
    version = versionProvider.getReleaseVersion()

    if (!isPlatform && !isTestApp) {
        with(extensions["kotlin"] as KotlinAndroidProjectExtension) {
            jvmToolchain(Version.JVM)
            explicitApi()
        }

        with(extensions["android"] as LibraryExtension) {
            namespace = baseGroup
            compileSdk = Version.COMPILE_SDK

            defaultConfig {
                minSdk = Version.MIN_SDK
            }

            publishing {
                multipleVariants {
                    singleVariant(PublicationName.Release)
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
            if (isPlatform) PublicationComponent.JavaPlatform else PublicationComponent.Android,
            PublicationName.Release
        )
    }
}
