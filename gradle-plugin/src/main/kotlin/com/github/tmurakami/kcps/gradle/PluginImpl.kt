package com.github.tmurakami.kcps.gradle

import com.google.auto.service.AutoService
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.compile.AbstractCompile
import org.jetbrains.kotlin.gradle.dsl.KotlinCommonOptions
import org.jetbrains.kotlin.gradle.plugin.KotlinCompilation
import org.jetbrains.kotlin.gradle.plugin.KotlinGradleSubplugin
import org.jetbrains.kotlin.gradle.plugin.SubpluginArtifact
import org.jetbrains.kotlin.gradle.plugin.SubpluginOption

private const val GROUP_ID = "com.github.tmurakami.kcps"
private const val VERSION = "0.0.1"
private const val COMPILER_PLUGIN_ID = "$GROUP_ID.compiler"
private const val DEPENDENCY_RUNTIME = "$GROUP_ID:runtime:$VERSION"
private val PLUGIN_ARTIFACT = SubpluginArtifact(GROUP_ID, "compiler-embeddable", VERSION)
private val PLUGIN_ARTIFACT_NATIVE = SubpluginArtifact(GROUP_ID, "compiler", VERSION)

internal class PluginImpl : Plugin<Project> {
    override fun apply(target: Project) = target.configurations.configureEach {
        if (it.name == "implementation" || it.name == "commonMainImplementation") {
            it.dependencies += target.dependencies.create(DEPENDENCY_RUNTIME)
        }
    }
}

@AutoService(KotlinGradleSubplugin::class)
internal class KotlinGradleSubpluginImpl : KotlinGradleSubplugin<AbstractCompile> {
    override fun apply(
        project: Project,
        kotlinCompile: AbstractCompile,
        javaCompile: AbstractCompile?,
        variantData: Any?,
        androidProjectHandler: Any?,
        kotlinCompilation: KotlinCompilation<KotlinCommonOptions>?
    ): List<SubpluginOption> = listOf(SubpluginOption("enabled", "true"))

    override fun isApplicable(project: Project, task: AbstractCompile): Boolean =
        project.plugins.hasPlugin(PluginImpl::class.java)

    override fun getCompilerPluginId(): String = COMPILER_PLUGIN_ID
    override fun getPluginArtifact(): SubpluginArtifact = PLUGIN_ARTIFACT
    override fun getNativeCompilerPluginArtifact(): SubpluginArtifact? = PLUGIN_ARTIFACT_NATIVE
}
