import org.jetbrains.kotlin.gradle.plugin.KotlinPlatformType

plugins {
    `auto-service`
    `shadow-jar`
}

val testJsCompileOnly by configurations.creating<Configuration> {
    attributes.attribute(KotlinPlatformType.attribute, KotlinPlatformType.js)
}

dependencies {
    implementation(project(":runtime")) { isTransitive = false }
    compileOnly(kotlin("compiler"))
    testImplementation(kotlin("compiler"))
    testJsCompileOnly(kotlin("stdlib-js") as String) { isTransitive = false }
    testJsCompileOnly(project(":runtime", configuration = "jsRuntime"))
}

tasks.named<Test>("test") {
    dependsOn(project(":runtime").tasks["jsJar"])
    systemProperty("com.github.tmurakami.kcps.compiler.jsLibraries", testJsCompileOnly.asPath)
}
