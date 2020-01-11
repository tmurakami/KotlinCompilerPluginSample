package com.github.tmurakami.kcps.idea

import com.github.tmurakami.kcps.compiler.registerComponents
import com.intellij.openapi.components.ProjectComponent
import com.intellij.openapi.project.Project
import org.jetbrains.kotlin.analyzer.ModuleInfo
import org.jetbrains.kotlin.idea.core.unwrapModuleSourceInfo
import org.jetbrains.kotlin.idea.facet.KotlinFacet
import org.jetbrains.kotlin.resolve.descriptorUtil.module
import java.io.File

internal class IdeaProjectComponent(private val project: Project) : ProjectComponent {
    override fun initComponent() = project.registerComponents { classDescriptor ->
        classDescriptor
            .module
            .getCapability(ModuleInfo.Capability)
            ?.unwrapModuleSourceInfo()
            ?.module
            ?.let { KotlinFacet.get(it) }
            ?.configuration
            ?.settings
            ?.compilerArguments
            ?.pluginClasspaths
            ?.any { File(it).name.run { startsWith("compiler-embeddable-") && endsWith(".jar") } } ?: false
    }
}
