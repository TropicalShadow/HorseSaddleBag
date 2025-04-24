import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "2.1.20"
    kotlin("plugin.serialization") version "2.1.20"
    id("io.github.goooler.shadow") version "8.1.8"
    id("xyz.jpenilla.run-paper") version "2.3.1"
}

group = "club.tesseract"
version = "1.2.2"

repositories {
    mavenCentral()

    maven("https://jitpack.io")
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.21.5-R0.1-SNAPSHOT")
    compileOnly("com.github.TropicalShadow:HorseOverhaul:18192078e8")

    implementation("org.bstats:bstats-bukkit:3.1.0")

    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.4.1")

    implementation("org.jetbrains.exposed", "exposed-core", "0.44.0")
    implementation("org.jetbrains.exposed", "exposed-dao", "0.44.0")
    implementation("org.jetbrains.exposed", "exposed-jdbc", "0.44.0")
    api("org.xerial:sqlite-jdbc:3.43.0.0")

}

tasks{
    runServer {
        minecraftVersion("1.21.5")
    }
    test {
        useJUnitPlatform {
            includeEngines("junit-jupiter")
        }
        workingDir = file("./run")
    }
    shadowJar{
        archiveBaseName.set(project.name)
        archiveClassifier.set("")

        relocate("kotlinx", "club.tesseract.horsechestsaddle.kotlinx")
        relocate("org.xerial", "club.tesseract.horsechestsaddle.xerial")
        relocate("org.bstats", "club.tesseract.horsechestsaddle.bstats")
    }
    processResources {
        filter<org.apache.tools.ant.filters.ReplaceTokens>("tokens" to mapOf("version" to project.version))
    }
}



kotlin{
    jvmToolchain(21)
}
