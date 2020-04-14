package com.github.tmurakami.kcps.compiler.extensions

import com.github.tmurakami.kcps.compiler.ALWAYS_ENABLED
import com.github.tmurakami.kcps.compiler.PluginEnabledOn
import com.github.tmurakami.kcps.compiler.resolve.Names
import com.github.tmurakami.kcps.compiler.resolve.findDumpToExtensionFunctionDescriptor
import com.github.tmurakami.kcps.compiler.resolve.isDumpable
import org.jetbrains.kotlin.descriptors.CallableMemberDescriptor
import org.jetbrains.kotlin.descriptors.ClassDescriptor
import org.jetbrains.kotlin.descriptors.Modality
import org.jetbrains.kotlin.descriptors.SimpleFunctionDescriptor
import org.jetbrains.kotlin.descriptors.Visibilities
import org.jetbrains.kotlin.descriptors.annotations.Annotations
import org.jetbrains.kotlin.descriptors.impl.SimpleFunctionDescriptorImpl
import org.jetbrains.kotlin.name.Name
import org.jetbrains.kotlin.resolve.BindingContext
import org.jetbrains.kotlin.resolve.extensions.SyntheticResolveExtension

class SyntheticResolveExtensionImpl(private val pluginEnabledOn: PluginEnabledOn = ALWAYS_ENABLED) :
    SyntheticResolveExtension {
    override fun getSyntheticFunctionNames(thisDescriptor: ClassDescriptor): List<Name> =
        if (pluginEnabledOn(thisDescriptor) && thisDescriptor.isDumpable()) {
            listOf(Names.Functions.DUMP_TO)
        } else {
            super.getSyntheticFunctionNames(thisDescriptor)
        }

    override fun generateSyntheticMethods(
        thisDescriptor: ClassDescriptor,
        name: Name,
        bindingContext: BindingContext,
        fromSupertypes: List<SimpleFunctionDescriptor>,
        result: MutableCollection<SimpleFunctionDescriptor>
    ) {
        if (!pluginEnabledOn(thisDescriptor) || name != Names.Functions.DUMP_TO || !thisDescriptor.isDumpable()) return
        val descriptor = SimpleFunctionDescriptorImpl.create(
            thisDescriptor,
            Annotations.EMPTY,
            name,
            CallableMemberDescriptor.Kind.SYNTHESIZED,
            thisDescriptor.source
        )
        val calleeDescriptor = thisDescriptor.findDumpToExtensionFunctionDescriptor()
        descriptor.initialize(
            null,
            thisDescriptor.thisAsReceiverParameter,
            calleeDescriptor.typeParameters,
            calleeDescriptor.valueParameters.map { it.copy(descriptor, it.name, 0) },
            calleeDescriptor.returnType,
            Modality.FINAL,
            Visibilities.PUBLIC
        )
        result += descriptor
    }
}
