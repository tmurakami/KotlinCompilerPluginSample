package com.github.tmurakami.kcps.idea.extensions

import com.github.tmurakami.kcps.compiler.extensions.IrGenerationExtensionImpl
import com.github.tmurakami.kcps.idea.compilerPluginIntroduced
import org.jetbrains.kotlin.backend.common.extensions.IrGenerationExtension

internal class IdeaIrGenerationExtension :
    IrGenerationExtension by IrGenerationExtensionImpl(::compilerPluginIntroduced)
