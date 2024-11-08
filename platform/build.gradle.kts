import fe.buildsrc.publishing.PublicationComponent
import fe.buildsrc.publishing.asProvider
import fe.buildsrc.publishing.publish

plugins {
    `java-platform`
    `maven-publish`
    id("net.nemerosa.versioning")
}

val group = "com.github.1fexd.composekit"

dependencies {
    constraints {
        rootProject.allprojects.filter { it != rootProject && it != project }.forEach { project ->
            api("$group:${project.name}:${project.version}")
        }
    }
}

publishing.publish(
    project,
    group.toString(),
    versioning.asProvider(project),
    PublicationComponent.JAVA_PLATFORM
)
