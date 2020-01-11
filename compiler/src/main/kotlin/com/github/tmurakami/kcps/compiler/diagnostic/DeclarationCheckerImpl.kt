package com.github.tmurakami.kcps.compiler.diagnostic

import com.github.tmurakami.kcps.compiler.PluginEnabledOn
import com.github.tmurakami.kcps.compiler.resolve.findDumpableAnnotationEntry
import org.jetbrains.kotlin.descriptors.ClassDescriptor
import org.jetbrains.kotlin.descriptors.DeclarationDescriptor
import org.jetbrains.kotlin.psi.KtDeclaration
import org.jetbrains.kotlin.resolve.checkers.DeclarationChecker
import org.jetbrains.kotlin.resolve.checkers.DeclarationCheckerContext

internal class DeclarationCheckerImpl(private val pluginEnabledOn: PluginEnabledOn) : DeclarationChecker {
    override fun check(
        declaration: KtDeclaration,
        descriptor: DeclarationDescriptor,
        context: DeclarationCheckerContext
    ) {
        if (descriptor !is ClassDescriptor || !pluginEnabledOn(descriptor)) return
        val annotationEntry = descriptor.findDumpableAnnotationEntry() ?: return
        if (!descriptor.isData) context.reportFromPlugin(PluginErrors.DATA_ONLY.on(annotationEntry))
    }
}
