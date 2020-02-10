package com.github.tmurakami.kcps.compiler

import org.intellij.lang.annotations.Language
import org.jetbrains.kotlin.cli.common.ExitCode
import kotlin.test.Test

class CompilerPluginTest {
    @Language("kotlin")
    private val file = """
        package foo
        
        import com.github.tmurakami.kcps.Dumpable
        
        @Dumpable
        data class Foo(val s: String)
        
        fun <A : Appendable> dumpFoo(foo: Foo, output: A): A = foo.dumpTo(output)
        """.trimIndent().let { KotlinFile("Foo.kt", it) }

    @Language("kotlin")
    val errorFile = """
        package foo
        
        import com.github.tmurakami.kcps.Dumpable
        
        @Dumpable
        class Foo(val s: String)
        """.trimIndent().let { KotlinFile("Foo.kt", it) }
    private val errorMessage = """
        Foo.kt:5:1: error: @Dumpable can only be annotated on a data class
        @Dumpable
        ^
        
        """.trimIndent()

    @Test
    fun `ensure the JS compiler adds the dumpTo function to a data class`() =
        jsCompiler().compile(file).assertSucceeded()

    @Test
    fun `ensure the JVM compiler adds the dumpTo function to a data class`() =
        jvmCompiler().compile(file).assertSucceeded()

    @Test
    fun `ensure the JVM IR compiler adds the dumpTo function to a data class`() =
        jvmIRCompiler().compile(file).assertSucceeded()

    @Test
    fun `ensure the Dumpable annotation cannot be annotated on a non-data class`() =
        jvmCompiler().compile(errorFile).assertResult(ExitCode.COMPILATION_ERROR, errorMessage)
}
