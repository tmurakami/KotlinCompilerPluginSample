package com.github.tmurakami.kcps.compiler.resolve

import com.github.tmurakami.kcps.Dumpable
import org.jetbrains.kotlin.name.FqName
import org.jetbrains.kotlin.name.Name

internal object Names {
    object Functions {
        val DUMP_TO = Name.identifier("dumpTo")
    }
}

internal object FqNames {
    object Packages {
        val RUNTIME = FqName("com.github.tmurakami.kcps")
    }

    object Annotations {
        val DUMPABLE = FqName(Dumpable::class.java.name)
    }
}
