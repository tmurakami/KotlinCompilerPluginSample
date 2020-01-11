package com.github.tmurakami.kcps.compiler

import com.github.tmurakami.kcps.compiler.diagnostic.DeclarationCheckerImpl
import com.github.tmurakami.kcps.compiler.extensions.ExpressionCodegenExtensionImpl
import com.github.tmurakami.kcps.compiler.extensions.IrGenerationExtensionImpl
import com.github.tmurakami.kcps.compiler.extensions.JsSyntheticTranslateExtensionImpl
import com.github.tmurakami.kcps.compiler.extensions.SyntheticResolveExtensionImpl
import com.intellij.openapi.project.Project
import org.jetbrains.kotlin.backend.common.extensions.IrGenerationExtension
import org.jetbrains.kotlin.codegen.extensions.ExpressionCodegenExtension
import org.jetbrains.kotlin.container.StorageComponentContainer
import org.jetbrains.kotlin.container.useInstance
import org.jetbrains.kotlin.descriptors.ClassDescriptor
import org.jetbrains.kotlin.descriptors.ModuleDescriptor
import org.jetbrains.kotlin.extensions.StorageComponentContainerContributor
import org.jetbrains.kotlin.js.translate.extensions.JsSyntheticTranslateExtension
import org.jetbrains.kotlin.platform.TargetPlatform
import org.jetbrains.kotlin.resolve.extensions.SyntheticResolveExtension

typealias PluginEnabledOn = (classDescriptor: ClassDescriptor) -> Boolean

fun Project.registerComponents(pluginEnabledOn: PluginEnabledOn) {
    StorageComponentContainerContributor.registerExtension(this, object : StorageComponentContainerContributor {
        override fun registerModuleComponents(
            container: StorageComponentContainer,
            platform: TargetPlatform,
            moduleDescriptor: ModuleDescriptor
        ) = container.useInstance(DeclarationCheckerImpl(pluginEnabledOn))
    })
    SyntheticResolveExtension.registerExtension(this, SyntheticResolveExtensionImpl(pluginEnabledOn))
    IrGenerationExtension.registerExtension(this, IrGenerationExtensionImpl(pluginEnabledOn))
    ExpressionCodegenExtension.registerExtension(this, ExpressionCodegenExtensionImpl(pluginEnabledOn))
    JsSyntheticTranslateExtension.registerExtension(this, JsSyntheticTranslateExtensionImpl(pluginEnabledOn))
}
