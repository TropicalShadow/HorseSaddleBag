import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.7.20"
    kotlin("plugin.serialization") version "1.7.20"
    id("com.github.johnrengelman.shadow") version "7.1.2"
    id("xyz.jpenilla.run-paper") version "2.0.0"
}

group = "club.tesseract"
version = "1.0.0"

repositories {
    mavenCentral()
    maven { url = uri("https://repo.papermc.io/repository/maven-public/") }
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.19.2-R0.1-SNAPSHOT")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.4.1")

    implementation("org.jetbrains.exposed", "exposed-core", "0.40.1")
    implementation("org.jetbrains.exposed", "exposed-dao", "0.40.1")
    implementation("org.jetbrains.exposed", "exposed-jdbc", "0.40.1")
    implementation("org.xerial:sqlite-jdbc:3.40.0.0")

}

tasks{
    runServer {

        minecraftVersion("1.19.2")
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