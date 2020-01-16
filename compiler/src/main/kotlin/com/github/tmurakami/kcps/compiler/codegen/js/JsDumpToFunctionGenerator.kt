package com.github.tmurakami.kcps.compiler.codegen.js

import com.github.tmurakami.kcps.compiler.codegen.AbstractDumpToFunctionGenerator
import org.jetbrains.kotlin.descriptors.FunctionDescriptor
import org.jetbrains.kotlin.js.backend.ast.JsInvocation
import org.jetbrains.kotlin.js.backend.ast.JsNameRef
import org.jetbrains.kotlin.js.backend.ast.JsParameter
import org.jetbrains.kotlin.js.backend.ast.JsReturn
import org.jetbrains.kotlin.js.backend.ast.JsThisRef
import org.jetbrains.kotlin.js.translate.context.TranslationContext
import org.jetbrains.kotlin.js.translate.declaration.DeclarationBodyVisitor

internal class JsDumpToFunctionGenerator(
    private val translator: DeclarationBodyVisitor,
    private val context: TranslationContext
) : AbstractDumpToFunctionGenerator() {
    override fun FunctionDescriptor.generate(calleeDescriptor: FunctionDescriptor) {
        val context = context
        val innerContext = context.newDeclaration(this)
        val valueParameterNames = valueParameters.map(context::getNameForDescriptor)
        val function = context.getFunctionObject(this).apply {
            parameters += valueParameterNames.map(::JsParameter)
            body.statements += JsReturn(
                JsInvocation(
                    innerContext.getInnerReference(calleeDescriptor),
                    JsThisRef(),
                    *valueParameterNames.map(::JsNameRef).toTypedArray()
                )
            )
        }
        translator.addFunction(this, function, null)
    }
}
