package com.github.tmurakami.kcps.compiler.resolve

import org.jetbrains.kotlin.descriptors.ClassDescriptor
import org.jetbrains.kotlin.descriptors.FunctionDescriptor
import org.jetbrains.kotlin.psi.KtAnnotationEntry
import org.jetbrains.kotlin.resolve.descriptorUtil.module
import org.jetbrains.kotlin.resolve.lazy.descriptors.LazyAnnotationDescriptor
import org.jetbrains.kotlin.resolve.scopes.DescriptorKindFilter

internal fun ClassDescriptor.isDumpable(): Boolean =
    isData && annotations.hasAnnotation(FqNames.Annotations.DUMPABLE)

internal fun ClassDescriptor.findDumpableAnnotationEntry(): KtAnnotationEntry? =
    (annotations.findAnnotation(FqNames.Annotations.DUMPABLE) as? LazyAnnotationDescriptor)?.annotationEntry

internal fun ClassDescriptor.findDumpToExtensionFunctionDescriptor(): FunctionDescriptor = module
    .getPackage(FqNames.Packages.RUNTIME)
    .memberScope
    .getContributedDescriptors(DescriptorKindFilter.FUNCTIONS) { it == Names.Functions.DUMP_TO }
    .single() as FunctionDescriptor
