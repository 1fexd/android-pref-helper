plugins {
    id("org.jetbrains.kotlin.plugin.compose")
}

dependencies {
    api(project(":core"))
    api(project(":compose:compose-core"))
    implementation(platform(AndroidX.compose.bom))
    implementation(AndroidX.compose.runtime)
}
