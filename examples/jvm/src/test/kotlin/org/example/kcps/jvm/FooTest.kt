package org.example.kcps.jvm

import kotlin.test.Test
import kotlin.test.assertEquals

class FooTest {
    @Test
    fun test() {
        val foo = Foo("foo")
        assertEquals(foo.toString(), foo.dumpTo(StringBuilder()).toString())
    }
}
