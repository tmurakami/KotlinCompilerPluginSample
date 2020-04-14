package com.github.tmurakami.kcps.compiler.extensions

import com.github.tmurakami.kcps.compiler.ALWAYS_ENABLED
import com.github.tmurakami.kcps.compiler.PluginEnabledOn
import com.github.tmurakami.kcps.compiler.diagnostic.DeclarationCheckerImpl
import org.jetbrains.kotlin.container.StorageComponentContainer
import org.jetbrains.kotlin.container.useInstance
import org.jetbrains.kotlin.descriptors.ModuleDescriptor
import org.jetbrains.kotlin.extensions.StorageComponentContainerContributor
import org.jetbrains.kotlin.platform.TargetPlatform

class StorageComponentContainerContributorImpl(private val pluginEnabledOn: PluginEnabledOn = ALWAYS_ENABLED) :
    StorageComponentContainerContributor {
    override fun registerModuleComponents(
        container: StorageComponentContainer,
        platform: TargetPlatform,
        moduleDescriptor: ModuleDescriptor
    ) = container.useInstance(DeclarationCheckerImpl(pluginEnabledOn))
}
