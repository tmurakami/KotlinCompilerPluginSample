package com.github.tmurakami.kcps.idea.extensions

import com.github.tmurakami.kcps.compiler.extensions.ExpressionCodegenExtensionImpl
import com.github.tmurakami.kcps.idea.compilerPluginIntroduced
import org.jetbrains.kotlin.codegen.extensions.ExpressionCodegenExtension

internal class IdeaExpressionCodegenExtension :
    ExpressionCodegenExtension by ExpressionCodegenExtensionImpl(::compilerPluginIntroduced)
