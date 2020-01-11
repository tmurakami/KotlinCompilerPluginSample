package com.github.tmurakami.kcps.compiler.codegen.jvm

import com.github.tmurakami.kcps.compiler.codegen.DumpToFunctionGenerator
import org.jetbrains.kotlin.codegen.ExpressionCodegen
import org.jetbrains.kotlin.codegen.FunctionGenerationStrategy
import org.jetbrains.kotlin.codegen.ImplementationBodyCodegen
import org.jetbrains.kotlin.descriptors.FunctionDescriptor
import org.jetbrains.kotlin.resolve.jvm.AsmTypes
import org.jetbrains.kotlin.resolve.jvm.diagnostics.OtherOriginFromPure
import org.jetbrains.kotlin.resolve.jvm.jvmSignature.JvmMethodSignature

internal class JvmDumpToFunctionGenerator(private val bodyCodegen: ImplementationBodyCodegen) :
    DumpToFunctionGenerator {
    override fun FunctionDescriptor.generate(calleeDescriptor: FunctionDescriptor) {
        val bodyCodegen = bodyCodegen
        val strategy = object : FunctionGenerationStrategy.CodegenBased(bodyCodegen.state) {
            override fun doGenerateBody(codegen: ExpressionCodegen, signature: JvmMethodSignature) =
                codegen.v.run {
                    load(0, AsmTypes.OBJECT_TYPE)
                    load(1, AsmTypes.OBJECT_TYPE)
                    val name = calleeDescriptor.name.asString()
                    val descriptor = StringBuilder("(").apply {
                        append(codegen.asmType(calleeDescriptor.extensionReceiverParameter!!.type).descriptor)
                        calleeDescriptor.valueParameters.forEach { append(codegen.asmType(it.type)) }
                    }.append(")").append(codegen.asmType(calleeDescriptor.returnType!!).descriptor).toString()
                    invokestatic("com/github/tmurakami/kcps/DumperKt", name, descriptor, false)
                    areturn(AsmTypes.OBJECT_TYPE)
                }
        }
        bodyCodegen.functionCodegen.generateMethod(OtherOriginFromPure(bodyCodegen.myClass, this), this, strategy)
    }
}
