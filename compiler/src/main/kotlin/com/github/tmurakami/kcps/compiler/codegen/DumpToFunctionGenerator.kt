package com.github.tmurakami.kcps.compiler.codegen

import com.github.tmurakami.kcps.compiler.resolve.Names
import com.github.tmurakami.kcps.compiler.resolve.findDumpToExtensionFunctionDescriptor
import com.github.tmurakami.kcps.compiler.resolve.isDumpable
import org.jetbrains.kotlin.descriptors.CallableMemberDescriptor
import org.jetbrains.kotlin.descriptors.ClassDescriptor
import org.jetbrains.kotlin.descriptors.FunctionDescriptor
import org.jetbrains.kotlin.incremental.components.NoLookupLocation
import org.jetbrains.kotlin.types.KotlinType
import org.jetbrains.kotlin.types.checker.KotlinTypeChecker
import org.jetbrains.kotlin.types.typeUtil.equalTypesOrNulls

internal interface DumpToFunctionGenerator {
    fun FunctionDescriptor.generate(calleeDescriptor: FunctionDescriptor)
}

internal fun ClassDescriptor.generateDumpToFunctionIfNeeded(generator: () -> DumpToFunctionGenerator) {
    if (!isDumpable()) return
    val calleeDescriptor = findDumpToExtensionFunctionDescriptor()
    val typeParameters = calleeDescriptor.typeParameters
    val valueParameters = calleeDescriptor.valueParameters
    val typeChecker = KotlinTypeChecker.DEFAULT
    fun List<Pair<KotlinType, KotlinType>>.isSameTypes() = all { (a, b) -> typeChecker.equalTypes(a, b) }
    unsubstitutedMemberScope
        .getContributedFunctions(Names.Functions.DUMP_TO, NoLookupLocation.FROM_BACKEND)
        .asSequence()
        .filter { it.kind == CallableMemberDescriptor.Kind.SYNTHESIZED }
        .filter { it.dispatchReceiverParameter?.type == defaultType }
        .filter { it.extensionReceiverParameter == null }
        .filter { it.typeParameters.size == typeParameters.size }
        .filter { it.typeParameters.zip(typeParameters) { a, b -> a.defaultType to b.defaultType }.isSameTypes() }
        .filter { it.valueParameters.size == valueParameters.size }
        .filter { it.valueParameters.zip(valueParameters) { a, b -> a.type to b.type }.isSameTypes() }
        .single { typeChecker.equalTypesOrNulls(it.returnType, calleeDescriptor.returnType) }
        .run { generator().run { generate(calleeDescriptor) } }
}
