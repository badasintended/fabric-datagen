plugins {
    id("java")
    id("maven-publish")
}

val env: Map<String, String> = System.getenv()

subprojects {
    apply(plugin = "java")
    apply(plugin = "maven-publish")

    group = "lol.bai.${rootProject.name}"
    version = env["VERSION"] ?: "local"

    java {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
        withSourcesJar()
    }

    tasks.withType<JavaCompile> {
        options.encoding = "UTF-8"
        options.release.set(8)
    }

    publishing {
        repositories {
            maven {
                url = uri("https://gitlab.com/api/v4/projects/25106863/packages/maven")
                name = "GitLab"
                credentials(HttpHeaderCredentials::class) {
                    name = "Private-Token"
                    value = env["GITLAB_TOKEN"]
                }
                authentication {
                    create<HttpHeaderAuthentication>("header")
                }
            }
        }
    }
}
