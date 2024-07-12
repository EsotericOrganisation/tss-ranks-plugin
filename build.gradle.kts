plugins {
    `java-library`
    id("io.papermc.paperweight.userdev") version "1.7.1"
    id("xyz.jpenilla.run-paper") version "2.3.0"
    id("xyz.jpenilla.resource-factory-bukkit-convention") version "1.1.1"
}

group = "net.slqmy"
version = "0.1"
description = "The plugin that manages ranks and permissions on The Slimy Swamp."

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
}

repositories {
    mavenCentral()
}

dependencies {
    compileOnly(files("../TSS-Core/build/libs/tss_core-0.1-dev-all.jar"))

    paperweight.paperDevBundle("1.21-R0.1-SNAPSHOT")
}

tasks {
    compileJava {
        options.encoding = Charsets.UTF_8.name()
        options.release.set(21)
    }

    javadoc {
        options.encoding = Charsets.UTF_8.name()
    }
}

bukkitPluginYaml {
  main = "net.slqmy.tss_ranks.TSSRanksPlugin"
  load = BukkitPluginYaml.PluginLoadOrder.STARTUP
  authors.add("Slqmy")
  apiVersion = "1.21"
}
