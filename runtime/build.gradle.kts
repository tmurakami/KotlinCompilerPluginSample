import org.jetbrains.kotlin.gradle.plugin.mpp.AbstractKotlinNativeTargetPreset

plugins {
    `kotlin-multiplatform`
    `maven-publish`
}

kotlin {
    js {
        nodejs()
        browser()
        compilations.configureEach {
            kotlinOptions.sourceMap = true
        }
        compilations["main"].kotlinOptions {
            sourceMapEmbedSources = "always"
            moduleKind = "umd"
        }
        compilations["test"].kotlinOptions.moduleKind = "commonjs"
    }
    jvm {
        compilations.configureEach {
            kotlinOptions.jvmTarget = "1.8"
        }
    }
    presets.withType<AbstractKotlinNativeTargetPreset<*>>().forEach {
        targetFromPreset(it)
    }
    sourceSets {
        commonMain {
            dependencies {
                implementation(kotlin("stdlib-common"))
            }
        }
        commonTest {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }
        named("jsMain") {
            dependencies {
                implementation(kotlin("stdlib-js"))
            }
        }
        named("jsTest") {
            dependencies {
                implementation(kotlin("test-js"))
            }
        }
        named("jvmMain") {
            dependencies {
                implementation(kotlin("stdlib-jdk8"))
            }
        }
        named("jvmTest") {
            dependencies {
                implementation(kotlin("test-junit"))
            }
        }
    }
}
