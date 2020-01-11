import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    `shadow-jar`
}

dependencies {
    implementation(project(":compiler", configuration = "shadow"))
}

tasks.named<ShadowJar>("shadowJar") {
    relocate("com.intellij", "org.jetbrains.kotlin.com.intellij")
}
