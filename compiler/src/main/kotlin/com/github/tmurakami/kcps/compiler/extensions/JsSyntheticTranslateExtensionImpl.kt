package com.github.tmurakami.kcps.compiler.extensions

import com.github.tmurakami.kcps.compiler.PluginEnabledOn
import com.github.tmurakami.kcps.compiler.codegen.js.JsDumpToFunctionGenerator
import org.jetbrains.kotlin.descriptors.ClassDescriptor
import org.jetbrains.kotlin.js.translate.context.TranslationContext
import org.jetbrains.kotlin.js.translate.declaration.DeclarationBodyVisitor
import org.jetbrains.kotlin.js.translate.extensions.JsSyntheticTranslateExtension
import org.jetbrains.kotlin.psi.KtPureClassOrObject

internal class JsSyntheticTranslateExtensionImpl(private val pluginEnabledOn: PluginEnabledOn) :
    JsSyntheticTranslateExtension {
    override fun generateClassSyntheticParts(
        declaration: KtPureClassOrObject,
        descriptor: ClassDescriptor,
        translator: DeclarationBodyVisitor,
        context: TranslationContext
    ) {
        if (pluginEnabledOn(descriptor)) JsDumpToFunctionGenerator(translator, context).generateIfDumpable(descriptor)
    }
}
