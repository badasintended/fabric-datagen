plugins {
    id("fabric-loom") version "0.8-SNAPSHOT"
}

dependencies {
    minecraft("com.mojang:minecraft:${property("minecraft")}")
    mappings("net.fabricmc:yarn:${property("yarn")}:v2")

    modImplementation("net.fabricmc:fabric-loader:${property("loader")}")
}

tasks.processResources {
    inputs.property("version", project.version)

    filesMatching("fabric.mod.json") {
        expand("version" to project.version)
    }
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            artifact(tasks.remapJar.get()) {
                classifier = null
            }
            artifact(tasks.sourcesJar.get()) {
                builtBy(tasks.remapSourcesJar.get())
                classifier = "sources"
            }
        }
    }
}
