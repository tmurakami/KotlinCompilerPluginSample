import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    `java-library`
    `maven-publish`
    com.github.johnrengelman.shadow
}

tasks.named<ShadowJar>("shadowJar") {
    archiveClassifier.set("")
}

publishing {
    publications {
        create<MavenPublication>("shadow") {
            shadow.component(this)
        }
    }
}
