plugins {
    `auto-service`
    `java-gradle-plugin`
    `maven-publish`
}

gradlePlugin {
    plugins {
        create("simplePlugin") {
            id = group as String
            implementationClass = "$id.gradle.PluginImpl"
        }
    }
}

dependencies {
    implementation(kotlin("gradle-plugin-api"))
}
