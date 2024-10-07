import xyz.jpenilla.resourcefactory.bukkit.BukkitPluginYaml

plugins {
    `java-library`
    `maven-publish`
    id("io.papermc.paperweight.userdev") version "1.7.1"
    id("xyz.jpenilla.run-paper") version "2.3.0"
    id("xyz.jpenilla.resource-factory-bukkit-convention") version "1.1.1"
}

group = "org.esoteric_organisation"
version = "0.1"
description = "The Minecraft plugin that manages ranks and permissions on The Slimy Swamp Minecraft server."

val projectNameString = rootProject.name

val projectGroupString = group.toString()
val projectVersionString = version.toString()

val javaVersion = 21
val javaVersionEnumMember = JavaVersion.valueOf("VERSION_$javaVersion")

java {
    sourceCompatibility = javaVersionEnumMember
    targetCompatibility = javaVersionEnumMember

    toolchain.languageVersion.set(JavaLanguageVersion.of(javaVersion))
}

repositories {
    mavenCentral()
    maven("https://jitpack.io")
}

dependencies {
    compileOnly("com.github.EsotericOrganisation:tss-core-plugin:0.1.6:dev-all")

    paperweight.paperDevBundle("1.21-R0.1-SNAPSHOT")
}

tasks {
    compileJava {
        options.release.set(javaVersion)
    }

    javadoc {
        options.encoding = Charsets.UTF_8.name()
    }
}

bukkitPluginYaml {
  main = "org.esoteric_organisation.tss_ranks_plugin.TSSRanksPlugin"
  load = BukkitPluginYaml.PluginLoadOrder.STARTUP
  authors.addAll("Esoteric Organisation", "Esoteric Enderman")
  apiVersion = "1.21"
  description = "The Minecraft plugin that manages ranks and permissions on The Slimy Swamp Minecraft server."
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
            groupId = projectGroupString
            artifactId = projectNameString
            version = projectVersionString
        }
    }
}

tasks.named("publishMavenJavaPublicationToMavenLocal") {
    dependsOn(tasks.named("build"))
}
