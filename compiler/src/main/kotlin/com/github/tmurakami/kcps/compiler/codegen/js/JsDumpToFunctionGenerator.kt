package com.github.tmurakami.kcps.compiler.codegen.js

import com.github.tmurakami.kcps.compiler.codegen.DumpToFunctionGenerator
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
) : DumpToFunctionGenerator {
    override fun FunctionDescriptor.generate(calleeDescriptor: FunctionDescriptor) {
        val context = context
        val innerContext = context.newDeclaration(this)
        val parameterNames = valueParameters.map { context.getNameForDescriptor(it) }
        val function = context.getFunctionObject(this).apply {
            parameters += parameterNames.map { JsParameter(it) }
            body.statements += JsReturn(
                JsInvocation(
                    innerContext.getInnerReference(calleeDescriptor),
                    JsThisRef(),
                    *parameterNames.map { JsNameRef(it) }.toTypedArray()
                )
            )
        }
        translator.addFunction(this, function, null)
    }
}
