package com.github.tmurakami.kcps.compiler.extensions

import com.github.tmurakami.kcps.compiler.PluginEnabledOn
import com.github.tmurakami.kcps.compiler.codegen.generateDumpToFunctionIfNeeded
import com.github.tmurakami.kcps.compiler.codegen.jvm.JvmDumpToFunctionGenerator
import org.jetbrains.kotlin.codegen.ImplementationBodyCodegen
import org.jetbrains.kotlin.codegen.extensions.ExpressionCodegenExtension

internal class ExpressionCodegenExtensionImpl(private val pluginEnabledOn: PluginEnabledOn) :
    ExpressionCodegenExtension {
    override fun generateClassSyntheticParts(codegen: ImplementationBodyCodegen) = codegen.descriptor.run {
        if (pluginEnabledOn(this)) generateDumpToFunctionIfNeeded { JvmDumpToFunctionGenerator(codegen) }
    }
}
