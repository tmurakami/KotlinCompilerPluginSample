package com.github.tmurakami.kcps.compiler.extensions

import com.github.tmurakami.kcps.compiler.ALWAYS_ENABLED
import com.github.tmurakami.kcps.compiler.PluginEnabledOn
import com.github.tmurakami.kcps.compiler.codegen.jvm.JvmDumpToFunctionGenerator
import org.jetbrains.kotlin.codegen.ImplementationBodyCodegen
import org.jetbrains.kotlin.codegen.extensions.ExpressionCodegenExtension

class ExpressionCodegenExtensionImpl(private val pluginEnabledOn: PluginEnabledOn = ALWAYS_ENABLED) :
    ExpressionCodegenExtension {
    override fun generateClassSyntheticParts(codegen: ImplementationBodyCodegen) = codegen.descriptor.let {
        if (pluginEnabledOn(it)) JvmDumpToFunctionGenerator(codegen).generateIfDumpable(it)
    }
}
