plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "com.pwing"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    maven("https://jitpack.io")
    maven("https://repo.extendedclip.com/content/repositories/placeholderapi/")
}

dependencies {
    compileOnly("org.spigotmc:spigot-api:1.20.1-R0.1-SNAPSHOT")
    compileOnly("com.github.MilkBowl:VaultAPI:1.7")
    compileOnly("me.clip:placeholderapi:2.11.3")
}

tasks {
    shadowJar {
        archiveClassifier.set("")
    }
    
    processResources {
        filesMatching("plugin.yml") {
            expand(project.properties)
        }
    }
    
    compileJava {
        options.encoding = "UTF-8"
        options.release.set(17)
    }
}