plugins {
    com.github.`ben-manes`.versions
}

allprojects {
    repositories {
        mavenCentral()
    }
}

subprojects {
    apply(plugin = "ktlint-convention")
    group = "com.github.tmurakami.kcps"
    version = "0.0.1"
}
