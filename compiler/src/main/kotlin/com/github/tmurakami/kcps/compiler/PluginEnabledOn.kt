package com.github.tmurakami.kcps.compiler

import org.jetbrains.kotlin.descriptors.DeclarationDescriptor

typealias PluginEnabledOn = (declarationDescriptor: DeclarationDescriptor) -> Boolean

internal val ALWAYS_ENABLED: PluginEnabledOn = { true }
