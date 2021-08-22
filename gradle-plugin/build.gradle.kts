plugins {
    id("java-gradle-plugin")
}

repositories {
    maven(url = "https://maven.fabricmc.net")
    gradlePluginPortal()
}

dependencies {
    implementation(gradleApi())

    compileOnly("net.fabricmc:fabric-loom:${property("loom")}")
}

gradlePlugin {
    plugins {
        create("fabric-datagen") {
            id = "lol.bai.fabric-datagen"
            implementationClass = "lol.bai.datagen.gradle.api.DatagenPlugin"
        }
    }
}

tasks.processResources {
    inputs.property("version", project.version)

    filesMatching("modversion") {
        expand("version" to project.version)
    }
}
