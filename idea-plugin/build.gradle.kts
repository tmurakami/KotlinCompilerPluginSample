import org.jetbrains.intellij.tasks.PatchPluginXmlTask

plugins {
    `kotlin-jvm-convention`
    org.jetbrains.intellij
}

intellij {
    version = "2019.3.3"
    setPlugins("org.jetbrains.kotlin:1.3.70-release-IJ2019.3-1")
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
