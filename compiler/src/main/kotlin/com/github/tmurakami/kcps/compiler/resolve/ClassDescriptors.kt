package com.github.tmurakami.kcps.compiler.resolve

import org.jetbrains.kotlin.descriptors.ClassDescriptor
import org.jetbrains.kotlin.descriptors.FunctionDescriptor
import org.jetbrains.kotlin.psi.KtAnnotationEntry
import org.jetbrains.kotlin.psi2ir.findSingleFunction
import org.jetbrains.kotlin.resolve.descriptorUtil.module
import org.jetbrains.kotlin.resolve.lazy.descriptors.LazyAnnotationDescriptor

internal fun ClassDescriptor.isDumpable(): Boolean =
    isData && annotations.hasAnnotation(FqNames.Annotations.DUMPABLE)

internal fun ClassDescriptor.findDumpableAnnotationEntry(): KtAnnotationEntry? =
    (annotations.findAnnotation(FqNames.Annotations.DUMPABLE) as? LazyAnnotationDescriptor)?.annotationEntry

internal fun ClassDescriptor.findDumpToExtensionFunctionDescriptor(): FunctionDescriptor =
    module.getPackage(FqNames.Packages.RUNTIME).memberScope.findSingleFunction(Names.Functions.DUMP_TO)
