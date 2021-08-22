pluginManagement {
    repositories {
        maven(url = "https://maven.fabricmc.net")
        gradlePluginPortal()
    }
}

include("gradle-plugin")
include("mod")

rootProject.name = "fabric-datagen"
