package com.github.tmurakami.kcps.compiler.extensions

import com.github.tmurakami.kcps.compiler.PluginEnabledOn
import com.github.tmurakami.kcps.compiler.codegen.ir.IrDumpToFunctionGenerator
import org.jetbrains.kotlin.backend.common.extensions.IrGenerationExtension
import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.ir.IrStatement
import org.jetbrains.kotlin.ir.declarations.IrClass
import org.jetbrains.kotlin.ir.declarations.IrModuleFragment
import org.jetbrains.kotlin.ir.visitors.IrElementTransformerVoid
import org.jetbrains.kotlin.ir.visitors.transformChildrenVoid

internal class IrGenerationExtensionImpl(private val pluginEnabledOn: PluginEnabledOn) : IrGenerationExtension {
    override fun generate(moduleFragment: IrModuleFragment, pluginContext: IrPluginContext) =
        moduleFragment.transformChildrenVoid(object : IrElementTransformerVoid() {
            override fun visitClass(declaration: IrClass): IrStatement {
                declaration.descriptor.let {
                    if (pluginEnabledOn(it)) {
                        IrDumpToFunctionGenerator(declaration, pluginContext).generateIfDumpable(it)
                    }
                }
                return super.visitClass(declaration)
            }
        })
}
