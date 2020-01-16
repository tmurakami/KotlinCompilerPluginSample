package com.github.tmurakami.kcps.compiler.codegen

import com.github.tmurakami.kcps.compiler.resolve.Names
import com.github.tmurakami.kcps.compiler.resolve.findDumpToExtensionFunctionDescriptor
import com.github.tmurakami.kcps.compiler.resolve.isDumpable
import org.jetbrains.kotlin.descriptors.CallableMemberDescriptor
import org.jetbrains.kotlin.descriptors.ClassDescriptor
import org.jetbrains.kotlin.descriptors.FunctionDescriptor
import org.jetbrains.kotlin.incremental.components.NoLookupLocation

internal interface DumpToFunctionGenerator {
    fun generateIfDumpable(thisDescriptor: ClassDescriptor)
}

internal abstract class AbstractDumpToFunctionGenerator protected constructor() : DumpToFunctionGenerator {
    final override fun generateIfDumpable(thisDescriptor: ClassDescriptor) {
        if (!thisDescriptor.isDumpable()) return
        val calleeDescriptor = thisDescriptor.findDumpToExtensionFunctionDescriptor()
        val calleeTypeParameters = calleeDescriptor.typeParameters
        val calleeValueParameters = calleeDescriptor.valueParameters
        thisDescriptor
            .unsubstitutedMemberScope
            .getContributedFunctions(Names.Functions.DUMP_TO, NoLookupLocation.FROM_BACKEND)
            .asSequence()
            .filter { it.kind == CallableMemberDescriptor.Kind.SYNTHESIZED }
            .filter { it.typeParameters.size == calleeTypeParameters.size }
            .filter { it.valueParameters.size == calleeValueParameters.size }
            .filter { it.extensionReceiverParameter == null }
            .filter { it.dispatchReceiverParameter?.type == thisDescriptor.defaultType }
            .filter { calleeDescriptor.returnType == it.returnType }
            .filter { it.typeParameters.zip(calleeTypeParameters).all { (a, b) -> a.defaultType == b.defaultType } }
            .single { it.valueParameters.zip(calleeValueParameters).all { (a, b) -> a.type == b.type } }
            .run { generate(calleeDescriptor) }
    }

    protected abstract fun FunctionDescriptor.generate(calleeDescriptor: FunctionDescriptor)
}
