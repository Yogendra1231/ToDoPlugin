plugins {
    id("org.jetbrains.intellij") version "1.17.3"
    id("java")
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

intellij {
    version.set("2023.2.2")
    type.set("IC")  // Use "IC" for Community Edition or "IU" for Ultimate Edition
    plugins.set(listOf())
}

repositories {
    mavenCentral()
    gradlePluginPortal()
    maven("https://cache-redirector.jetbrains.com/intellij-dependencies")
}

tasks {
    patchPluginXml {
        changeNotes.set("Initial version")
    }
}
