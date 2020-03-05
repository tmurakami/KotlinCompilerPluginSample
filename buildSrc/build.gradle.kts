plugins {
    `kotlin-dsl`
}

kotlinDslPluginOptions.experimentalWarning.set(false)

repositories {
    gradlePluginPortal()
    mavenCentral()
    jcenter()
}

dependencies {
    api(kotlin("gradle-plugin", version = "1.3.70"))
    api("org.jlleitschuh.gradle:ktlint-gradle:9.1.1")
    api("com.github.jengelman.gradle.plugins:shadow:5.2.0")
    api("gradle.plugin.org.jetbrains.intellij.plugins:gradle-intellij-plugin:0.4.15")
    api("com.github.ben-manes:gradle-versions-plugin:0.27.0")
}
