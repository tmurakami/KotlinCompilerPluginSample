package com.github.tmurakami.kcps.idea

import org.jetbrains.kotlin.analyzer.ModuleInfo
import org.jetbrains.kotlin.descriptors.DeclarationDescriptor
import org.jetbrains.kotlin.idea.core.unwrapModuleSourceInfo
import org.jetbrains.kotlin.idea.facet.KotlinFacet
import org.jetbrains.kotlin.resolve.descriptorUtil.module
import java.io.File

internal fun compilerPluginIntroduced(declarationDescriptor: DeclarationDescriptor): Boolean =
    declarationDescriptor
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
