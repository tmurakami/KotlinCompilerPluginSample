package com.github.tmurakami.kcps.compiler.codegen.ir

import com.github.tmurakami.kcps.compiler.codegen.AbstractDumpToFunctionGenerator
import org.jetbrains.kotlin.backend.common.BackendContext
import org.jetbrains.kotlin.backend.common.lower.at
import org.jetbrains.kotlin.backend.common.lower.createIrBuilder
import org.jetbrains.kotlin.descriptors.FunctionDescriptor
import org.jetbrains.kotlin.descriptors.ModuleDescriptor
import org.jetbrains.kotlin.descriptors.ParameterDescriptor
import org.jetbrains.kotlin.ir.builders.IrBlockBodyBuilder
import org.jetbrains.kotlin.ir.builders.irBlockBody
import org.jetbrains.kotlin.ir.builders.irCall
import org.jetbrains.kotlin.ir.builders.irGet
import org.jetbrains.kotlin.ir.builders.irReturn
import org.jetbrains.kotlin.ir.declarations.IrClass
import org.jetbrains.kotlin.ir.declarations.IrDeclarationOriginImpl
import org.jetbrains.kotlin.ir.declarations.IrSimpleFunction
import org.jetbrains.kotlin.ir.declarations.addMember
import org.jetbrains.kotlin.ir.declarations.impl.IrTypeParameterImpl
import org.jetbrains.kotlin.ir.declarations.impl.IrValueParameterImpl
import org.jetbrains.kotlin.ir.symbols.impl.IrTypeParameterSymbolImpl
import org.jetbrains.kotlin.ir.types.defaultType
import org.jetbrains.kotlin.ir.util.ConstantValueGenerator
import org.jetbrains.kotlin.ir.util.ReferenceSymbolTable
import org.jetbrains.kotlin.ir.util.SymbolTable
import org.jetbrains.kotlin.ir.util.TypeTranslator
import org.jetbrains.kotlin.resolve.descriptorUtil.module

private val ORIGIN = object : IrDeclarationOriginImpl("KotlinCompilerPluginSample") {}

internal class IrDumpToFunctionGenerator(private val irClass: IrClass, private val backendContext: BackendContext) :
    AbstractDumpToFunctionGenerator() {
    private val symbolTable = SymbolTable()
    private val externalSymbolTable = backendContext.ir.symbols.externalSymbolTable
    private val typeTranslator = irClass.descriptor.module.createTypeTranslator(externalSymbolTable)

    override fun FunctionDescriptor.generate(calleeDescriptor: FunctionDescriptor) =
        irClass.declareSimpleFunction(this) {
            +irReturn(irCall(externalSymbolTable.referenceSimpleFunction(calleeDescriptor)).apply {
                extensionReceiver = irGet(it.dispatchReceiverParameter!!)
                it.typeParameters.forEachIndexed { i, parameter -> putTypeArgument(i, parameter.defaultType) }
                it.valueParameters.forEachIndexed { i, parameter -> putValueArgument(i, irGet(parameter)) }
            })
        }

    private fun IrClass.declareSimpleFunction(
        descriptor: FunctionDescriptor,
        block: IrBlockBodyBuilder.(function: IrSimpleFunction) -> Unit
    ) {
        val f = symbolTable.declareSimpleFunction(startOffset, endOffset, ORIGIN, descriptor)
        val startOffset = f.startOffset
        val endOffset = f.endOffset
        f.typeParameters += descriptor.typeParameters.map {
            IrTypeParameterImpl(
                startOffset,
                endOffset,
                ORIGIN,
                IrTypeParameterSymbolImpl(it)
            ).apply {
                parent = f
            }
        }
        fun ParameterDescriptor.toIrValueParameter() = IrValueParameterImpl(
            startOffset,
            endOffset,
            ORIGIN,
            this,
            typeTranslator.translateType(type),
            null
        ).apply {
            parent = f
        }
        addMember(f.apply {
            typeTranslator.buildWithScope(this) {
                parent = this@declareSimpleFunction
                returnType = typeTranslator.translateType(descriptor.returnType!!)
                dispatchReceiverParameter = descriptor.dispatchReceiverParameter?.toIrValueParameter()
                extensionReceiverParameter = descriptor.extensionReceiverParameter?.toIrValueParameter()
                typeParameters.forEach { it.superTypes += it.descriptor.upperBounds.map(typeTranslator::translateType) }
                valueParameters += descriptor.valueParameters.map(ParameterDescriptor::toIrValueParameter)
                body = backendContext.createIrBuilder(f.symbol).at(f).irBlockBody { block(f) }
            }
        })
    }

    private fun ModuleDescriptor.createTypeTranslator(symbolTable: ReferenceSymbolTable): TypeTranslator =
        TypeTranslator(
            symbolTable,
            backendContext.irBuiltIns.languageVersionSettings,
            builtIns
        ).also {
            it.constantValueGenerator = ConstantValueGenerator(this, symbolTable).apply { typeTranslator = it }
        }
}
