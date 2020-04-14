package com.github.tmurakami.kcps.idea.extensions

import com.github.tmurakami.kcps.compiler.extensions.JsSyntheticTranslateExtensionImpl
import com.github.tmurakami.kcps.idea.compilerPluginIntroduced
import org.jetbrains.kotlin.js.translate.extensions.JsSyntheticTranslateExtension

internal class IdeaJsSyntheticTranslateExtension :
    JsSyntheticTranslateExtension by JsSyntheticTranslateExtensionImpl(::compilerPluginIntroduced)
