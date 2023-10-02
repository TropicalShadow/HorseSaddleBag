import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.8.10"
    kotlin("plugin.serialization") version "1.8.10"
    id("com.github.johnrengelman.shadow") version "7.1.2"
    id("xyz.jpenilla.run-paper") version "2.0.0"
}

group = "club.tesseract"
version = "1.2.0"

repositories {
    mavenLocal()
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.20.1-R0.1-SNAPSHOT")
    compileOnly("club.tesseract:HorseOverhaul:2.0.2")

    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.4.1")

    implementation("org.jetbrains.exposed", "exposed-core", "0.44.0")
    implementation("org.jetbrains.exposed", "exposed-dao", "0.44.0")
    implementation("org.jetbrains.exposed", "exposed-jdbc", "0.44.0")
    api("org.xerial:sqlite-jdbc:3.43.0.0")

}

tasks{
    runServer {
        minecraftVersion("1.20.2")
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
    }
    processResources {
        filter<org.apache.tools.ant.filters.ReplaceTokens>("tokens" to mapOf("version" to project.version))
    }
}


val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions.jvmTarget = JavaVersion.VERSION_16.toString()

compileKotlin.kotlinOptions {
    freeCompilerArgs = listOf("-Xinline-classes")
}