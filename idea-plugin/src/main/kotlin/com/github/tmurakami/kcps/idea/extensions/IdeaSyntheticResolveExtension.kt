package com.github.tmurakami.kcps.idea.extensions

import com.github.tmurakami.kcps.compiler.extensions.SyntheticResolveExtensionImpl
import com.github.tmurakami.kcps.idea.compilerPluginIntroduced
import org.jetbrains.kotlin.resolve.extensions.SyntheticResolveExtension

class IdeaSyntheticResolveExtension :
    SyntheticResolveExtension by SyntheticResolveExtensionImpl(::compilerPluginIntroduced)
