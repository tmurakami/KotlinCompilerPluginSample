import org.jetbrains.intellij.tasks.PatchPluginXmlTask

plugins {
    `kotlin-jvm-convention`
    org.jetbrains.intellij
}

intellij {
    version = "2020.1"
    setPlugins("org.jetbrains.kotlin:1.3.72-release-IJ2020.1-1")
}

dependencies {
    implementation(project(":compiler", configuration = "shadow"))
}

tasks.named<PatchPluginXmlTask>("patchPluginXml") {
    changeNotes(
        """
        Add change notes here.<br>
        <em>most HTML tags may be used</em>"""
    )
}

tasks.buildSearchableOptions {
    enabled = false
}

tasks.runIde {
    jvmArgs("--add-exports", "java.base/jdk.internal.vm=ALL-UNNAMED")
}
