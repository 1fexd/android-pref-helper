import org.jetbrains.kotlin.gradle.dsl.KotlinVersion

plugins {
    `kotlin-dsl`
}

repositories {
    gradlePluginPortal()
    maven(url = "https://plugins.gradle.org/m2")
}

dependencies {
    implementation("net.nemerosa.versioning:net.nemerosa.versioning.gradle.plugin:_")
}

kotlin {
    compilerOptions {
        languageVersion.set(KotlinVersion.KOTLIN_1_9)
        apiVersion.set(KotlinVersion.KOTLIN_1_9)
    }
}
