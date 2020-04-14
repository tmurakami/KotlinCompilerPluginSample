package com.github.tmurakami.kcps.compiler

import com.github.tmurakami.kcps.compiler.extensions.ExpressionCodegenExtensionImpl
import com.github.tmurakami.kcps.compiler.extensions.IrGenerationExtensionImpl
import com.github.tmurakami.kcps.compiler.extensions.JsSyntheticTranslateExtensionImpl
import com.github.tmurakami.kcps.compiler.extensions.StorageComponentContainerContributorImpl
import com.github.tmurakami.kcps.compiler.extensions.SyntheticResolveExtensionImpl
import com.google.auto.service.AutoService
import com.intellij.mock.MockProject
import org.jetbrains.kotlin.backend.common.extensions.IrGenerationExtension
import org.jetbrains.kotlin.codegen.extensions.ExpressionCodegenExtension
import org.jetbrains.kotlin.compiler.plugin.ComponentRegistrar
import org.jetbrains.kotlin.config.CompilerConfiguration
import org.jetbrains.kotlin.extensions.StorageComponentContainerContributor
import org.jetbrains.kotlin.js.translate.extensions.JsSyntheticTranslateExtension
import org.jetbrains.kotlin.resolve.extensions.SyntheticResolveExtension

@AutoService(ComponentRegistrar::class)
internal class ComponentRegistrarImpl : ComponentRegistrar {
    override fun registerProjectComponents(project: MockProject, configuration: CompilerConfiguration) {
        StorageComponentContainerContributor.registerExtension(project, StorageComponentContainerContributorImpl())
        SyntheticResolveExtension.registerExtension(project, SyntheticResolveExtensionImpl())
        IrGenerationExtension.registerExtension(project, IrGenerationExtensionImpl())
        ExpressionCodegenExtension.registerExtension(project, ExpressionCodegenExtensionImpl())
        JsSyntheticTranslateExtension.registerExtension(project, JsSyntheticTranslateExtensionImpl())
    }
}
