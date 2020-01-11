package com.github.tmurakami.kcps

@Deprecated("", level = DeprecationLevel.HIDDEN)
fun <A : Appendable> Any.dumpTo(output: A): A = output.also { it.append(toString()) }
