package com.github.tmurakami.kcps.compiler.extensions

import com.github.tmurakami.kcps.compiler.PluginEnabledOn
import com.github.tmurakami.kcps.compiler.codegen.generateDumpToFunctionIfNeeded
import com.github.tmurakami.kcps.compiler.codegen.ir.IrDumpToFunctionGenerator
import org.jetbrains.kotlin.backend.common.BackendContext
import org.jetbrains.kotlin.backend.common.extensions.IrGenerationExtension
import org.jetbrains.kotlin.ir.IrStatement
import org.jetbrains.kotlin.ir.declarations.IrClass
import org.jetbrains.kotlin.ir.declarations.IrFile
import org.jetbrains.kotlin.ir.visitors.IrElementTransformerVoid
import org.jetbrains.kotlin.ir.visitors.transformChildrenVoid
import org.jetbrains.kotlin.resolve.BindingContext

internal class IrGenerationExtensionImpl(private val pluginEnabledOn: PluginEnabledOn) : IrGenerationExtension {
    override fun generate(file: IrFile, backendContext: BackendContext, bindingContext: BindingContext) =
        file.transformChildrenVoid(object : IrElementTransformerVoid() {
            override fun visitClass(declaration: IrClass): IrStatement {
                declaration.descriptor.run {
                    if (pluginEnabledOn(this)) {
                        generateDumpToFunctionIfNeeded { IrDumpToFunctionGenerator(declaration, backendContext) }
                    }
                }
                return super.visitClass(declaration)
            }
        })
}
