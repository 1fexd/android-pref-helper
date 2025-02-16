plugins {
    `java-platform`
}

dependencies {
    constraints {
        rootProject.allprojects.filter { it != rootProject && it != project }.forEach { project ->
            api("$group:${project.name}:${project.version}")
        }
    }
}
