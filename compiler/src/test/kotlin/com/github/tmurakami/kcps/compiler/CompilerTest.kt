package com.github.tmurakami.kcps.compiler

import org.intellij.lang.annotations.Language
import kotlin.test.Test

class CompilerTest {
    @Language("kotlin")
    private val source = """
        package foo
        
        import com.github.tmurakami.kcps.Dumpable
        
        @Dumpable
        data class Foo(val s: String)
        
        fun <A : Appendable> dumpFoo(foo: Foo, output: A): A = foo.dumpTo(output)
        """.trimIndent()

    private val file = KotlinFile("Foo.kt", source)

    @Test
    fun testJs() = jsCompiler().compile(file).assertSucceeded()

    @Test
    fun testJvm() = jvmCompiler().compile(file).assertSucceeded()

    @Test
    fun testJvmIr() = jvmIrCompiler().compile(file).assertSucceeded()
}
