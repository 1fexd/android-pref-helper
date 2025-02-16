plugins {
    id("org.jetbrains.kotlin.plugin.compose")
}

dependencies {
    api(project(":core"))
    implementation(platform(AndroidX.compose.bom))
    implementation(AndroidX.compose.runtime)
}
