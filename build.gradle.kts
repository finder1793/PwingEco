plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version "8.1.1"
    id("maven-publish")
}

group = "com.pwing"
version = "1.1.2"

repositories {
    mavenCentral()
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    maven("https://jitpack.io")
    maven("https://repo.helpch.at/releases")
    maven("https://repo.skriptlang.org/releases")
}



dependencies {
    compileOnly("org.spigotmc:spigot-api:1.20.1-R0.1-SNAPSHOT")
    compileOnly("com.github.MilkBowl:VaultAPI:1.7")
    compileOnly("me.clip:placeholderapi:2.11.6")
    compileOnly("com.github.SkriptLang:Skript:2.9.0")
    implementation("com.zaxxer:HikariCP:5.0.1")
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
